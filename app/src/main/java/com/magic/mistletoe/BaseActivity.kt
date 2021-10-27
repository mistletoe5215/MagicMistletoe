package com.magic.mistletoe

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.magic.multi.theme.core.factory.MultiThemeFactory

/**
 * Created by mistletoe
 * on 2021/10/18
 **/
abstract class BaseActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        layoutInflater.factory2 = MultiThemeFactory()
        super.onCreate(savedInstanceState)
    }
}