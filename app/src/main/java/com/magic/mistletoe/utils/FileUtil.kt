package com.magic.mistletoe.utils

import android.content.Context
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

/**
 * Created by mistletoe
 * on 2021/8/19
 **/
object FileUtil {
    @Suppress("SameParameterValue")
    fun copyAssetAndWrite(context: Context,fileName: String): Boolean {
        try {
            val cacheDir = context.cacheDir
            if (!cacheDir.exists()) {
                cacheDir.mkdirs()
            }
            val outFile = File(cacheDir, fileName)
            if (!outFile.exists()) {
                val res = outFile.createNewFile()
                if (!res) {
                    return false
                }
            } else {
                if (outFile.length() > 10) { //表示已经写入一次
                    return true
                }
            }
            val `is`: InputStream = context.assets.open(fileName)
            val fos = FileOutputStream(outFile)
            val buffer = ByteArray(1024)
            var byteCount: Int
            while (`is`.read(buffer).also { byteCount = it } != -1) {
                fos.write(buffer, 0, byteCount)
            }
            fos.flush()
            `is`.close()
            fos.close()
            return true
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return false
    }
}