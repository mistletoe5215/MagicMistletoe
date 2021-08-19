package com.magic.multi.theme.core.api

import android.app.Application
import androidx.annotation.WorkerThread
import androidx.appcompat.app.AppCompatActivity
import com.magic.multi.theme.core.annotation.CalledAfterSetThemeFactory
import com.magic.multi.theme.core.base.BaseAttr
import com.magic.multi.theme.core.strategy.IThemeLoadStrategy

/**
 * Created by mistletoe
 * on 7/27/21
 **/
interface IOperationHandler {
    /**
     * 初始化
     */
    fun init(app: Application)
    /**
     * 添加监听的页面
     */
    @CalledAfterSetThemeFactory
    fun bindPage(page:AppCompatActivity)
    /**
     * 根据主题包名称得到主题包路径
     * @param strategy 一个主题包加载策略
     * @return 释放是否成功
     */
    @WorkerThread
    fun loadThemeByStrategy(strategy: IThemeLoadStrategy, iLoadListener: ILoadListener? = null)
    /**
     * 应用主题
     */
    fun applyTheme()

    /**
     * 允许外部配置自定义View的换主题属性映射
     * @param attrMap 自定义属性映射
     */
    fun configCustomAttrs(attrMap: MutableMap<String, Class<out BaseAttr>>)


    /**
     * 清空当前缓存视图集
     */
    fun clean()
}