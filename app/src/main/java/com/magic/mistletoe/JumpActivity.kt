package com.magic.mistletoe

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

/**
 * Created by mistletoe
 * on 2021/8/18
 **/
class JumpActivity: AppCompatActivity() {
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