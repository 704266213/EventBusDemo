package com.eventbus.xeventbus

import com.eventbus.eventbus.annotation.Subscribe
import java.lang.reflect.Method
import java.util.HashMap
import android.os.Looper
import android.os.Handler
import com.eventbus.xeventbus.annotation.ThreadMode
import java.util.concurrent.Executors

class XEventBus private constructor() {

    private val cacheMap = HashMap<Any, List<SubscribleMethod>>()
    private val executorService = Executors.newCachedThreadPool()
    private val handler = Handler(Looper.getMainLooper())

    companion object {
        val getDefault: XEventBus by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            XEventBus()
        }
    }

    fun register(clazzInstance: Any) {
        //查询缓存中是否已经注册了相应的方法
        val subscribleMethods = cacheMap[clazzInstance]
        //如果缓存中没有，则到相关的类中去查找并注册
        if (subscribleMethods == null) {
            val methods = findSubscribleMethods(clazzInstance)
            cacheMap[clazzInstance] = methods
        }
    }

    /*
     * 查找并注册注解相关的方法
     */
    private fun findSubscribleMethods(clazzInstance: Any): List<SubscribleMethod> {
        //获取当前类中的所有方法
        val declaredMethods = clazzInstance.javaClass.declaredMethods
        //用于存储所有的相关熟悉
        val subscribleMethods = ArrayList<SubscribleMethod>()
        declaredMethods.forEach {
            //获取对于注解的方法
            val annotationSubscribeMethod = it.getAnnotation(Subscribe::class.java)
            if (annotationSubscribeMethod != null) {
                //获取注解的方法的参数类型
                val parameterTypes = it.parameterTypes
                if (parameterTypes.size != 1)
                    throw  RuntimeException("XEventBus只能接收到一个参数")
                val subscribleMethod = SubscribleMethod()
                subscribleMethod.method = it
                subscribleMethod.methodParameterType = parameterTypes[0]
                subscribleMethod.threadMode = annotationSubscribeMethod.threadMode
                subscribleMethods.add(subscribleMethod)
            }
        }
        return subscribleMethods
    }


    fun unregister(clazzInstance: Any) {
        val subscribleMethods = cacheMap[clazzInstance]
        if (subscribleMethods != null) {
            cacheMap.remove(clazzInstance)
        }
    }

    /*
     * 发送消息
     */
    fun post(methodParameterTypeInstance: Any) {
        cacheMap.forEach { (key, value) ->
            value.forEach {
                //判断需要执行的方法的参数类型与缓存中的类型是否相同
                if (it.methodParameterType.isAssignableFrom(methodParameterTypeInstance.javaClass)) {
                    when (it.threadMode) {
                        ThreadMode.MainThread -> {
                            //判断是否在主线程中执行
                            if (Looper.myLooper() == Looper.getMainLooper()) {
                                invoke(key, it.method, methodParameterTypeInstance)
                            } else {
                                handler.post(Runnable {
                                    invoke(key, it.method, methodParameterTypeInstance)
                                })
                            }
                        }
                        ThreadMode.Async -> {
                            if (Looper.myLooper() == Looper.getMainLooper()) {
                                executorService.execute(Runnable {
                                    invoke(key, it.method, methodParameterTypeInstance)
                                })
                            } else {
                                invoke(key, it.method, methodParameterTypeInstance)

                            }
                        }
                    }
                }
            }
        }
    }


    private fun invoke(runObject: Any, method: Method, methodParameterTypeInstance: Any) {
        method.invoke(runObject, methodParameterTypeInstance)
    }


}