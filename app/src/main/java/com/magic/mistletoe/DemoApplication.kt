package com.magic.mistletoe

import android.app.Activity
import android.app.Application
import android.content.res.Configuration
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.magic.mistletoe.strategy.NightThemeStrategy
import com.magic.mistletoe.utils.SPUtils
import com.magic.multi.theme.core.action.SkinLoadManager
import com.magic.multi.theme.core.api.ILoadListener
import com.magic.multi.theme.core.base.BaseAttr
import com.magic.multi.theme.core.exception.SkinLoadException

/**
 * Created by mistletoe
 * on 7/28/21
 **/
class DemoApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        SkinLoadManager.getInstance().init(this)
        //增加一个设置前景图片属性
        val configMap = mutableMapOf<String, Class<out BaseAttr>>().apply {
            put("foreground", ImageForegroundAttr::class.java)
            put(ImageViewAlphaAttr.IMAGE_VIEW_ALPHA, ImageViewAlphaAttr::class.java)
        }
        SkinLoadManager.getInstance().configCustomAttrs(configMap)
        SkinLoadManager.getInstance()
            .preLoadThemeByStrategy(NightThemeStrategy, object : ILoadListener {
                override fun onSuccess() {
                    Log.i("Mistletoe", "preLoad Night Theme Succeed")
                }

                override fun onFailed(e: SkinLoadException) {
                    Log.i("Mistletoe", "preLoad Night Theme Failed")
                }
            })
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityPreCreated(activity: Activity, savedInstanceState: Bundle?) {
                super.onActivityPreCreated(activity, savedInstanceState)
            }

            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                (activity as? AppCompatActivity)?.let {
                    SkinLoadManager.getInstance().bindPage(it)
                }
            }

            override fun onActivityStarted(activity: Activity) {

            }

            override fun onActivityResumed(activity: Activity) {

            }

            override fun onActivityPaused(activity: Activity) {

            }

            override fun onActivityStopped(activity: Activity) {

            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

            }

            override fun onActivityDestroyed(activity: Activity) {

            }

        })
        when (SPUtils.getInstance(ThemeConstant.THEME_TABLE)?.getString(ThemeConstant.THEME_KEY)) {
            ThemeConstant.THEME_NIGHT -> {
                SkinLoadManager.getInstance()
                    .loadThemeByStrategy(NightThemeStrategy, object : ILoadListener {
                        override fun onSuccess() {
                            SkinLoadManager.getInstance().applyTheme()
                        }

                        override fun onFailed(e: SkinLoadException) {
                            Log.i("Mistletoe", "apply night theme failed")
                        }
                    })
            }
            ThemeConstant.THEME_AUTO -> {
                when (resources?.configuration?.uiMode!! and Configuration.UI_MODE_NIGHT_MASK) {
                    UI_MODE_NIGHT_YES -> {
                        SkinLoadManager.getInstance()
                            .loadThemeByStrategy(NightThemeStrategy, object : ILoadListener {
                                override fun onSuccess() {
                                    SkinLoadManager.getInstance().applyTheme()
                                }

                                override fun onFailed(e: SkinLoadException) {
                                    Log.i("Mistletoe", "apply night theme failed")
                                }
                            })
                    }
                    else -> {
                        SkinLoadManager.getInstance().restoreDefaultTheme()
                    }
                }
            }
            else -> {
                SkinLoadManager.getInstance().restoreDefaultTheme()
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        when (newConfig.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_NO -> {
                SkinLoadManager.getInstance().restoreDefaultTheme()
            }
            UI_MODE_NIGHT_YES -> {
                SkinLoadManager.getInstance()
                    .loadThemeByStrategy(NightThemeStrategy, object : ILoadListener {
                        override fun onSuccess() {
                            SkinLoadManager.getInstance().applyTheme()
                        }

                        override fun onFailed(e: SkinLoadException) {
                            Log.i("Mistletoe", "apply night theme failed")
                        }
                    })
            }
        }
    }

}