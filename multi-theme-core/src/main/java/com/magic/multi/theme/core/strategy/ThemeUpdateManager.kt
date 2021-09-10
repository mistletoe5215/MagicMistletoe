package com.magic.multi.theme.core.strategy

/**
 * Created by mistletoe
 * on 2021/9/10
 **/
object ThemeUpdateManager {
    private var mCheckerMap: MutableMap<IThemeLoadStrategy, IThemeUpdateChecker> = mutableMapOf()

    @Synchronized
    fun registerUpdateCheckers(vararg strategyCheckers: Pair<IThemeLoadStrategy, IThemeUpdateChecker>) {
        strategyCheckers.forEach {
            val (strategy, checker) = it
            mCheckerMap[strategy] = checker
        }
    }

    @Synchronized
    fun getChecker(strategy: IThemeLoadStrategy): IThemeUpdateChecker? {
        return mCheckerMap[strategy]
    }
}