package com.magic.multi.theme.core.api

import com.magic.multi.theme.core.exception.SkinLoadException

/**
 * 资源加载监听
 * Created by mistletoe
 * on 7/23/21
 **/
interface ILoadListener {
    /**
     * 开始加载回调
     */
    fun onStart(){}
    /**
     * 加载成功回调
     */
    fun onSuccess(){}
    /**
     * 加载失败回调
     */
    fun onFailed(e:SkinLoadException){}
}