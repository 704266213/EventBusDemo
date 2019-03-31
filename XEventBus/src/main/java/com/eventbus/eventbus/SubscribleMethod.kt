package com.eventbus.xeventbus

import com.eventbus.xeventbus.annotation.ThreadMode
import java.lang.reflect.Method

class SubscribleMethod {

    //需要执行的方法
    lateinit var method: Method
    //线程切换
    lateinit var threadMode: ThreadMode
    //需要执行方法的参数的类型(即注解方法的参数类型)
    lateinit var methodParameterType: Class<*>

}