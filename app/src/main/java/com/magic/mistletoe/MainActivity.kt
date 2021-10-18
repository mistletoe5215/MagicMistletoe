package com.magic.mistletoe

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView


class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<TextView>(R.id.jump).setOnClickListener {
            Intent(this,JumpActivity::class.java).apply {
                startActivity(this)
            }
        }
        findViewById<TextView>(R.id.open_dialog).setOnClickListener {
            DemoDialog(this).show()
        }
    }


    override fun onNightModeChanged(mode: Int) {
        super.onNightModeChanged(mode)
        Log.i("Mis","onNightModeChanged")
    }
}