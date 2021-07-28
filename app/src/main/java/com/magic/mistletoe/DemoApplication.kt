package com.magic.mistletoe

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.magic.multi.theme.core.action.SkinLoadManager

/**
 * Created by mistletoe
 * on 7/28/21
 **/
class DemoApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        SkinLoadManager.getInstance().init(this)
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityPreCreated(activity: Activity, savedInstanceState: Bundle?) {
                super.onActivityPreCreated(activity, savedInstanceState)
                activity.layoutInflater.factory = SkinLoadManager.getInstance().multiThemeFactory
            }

            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {

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
    }
}