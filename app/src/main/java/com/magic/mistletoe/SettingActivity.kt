package com.magic.mistletoe

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.magic.mistletoe.strategy.NightThemeStrategy
import com.magic.mistletoe.utils.SPUtils
import com.magic.multi.theme.core.action.SkinLoadManager
import com.magic.multi.theme.core.api.ILoadListener
import com.magic.multi.theme.core.exception.SkinLoadException

/**
 * Created by mistletoe
 * on 2021/8/19
 **/
class SettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        findViewById<TextView>(R.id.change_skin_night).setOnClickListener {
            SkinLoadManager.getInstance()
                .loadThemeByStrategy(NightThemeStrategy, object : ILoadListener {
                    override fun onSuccess() {
                        SkinLoadManager.getInstance().applyTheme()
                        SPUtils.getInstance(ThemeConstant.THEME_TABLE)
                            ?.put(ThemeConstant.THEME_KEY, ThemeConstant.THEME_NIGHT)
                    }

                    override fun onFailed(e: SkinLoadException) {
                        Log.i("Mistletoe", "apply night theme failed")
                    }
                })
        }
        findViewById<TextView>(R.id.change_skin_day).setOnClickListener {
            SkinLoadManager.getInstance().restoreDefaultTheme()
            SPUtils.getInstance(ThemeConstant.THEME_TABLE)
                ?.put(ThemeConstant.THEME_KEY, ThemeConstant.THEME_DAY)
        }

        findViewById<TextView>(R.id.change_skin_auto).setOnClickListener{
            when(resources?.configuration?.uiMode!! and Configuration.UI_MODE_NIGHT_MASK){
                Configuration.UI_MODE_NIGHT_YES ->{
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
                else ->{
                    SkinLoadManager.getInstance().loadThemeByStrategy(NightThemeStrategy)
                    SkinLoadManager.getInstance().restoreDefaultTheme()
                }
            }
            SPUtils.getInstance(ThemeConstant.THEME_TABLE)
                ?.put(ThemeConstant.THEME_KEY, ThemeConstant.THEME_AUTO)
        }

    }
}