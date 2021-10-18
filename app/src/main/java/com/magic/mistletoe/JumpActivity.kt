package com.magic.mistletoe

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import com.magic.multi.theme.core.factory.MultiThemeFactory

/**
 * Created by mistletoe
 * on 2021/8/18
 **/
class JumpActivity: BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        findViewById<TextView>(R.id.jump_setting).setOnClickListener {
            Intent(this,SettingActivity::class.java).apply {
                startActivity(this)
            }
        }
    }

}