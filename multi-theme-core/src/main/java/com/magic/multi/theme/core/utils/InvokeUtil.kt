package com.magic.multi.theme.core.utils

import android.content.Context
import android.util.AttributeSet


/**
 * Created by mistletoe
 * on 7/27/21
 **/
internal object InvokeUtil {
    /**
     * 通过反射创建带2个构造参数的View
     **/
    @Throws(Exception::class)
    fun createView(classPath: String?, context: Context,attributes:AttributeSet): Any? {
        var view: Any? = null
        classPath?.let {
            val clazz: Class<*>? = Class.forName(it)
            val con =  clazz?.getDeclaredConstructor(Context::class.java, AttributeSet::class.java)
            view = con?.newInstance(context, attributes)
        } 
        return view
    }
}