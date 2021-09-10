package com.magic.multi.theme.core.strategy

/**
 * Created by mistletoe
 * on 2021/9/10
 **/
object ThemeUpdateManager {
    private var mDelegateMap: MutableMap<IThemeLoadStrategy, IThemeUpdateChecker> = mutableMapOf()

    @Synchronized
    fun registerUpdateCheckDelegates(vararg strategyDelegates: Pair<IThemeLoadStrategy, IThemeUpdateChecker>) {
        strategyDelegates.forEach {
            val (strategy, checker) = it
            mDelegateMap[strategy] = checker
        }
    }

    @Synchronized
    fun getChecker(strategy: IThemeLoadStrategy): IThemeUpdateChecker? {
        return mDelegateMap[strategy]
    }
}