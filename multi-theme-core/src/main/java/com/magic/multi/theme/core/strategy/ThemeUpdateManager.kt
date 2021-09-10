package com.magic.multi.theme.core.strategy

import androidx.annotation.StringRes

/**
 * Created by mistletoe
 * on 2021/9/10
 **/
object ThemeUpdateManager {
    private var mCheckerMap: MutableMap<String, IThemeUpdateChecker> = mutableMapOf()

    @Synchronized
    fun registerUpdateCheckers(vararg strategyCheckers: Pair<String, IThemeUpdateChecker>) {
        strategyCheckers.forEach {
            val (themeName, checker) = it
            mCheckerMap[themeName] = checker
        }
    }

    @Synchronized
    fun getChecker(themeName: String): IThemeUpdateChecker? {
        return mCheckerMap[themeName]
    }
}