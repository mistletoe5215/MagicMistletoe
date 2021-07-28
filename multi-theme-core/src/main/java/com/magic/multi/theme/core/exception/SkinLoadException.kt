package com.magic.multi.theme.core.exception

/**
 * Created by mistletoe
 * on 7/27/21
 **/
open class SkinLoadException(message: String?) : Exception(message) {
    companion object {
        const val NULL_SKIN_PATH_EXCEPTION = "failed, get null file path"
        const val SKIN_FILE_NOT_EXISTS = "failed, file path don't exist"
        const val SKIN_GET_NULL_RESOURCES = "failed,generated resource is null"
    }
}