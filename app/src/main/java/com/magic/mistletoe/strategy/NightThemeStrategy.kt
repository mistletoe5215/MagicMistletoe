package com.magic.mistletoe.strategy

import android.content.Context
import com.magic.mistletoe.ThemeConstant
import com.magic.mistletoe.utils.FileUtil
import com.magic.multi.theme.core.strategy.IThemeLoadStrategy
import java.io.File

/**
 * Created by mistletoe
 * on 2021/8/19
 **/
object NightThemeStrategy:IThemeLoadStrategy {
    private const val fileName = "theme-pkg.zip"
    override val themeName: String
        get() = ThemeConstant.THEME_NIGHT

    override fun getOrGenerateThemePackage(context:Context): String? {
        val dataFile = File(context.cacheDir, fileName)
        if(!dataFile.exists()){
            FileUtil.copyAssetAndWrite(context,fileName)
        }
        return dataFile.absolutePath
    }
}