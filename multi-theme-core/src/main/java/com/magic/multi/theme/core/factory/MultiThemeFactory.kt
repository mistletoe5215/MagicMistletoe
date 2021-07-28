package com.magic.multi.theme.core.factory

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.RestrictTo
import com.magic.multi.theme.core.action.SkinLoadManager
import com.magic.multi.theme.core.api.IOperationHandler
import com.magic.multi.theme.core.base.BaseAttr
import com.magic.multi.theme.core.constants.SkinConfig
import com.magic.multi.theme.core.constants.SkinConfig.MULTI_THEME_TAG
import com.magic.multi.theme.core.impl.SkinView
import com.magic.multi.theme.core.utils.AttrConfig
import com.magic.multi.theme.core.utils.InvokeUtil

/**
 * 多主题layout factory
 * Created by mistletoe
 * on 7/27/21
 **/
class MultiThemeFactory : LayoutInflater.Factory {
    private val mSkinViews: MutableList<SkinView> = mutableListOf()
    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        var view: View? = null
        val skinEnable = attrs.getAttributeBooleanValue(
            SkinConfig.DEFAULT_SCHEMA_NAME,
            SkinConfig.DEFAULT_ATTR_NAME,
            false
        )
        if (skinEnable) {
            view = createView(context, name, attrs)
            view?.let { parseSkinAttr(context, attrs, it) }
        }
        return view
    }

    /**
     * 重新创建视图
     * @param context 当前上下文
     * @param name 视图控件名称
     * @param attrs 视图控件属性集合(key-value的set)
     * @return 重新创建后的视图对象
     */
    private fun createView(context: Context, name: String, attrs: AttributeSet): View? {
        var view: View?
        var mName = name
        try {
            if (-1 == name.indexOf('.')) {
                mName = "android.widget.$name"
            }
            view = InvokeUtil.createView(mName, context, attrs) as? View
            Log.i(MULTI_THEME_TAG, "$mName transfer succeed!")
        } catch (e: Exception) {
            for (i in 0 until attrs.attributeCount) {
                Log.e(
                    MULTI_THEME_TAG,
                    "${attrs.getAttributeName(i)}:${attrs.getAttributeValue(i)}".trimIndent()
                )
            }
            Log.e(MULTI_THEME_TAG, "$mName transfer failed!reason:" + e.message)
            view = null
        }
        return view
    }

    /**
     * 解析换肤视图的属性集合,将需要换肤的属性对应的第三方资源设置上去
     * @param context 当前上下文
     * @param attrs 换肤视图控件的属性集合(key-value的set)
     * @param view 视图对象
     */
    private fun parseSkinAttr(context: Context, attrs: AttributeSet, view: View) {
        val viewAttrs: MutableList<BaseAttr> = mutableListOf()
        for (i in 0 until attrs.attributeCount) {
            val attrName = attrs.getAttributeName(i)
            val attrValue = attrs.getAttributeValue(i)
            if (!AttrConfig.isSupportedAttr(attrName)) {
                continue
            }
            if (attrValue.startsWith("@")) {
                try {
                    val id = attrValue.substring(1).toInt()
                    val entryName = context.resources.getResourceEntryName(id)
                    val typeName = context.resources.getResourceTypeName(id)
                    val mSkinAttr = AttrConfig.get(attrName, id, entryName, typeName)
                    mSkinAttr?.let {
                        viewAttrs.add(it)
                    }
                } catch (e: Exception) {
                    Log.e(MULTI_THEME_TAG, e.message.toString())
                }
            }
        }
        if (viewAttrs.isNotEmpty()) {
            val skinView = SkinView()
            skinView.view = view
            skinView.attrs = viewAttrs
            mSkinViews.add(skinView)
            if (SkinLoadManager.getInstance().isExternalSkin) {
                skinView.apply()
            }
        }
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY)
    internal fun applyTheme() {
        for (skinView in mSkinViews) {
            if (null != skinView.view) {
                skinView.apply()
            }
        }
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY)
    internal fun clean() {
        if (mSkinViews.isEmpty()) {
            return
        }
        for (skinView in mSkinViews) {
            if (skinView.view == null) {
                continue
            }
            skinView.clean()
        }
    }
}