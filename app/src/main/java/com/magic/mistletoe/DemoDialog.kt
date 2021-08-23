package com.magic.mistletoe

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.TextView
import com.magic.mistletoe.utils.dp2px

/**
 * Created by mistletoe
 * on 2021/8/23
 **/
class DemoDialog(context: Context) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val window = window ?: return
        window.setBackgroundDrawableResource(android.R.color.transparent)
        window.decorView.setPadding(45.dp2px, 0, 45.dp2px, 0)
        val layoutParams = window.attributes
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        window.attributes = layoutParams
        val view = LayoutInflater.from(context).inflate(R.layout.view_dialog_comm,null)
        setContentView(view)
        view.findViewById<TextView>(R.id.tv_cancel).setOnClickListener {
            dismiss()
        }
        view.findViewById<TextView>(R.id.tv_confirm).setOnClickListener {
            dismiss()
        }
    }
}
