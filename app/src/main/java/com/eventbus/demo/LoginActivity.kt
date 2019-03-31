package com.eventbus.demo

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.eventbus.eventbus.annotation.Subscribe
import com.eventbus.xeventbus.XEventBus
import com.eventbus.xeventbus.annotation.ThreadMode
import kotlinx.android.synthetic.main.activity_main.*

class LoginActivity : AppCompatActivity() {

    private val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        goToActivity.setOnClickListener {
            val intent = Intent(this, ThreeActivity::class.java)
            startActivity(intent)
        }

        XEventBus.getDefault.register(this)


        handler.postDelayed(Runnable {
            XEventBus.getDefault.unregister(this)

        }, 1000 * 30)

    }


    @Subscribe(threadMode = ThreadMode.Async)
    fun onEventMain(userModel: UserModel) {
        Log.e("XLog", "==========线程名称=================${Thread.currentThread().name}")
        Log.e("XLog", "==============${userModel.userName}=========")
//        message.text = userModel.userName
    }

    override fun onDestroy() {
        super.onDestroy()
        XEventBus.getDefault.unregister(this)
    }

}
