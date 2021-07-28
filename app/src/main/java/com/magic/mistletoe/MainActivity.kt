package com.magic.mistletoe

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.arch.core.executor.ArchTaskExecutor
import com.magic.multi.theme.core.action.SkinLoadManager
import com.magic.multi.theme.core.api.ILoadListener
import com.magic.multi.theme.core.exception.SkinLoadException
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initClick()
    }

    private var toggle: Boolean = false
    private val fileName = "theme-pkg.zip"

    @SuppressLint("RestrictedApi")
    private fun initClick() {
        findViewById<TextView>(R.id.copy).setOnClickListener {
            ArchTaskExecutor.getIOThreadExecutor().execute {
                copyAssetAndWrite(fileName)
                ArchTaskExecutor.getMainThreadExecutor().execute {
                    Toast.makeText(this, "成功", Toast.LENGTH_SHORT).show()
                }
            }
        }
        findViewById<TextView>(R.id.change_skin).setOnClickListener {
            if (!toggle) {
                val dataFile = File(cacheDir, fileName)
                SkinLoadManager.getInstance()
                    .loadSkin(dataFile.absolutePath, object : ILoadListener {
                        override fun onStart() {
                            Log.i("Mistletoe", "onStart")
                        }

                        override fun onSuccess() {
                            Log.i("Mistletoe", "onSuccess")
                        }

                        override fun onFailed(e: SkinLoadException) {
                            Log.e("Mistletoe", "onFailed:${e.message}")
                        }

                    })
            } else {
                SkinLoadManager.getInstance().restoreDefaultTheme()
            }
            toggle = !toggle
        }
    }

    @Suppress("SameParameterValue")
    private fun copyAssetAndWrite(fileName: String): Boolean {
        try {
            val cacheDir = cacheDir
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
            val `is`: InputStream = assets.open(fileName)
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