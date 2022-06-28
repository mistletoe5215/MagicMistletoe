package com.magic.multi.theme.core.action

import android.annotation.SuppressLint
import android.app.Application
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.content.res.ColorStateList
import android.content.res.Resources
import android.content.res.Resources.NotFoundException
import android.graphics.drawable.Drawable
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.magic.multi.theme.core.annotation.CalledAfterSetThemeFactory
import com.magic.multi.theme.core.api.ILoadListener
import com.magic.multi.theme.core.api.IOperationHandler
import com.magic.multi.theme.core.api.IResourceHandler
import com.magic.multi.theme.core.base.BaseAttr
import com.magic.multi.theme.core.cache.GlobalStore
import com.magic.multi.theme.core.constants.AttrConstants
import com.magic.multi.theme.core.exception.SkinLoadException
import com.magic.multi.theme.core.exception.SkinLoadException.Companion.NULL_SKIN_PATH_EXCEPTION
import com.magic.multi.theme.core.exception.SkinLoadException.Companion.SKIN_GET_NULL_RESOURCES
import com.magic.multi.theme.core.factory.MultiThemeFactory
import com.magic.multi.theme.core.log.MultiThemeLog
import com.magic.multi.theme.core.strategy.IThemeLoadStrategy
import com.magic.multi.theme.core.utils.AttrConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

    private var mSkinResource: Resources? = null

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
     * 外部设置的主题包更新状态监听list
     */
    private var mOuterLoadListener: MutableList<ILoadListener> = mutableListOf()

    /**
     * 初始化
     */
    override fun init(app: Application, enableDebug: Boolean) {
        MultiThemeLog.enable(enableDebug)
        this.app = app
    }

    /**
     * 异步加载皮肤资源文件
     * @param skinFilePath 资源文件路径
     * @param iLoadListener 加载皮肤资源文件结果回调
     */
    @Suppress("ThrowableNotThrown")
    private fun loadSkin(skinFilePath: String, iLoadListener: ILoadListener?) {
        val cacheThemeResource = GlobalStore.themeCache[skinFilePath]
        if (cacheThemeResource != null) {
            mResource = cacheThemeResource
            mSkinResource = cacheThemeResource
            iLoadListener?.onSuccess()
            mOuterLoadListener.forEach {
                it.onSuccess()
            }
            return
        }
        mScope.launch {
            MultiThemeLog.i("begin load theme resource")
            iLoadListener?.onStart()
            mOuterLoadListener.forEach {
                it.onStart()
            }
            val resources = skinFilePath.convert2ThemeResource()
            if (resources != null) {
                mResource = resources
                mSkinResource = resources
                iLoadListener?.onSuccess()
                mOuterLoadListener.forEach {
                    it.onSuccess()
                }
                GlobalStore.themeCache[skinFilePath] = resources
            } else {
                isDefaultSkin = true
                iLoadListener?.onFailed(SkinLoadException(SKIN_GET_NULL_RESOURCES))
                mOuterLoadListener.forEach {
                    it.onFailed(SkinLoadException(SKIN_GET_NULL_RESOURCES))
                }
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
        mOuterLoadListener.forEach {
            it.onSuccess()
        }
    }

    @CalledAfterSetThemeFactory
    override fun bindPage(page: AppCompatActivity) {
        (page.layoutInflater.factory2 as? MultiThemeFactory)?.apply {
            mPageFactoryMap[page] = this
        }
        page.lifecycle.addObserver(object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun onDestroy() {
                mPageFactoryMap.remove(page)
            }
        })
    }

    override fun loadThemeByStrategy(strategy: IThemeLoadStrategy, iLoadListener: ILoadListener?) {
        this.mStrategy = strategy
        val filePath = strategy.getOrGenerateThemePackage(this.app)
        if (filePath.isNullOrEmpty()) {
            iLoadListener?.onFailed(SkinLoadException(NULL_SKIN_PATH_EXCEPTION))
        } else {
            loadSkin(filePath, iLoadListener)
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

    override fun addExtraLoadListener(lifecycleOwner: LifecycleOwner, loadListener: ILoadListener) {
        synchronized(mOuterLoadListener) {
            mOuterLoadListener.add(loadListener)
        }
        lifecycleOwner.lifecycle.addObserver(object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun onDestroy() {
                mOuterLoadListener.remove(loadListener)
            }
        })
    }

    override fun clean() {
        mPageFactoryMap.clear()
    }

    override fun preLoadThemeByStrategy(
        strategy: IThemeLoadStrategy,
        iLoadListener: ILoadListener?,
    ) {
        val filePath = strategy.getOrGenerateThemePackage(this.app)
        if (filePath.isNullOrEmpty()) {
            iLoadListener?.onFailed(SkinLoadException(NULL_SKIN_PATH_EXCEPTION))
        } else {
            mScope.launch {
                MultiThemeLog.i("begin preLoad theme resource")
                iLoadListener?.onStart()
                val resources = filePath.convert2ThemeResource()
                if (resources != null) {
                    mSkinResource = resources
                    iLoadListener?.onSuccess()
                } else {
                    iLoadListener?.onFailed(SkinLoadException(SKIN_GET_NULL_RESOURCES))
                }
            }
        }
    }

    private suspend fun String.convert2ThemeResource(): Resources? {
        return withContext(Dispatchers.IO) {
            try {
                val mInfo = app.packageManager.getPackageArchiveInfo(
                    this@convert2ThemeResource,
                    PackageManager.GET_ACTIVITIES
                )
                mSkinPkgName = mInfo?.packageName
                //hook addAssetPath
                val assetManager = AssetManager::class.java.newInstance()
                val addAssetPath =
                    assetManager.javaClass.getMethod("addAssetPath", String::class.java)
                addAssetPath.invoke(assetManager, this@convert2ThemeResource)
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
                MultiThemeLog.e(e.message.toString())
                null
            }
        }
    }
    //===================  检测到xml中的锚点控件后进行动态替换的方法  自定义View 需要实现===================
    /**
     * 根据图片资源Id索引皮肤资源中的图片
     * @param resId 资源Id
     * @return 图片drawable对象
     */
    @SuppressLint("UseCompatLoadingForDrawables")
    override fun getDrawable(resId: Int): Drawable {
        val originResources = app.resources
        val originDrawable = originResources.getDrawable(resId, null)
        if (null == mResource || TextUtils.isEmpty(mSkinPkgName)) {
            return originDrawable
        }
        //这里决定了换肤文件中的资源命名需要和宿主app资源命名相同
        val entryName = originResources.getResourceEntryName(resId)
        val resourceId = mResource!!.getIdentifier(entryName, "drawable", mSkinPkgName)
        return try {
            mResource!!.getDrawable(resourceId, null)
        } catch (e: Exception) {
            MultiThemeLog.d(
                "get theme drawable  failed with resId:${resId},use origin drawable"
            )
            originDrawable
        }
    }

    override fun getAppDrawable(resId: Int): Drawable {
        val originResources = app.resources
        return originResources.getDrawable(resId, null)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun getSkinDrawable(resId: Int): Drawable {
        val originResources = app.resources
        val originDrawable = originResources.getDrawable(resId, null)
        if (null == mSkinResource || TextUtils.isEmpty(mSkinPkgName)) {
            return originDrawable
        }
        //这里决定了换肤文件中的资源命名需要和宿主app资源命名相同
        val entryName = originResources.getResourceEntryName(resId)
        val resourceId = mSkinResource!!.getIdentifier(entryName, "drawable", mSkinPkgName)
        return try {
            mSkinResource!!.getDrawable(resourceId, null)
        } catch (e: Exception) {
            MultiThemeLog.d(
                "get skin theme drawable  failed with resId:${resId},use origin drawable"
            )
            originDrawable
        }
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
            MultiThemeLog.d(
                "get theme text string  failed with resId:${resId},use origin text string"
            )
            originText
        }
    }

    override fun getAppTextString(resId: Int): String {
        val originResources = app.resources
        return originResources.getString(resId)
    }

    override fun getSkinTextString(resId: Int): String {
        val originResources = app.resources
        val originText = originResources.getString(resId)
        if (null == mSkinResource || TextUtils.isEmpty(mSkinPkgName)) {
            return originText
        }
        //这里决定了换肤文件中的资源命名需要和宿主app资源命名相同
        val entryName = originResources.getResourceEntryName(resId)
        val resourceId = mSkinResource!!.getIdentifier(entryName, "string", mSkinPkgName)
        return try {
            mSkinResource!!.getString(resourceId)
        } catch (e: Exception) {
            MultiThemeLog.d(
                "get theme text string  failed with resId:${resId},use origin text string"
            )
            originText
        }
    }

    /**
     * 根据颜色的资源id索引皮肤资源中的color.xml中的值
     * @param resId 颜色的资源id
     * @return 皮肤资源中的color.xml中对应的值
     */
    override fun getColor(resId: Int): Int {
        val originResources = app.resources
        val originColor = originResources.getColor(resId, null)
        if (null == mResource || TextUtils.isEmpty(mSkinPkgName)) {
            return originColor
        }
        val entryName = originResources.getResourceEntryName(resId)
        val resourceId = mResource!!.getIdentifier(entryName, "color", mSkinPkgName)
        return try {
            return mResource!!.getColor(resourceId, null)
        } catch (e: Exception) {
            MultiThemeLog.d("get theme color  failed with resId:${resId},use origin color")
            originColor
        }
    }

    override fun getAppColor(resId: Int): Int {
        val originResources = app.resources
        return originResources.getColor(resId, null)
    }

    override fun getSkinColor(resId: Int): Int {
        val originResources = app.resources
        val originColor = originResources.getColor(resId, null)
        if (null == mSkinResource || TextUtils.isEmpty(mSkinPkgName)) {
            return originColor
        }
        val entryName = originResources.getResourceEntryName(resId)
        val resourceId = mSkinResource!!.getIdentifier(entryName, "color", mSkinPkgName)
        return try {
            return mSkinResource!!.getColor(resourceId, null)
        } catch (e: Exception) {
            MultiThemeLog.d("get theme color  failed with resId:${resId},use origin color")
            originColor
        }
    }

    /**
     * 根据资源id索引皮肤资源中dimen的值
     * @param resId 整数值的资源id
     * @return 皮肤资源中dimen对应的值
     */
    override fun getDimenString(resId: Int): String {
        val originResources = app.resources
        val originDimen = originResources.getString(resId)
        if (null == mResource || TextUtils.isEmpty(mSkinPkgName)) {
            return originDimen
        }
        val entryName = originResources.getResourceEntryName(resId)
        val resourceId = mResource!!.getIdentifier(entryName, "dimen", mSkinPkgName)
        return try {
            return mResource!!.getString(resourceId)
        } catch (e: Exception) {
            MultiThemeLog.d("get dimen value  failed with resId:${resId},use origin dimen value")
            originDimen
        }
    }

    override fun getAppDimenString(resId: Int): String {
        val originResources = app.resources
        return originResources.getString(resId)
    }

    override fun getSkinDimenString(resId: Int): String {
        val originResources = app.resources
        val originDimen = originResources.getString(resId)
        if (null == mSkinResource || TextUtils.isEmpty(mSkinPkgName)) {
            return originDimen
        }
        val entryName = originResources.getResourceEntryName(resId)
        val resourceId = mSkinResource!!.getIdentifier(entryName, "dimen", mSkinPkgName)
        return try {
            return mSkinResource!!.getString(resourceId)
        } catch (e: Exception) {
            MultiThemeLog.d("get dimen value  failed with resId:${resId},use origin dimen value")
            originDimen
        }
    }

    /**
     * 根据selector形式的颜色资源id索引皮肤资源中的color/xxx.xml文件的值
     * @param resId 颜色的资源id
     * @return ColorStateList 皮肤资源中的color/xxx.xml文件对应的ColorStateList对象
     */
    override fun getColorStateList(resId: Int): ColorStateList {
        val originResources = app.resources
        val originColorStateList = originResources.getColorStateList(resId, null)
        if (null == mResource || TextUtils.isEmpty(mSkinPkgName)) {
            return originColorStateList
        }
        val entryName = app.resources.getResourceEntryName(resId)
        val resourceId = mResource!!.getIdentifier(entryName, "color", mSkinPkgName)
        try {
            return mResource!!.getColorStateList(resourceId, null)
        } catch (e: NotFoundException) {
            MultiThemeLog.e(e.message.toString())
        }
        val states = Array(1) { IntArray(1) }
        return ColorStateList(states, intArrayOf(app.resources.getColor(resId, null)))
    }

    override fun getAppColorStateList(resId: Int): ColorStateList {
        val originResources = app.resources
        return originResources.getColorStateList(resId, null)
    }

    override fun getSkinColorStateList(resId: Int): ColorStateList {
        val originResources = app.resources
        val originColorStateList = originResources.getColorStateList(resId, null)
        if (null == mSkinResource || TextUtils.isEmpty(mSkinPkgName)) {
            return originColorStateList
        }
        val entryName = app.resources.getResourceEntryName(resId)
        val resourceId = mSkinResource!!.getIdentifier(entryName, "color", mSkinPkgName)
        try {
            return mSkinResource!!.getColorStateList(resourceId, null)
        } catch (e: NotFoundException) {
            MultiThemeLog.e(e.message.toString())
        }
        val states = Array(1) { IntArray(1) }
        return ColorStateList(states, intArrayOf(app.resources.getColor(resId, null)))
    }

    override fun getAssetsFilePath(fileName: String): String {
        return this.mStrategy?.let { String.format("%s/%s", it.themeName, fileName) } ?: fileName
    }
}