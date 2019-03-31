package com.eventbus.demo

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.eventbus.eventbus.annotation.Subscribe
import com.eventbus.xeventbus.XEventBus
import com.eventbus.xeventbus.annotation.ThreadMode
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        XEventBus.getDefault.register(this)

        goToActivity.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }


    @Subscribe(threadMode = ThreadMode.MainThread)
    fun onEventMain(userModel: UserModel) {
        Log.e("XLog", "==========线程名称=================${Thread.currentThread().name}")
        Log.e("XLog", "==============${userModel.userName}=========")
        message.text = userModel.userName
    }

}
