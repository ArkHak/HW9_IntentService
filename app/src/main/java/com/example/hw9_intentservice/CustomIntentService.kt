package com.example.hw9_intentservice

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.HandlerThread
import android.os.IBinder

abstract class CustomIntentService(threadName: String) : Service() {
    private val serviceThread: HandlerThread = HandlerThread(threadName)
    protected var isStopped = false

    override fun onCreate() {
        super.onCreate()
        serviceThread.start()
    }

    override fun onBind(p0: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Handler(serviceThread.looper).post {
            isStopped = false
            onHandleIntent(intent)
        }
        Handler(serviceThread.looper).post {
            stopSelf()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    abstract fun onHandleIntent(intent: Intent?)

    override fun onDestroy() {
        isStopped = true
        super.onDestroy()
    }
}