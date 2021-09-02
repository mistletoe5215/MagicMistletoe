package com.magic.multi.theme.core.action

import android.annotation.SuppressLint
import android.app.Application
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.arch.core.executor.ArchTaskExecutor
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.magic.multi.theme.core.annotation.CalledAfterSetThemeFactory
import com.magic.multi.theme.core.api.ILoadListener
import com.magic.multi.theme.core.api.IOperationHandler
import com.magic.multi.theme.core.api.IResourceHandler
import com.magic.multi.theme.core.base.BaseAttr
import com.magic.multi.theme.core.constants.AttrConstants
import com.magic.multi.theme.core.constants.SkinConfig.MULTI_THEME_TAG
import com.magic.multi.theme.core.exception.SkinLoadException
import com.magic.multi.theme.core.exception.SkinLoadException.Companion.NULL_SKIN_PATH_EXCEPTION
import com.magic.multi.theme.core.exception.SkinLoadException.Companion.SKIN_FILE_NOT_EXISTS
import com.magic.multi.theme.core.exception.SkinLoadException.Companion.SKIN_GET_NULL_RESOURCES
import com.magic.multi.theme.core.factory.MultiThemeFactory
import com.magic.multi.theme.core.strategy.IThemeLoadStrategy
import com.magic.multi.theme.core.utils.AttrConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.File

/**
 * 皮肤切换处理器
 * Created by mistletoe
 * on 7/23/21
 */
class SkinLoadManager private constructor() : IOperationHandler, IResourceHandler {

    companion object {
        @Volatile
        private var instance: SkinLoadManager? = null
        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: SkinLoadManager().also { instance = it }
            }
    }

    /**
     * 宿主App
     */
    private lateinit var app: Application

    /**
     * 当前主题包名
     */
    private var mSkinPkgName: String? = null

    /**
     * 当前主题Resource对象
     */
    private var mResource: Resources? = null

    /**
     * 默认皮肤标志位
     */
    private var isDefaultSkin = false


    /**
     * 是否为第三方皮肤
     * true:第三方皮肤 false:默认皮肤
     */
    val isExternalSkin: Boolean
        get() = !isDefaultSkin && mResource != null

    /**
     * 换肤task协程scope
     */
    private val mScope: CoroutineScope = CoroutineScope(Dispatchers.Main)

    /**
     * 当前页面-> MultiThemeFactory 集合
     */
    private val mPageFactoryMap: MutableMap<AppCompatActivity, MultiThemeFactory> = mutableMapOf()

    /**
     * 当前主题策略
     */
    private var mStrategy: IThemeLoadStrategy? = null

    /**
     * 初始化
     */
    override fun init(app: Application) {
        this.app = app
    }

    /**
     * 异步加载皮肤资源文件
     * @param mSkinFilePath 资源文件路径
     * @param iLoadListener 加载皮肤资源文件结果回调
     */
    @Suppress("ThrowableNotThrown")
    private fun loadSkin(mSkinFilePath: String?, iLoadListener: ILoadListener?) {
        mScope.launch {
            Log.i(MULTI_THEME_TAG, "begin load skin resource")
            iLoadListener?.onStart()
            mSkinFilePath?.let {
                val file = File(mSkinFilePath)
                if (!file.exists()) {
                    iLoadListener?.onFailed(SkinLoadException(NULL_SKIN_PATH_EXCEPTION))
                    return@launch
                }
                val resourcesDeferred = async(Dispatchers.IO) {
                    try {
                        val mInfo = app.packageManager.getPackageArchiveInfo(
                            it,
                            PackageManager.GET_ACTIVITIES
                        )
                        mSkinPkgName = mInfo?.packageName
                        //hook addAssetPath
                        val assetManager = AssetManager::class.java.newInstance()
                        val addAssetPath =
                            assetManager.javaClass.getMethod("addAssetPath", String::class.java)
                        addAssetPath.invoke(assetManager, it)
                        //previous resources
                        val previousRes = app.resources
                        isDefaultSkin = false
                        //extends previous configuration,generate new resources
                        Resources(
                            assetManager,
                            previousRes.displayMetrics,
                            previousRes.configuration
                        )
                    } catch (e: Exception) {
                        Log.e(MULTI_THEME_TAG, e.message.toString())
                        null
                    }
                }
                val resources = resourcesDeferred.await()
                resources?.let { res ->
                    mResource = res
                    iLoadListener?.onSuccess()
                } ?: run {
                    isDefaultSkin = true
                    iLoadListener?.onFailed(SkinLoadException(SKIN_GET_NULL_RESOURCES))
                }

            } ?: run {
                iLoadListener?.onFailed(SkinLoadException(SKIN_FILE_NOT_EXISTS))
            }
        }
    }

    /**
     * 存储恢复默认主题
     */
    fun restoreDefaultTheme() {
        isDefaultSkin = true
        mResource = app.resources
        applyTheme()
    }

    @CalledAfterSetThemeFactory
    override fun bindPage(page: AppCompatActivity) {
        (page.layoutInflater.factory as? MultiThemeFactory)?.apply {
            mPageFactoryMap[page] = this
        }
        page.lifecycle.addObserver(object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun onDestroy() {
                mPageFactoryMap.remove(page)
            }
        })
    }

    @SuppressLint("RestrictedApi")
    override fun loadThemeByStrategy(strategy: IThemeLoadStrategy, iLoadListener: ILoadListener?) {
        ArchTaskExecutor.getIOThreadExecutor().execute{
            this.mStrategy = strategy
            val filePath  = strategy.getOrGenerateThemePackage(this.app)
            if(filePath.isNullOrEmpty()){
                iLoadListener?.onFailed(SkinLoadException(NULL_SKIN_PATH_EXCEPTION))
            }else{
                loadSkin(filePath,iLoadListener)
            }
        }
    }

    /**
     * 通知所有缓存页面集的页面更新主题皮肤
     */
    override fun applyTheme() {
        mPageFactoryMap.forEach {
            it.value.applyTheme()
        }
    }

    override fun configCustomAttrs(attrMap: MutableMap<String, Class<out BaseAttr>>) {
        attrMap.forEach {
            AttrConfig.externalAttrMap[it.key] = it.value
        }
        AttrConstants.attrConstantList.addAll(attrMap.keys)
    }

    override fun clean() {
        mPageFactoryMap.clear()
    }
    //===================  检测到xml中的锚点控件后进行动态替换的方法  自定义View 需要实现===================
    /**
     * 根据图片资源Id索引皮肤资源中的图片
     * @param resId 资源Id
     * @return 图片drawable对象
     */
    override fun getDrawable(resId: Int): Drawable {
        val originResources = app.resources
        val originDrawable = originResources.getDrawable(resId)
        if (null == mResource || TextUtils.isEmpty(mSkinPkgName)) {
            return originDrawable
        }
        //这里决定了换肤文件中的资源命名需要和宿主app资源命名相同
        val entryName = originResources.getResourceEntryName(resId)
        val resourceId = mResource!!.getIdentifier(entryName, "drawable", mSkinPkgName)
        try {
            return mResource!!.getDrawable(resourceId)
        } catch (e: Exception) {
            // do nothing
        }
        return originDrawable
    }

    /**
     * 根据字符串资源Id索引皮肤资源中的strings.xml中的值
     * @param resId 字符串资源Id
     * @return strings.xml中对应的值
     */
    override fun getTextString(resId: Int): String {
        val originResources = app.resources
        val originText = originResources.getString(resId)
        if (null == mResource || TextUtils.isEmpty(mSkinPkgName)) {
            return originText
        }
        //这里决定了换肤文件中的资源命名需要和宿主app资源命名相同
        val entryName = originResources.getResourceEntryName(resId)
        val resourceId = mResource!!.getIdentifier(entryName, "string", mSkinPkgName)
        return try {
            mResource!!.getString(resourceId)
        } catch (e: Exception) {
            originText
        }
    }

    /**
     * 根据颜色的资源id索引皮肤资源中的color.xml中的值
     * @param resId 颜色的资源id
     * @return 皮肤资源中的color.xml中对应的值
     */
    override fun getColor(resId: Int): Int {
        Log.d("Mistletoe","SkinLoadManager getColor executed!!!")
        val originResources = app.resources
        val originColor = originResources.getColor(resId)
        if (null == mResource || TextUtils.isEmpty(mSkinPkgName)) {
            return originColor
        }
        val entryName = originResources.getResourceEntryName(resId)
        val resourceId = mResource!!.getIdentifier(entryName, "color", mSkinPkgName)
        return try {
            return mResource!!.getColor(resourceId)
        } catch (e: Exception) {
            originColor
        }
    }

    override fun getAssetsFilePath(fileName: String): String {
        return this.mStrategy?.let { String.format("%s/%s", it.themeName, fileName) } ?: fileName
    }

}