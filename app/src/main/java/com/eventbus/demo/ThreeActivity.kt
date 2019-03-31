package com.eventbus.demo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.eventbus.xeventbus.XEventBus
import kotlinx.android.synthetic.main.activity_three.*

class ThreeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_three)


        sentMessage.setOnClickListener {
            Log.e("XLog", "==========线程名称=================${Thread.currentThread().name}")
            val userModel = UserModel()
            userModel.userName = "jaty"
            XEventBus.getDefault.post(userModel)
        }

    }
}
