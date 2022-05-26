package com.example.hw9_intentservice

import android.app.IntentService
import android.content.Intent

private const val SERVICE_THREAD_NAME = "QUARTER_SERVICE_THREAD_NAME"
private const val TIMER_SECONDS_DELAY = 1000L
private const val TIMER_SECONDS = 15

class QuarterService : IntentService(SERVICE_THREAD_NAME) {

    private var isStopped = false

    override fun onHandleIntent(intent: Intent?) {
        val quarterIntent = Intent(this, MainActivity::class.java)

        for (second in 0..TIMER_SECONDS) {
            startActivity(
                quarterIntent.apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                    putExtra(MainActivity.QUARTER_INTENT_SERVICE_VALUE_KEY, second.toString())
                }
            )
            if (second != 0) {
                Thread.sleep(TIMER_SECONDS_DELAY)
            }
            if (second == TIMER_SECONDS) {
                startActivity(
                    quarterIntent.apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                        putExtra(MainActivity.QUARTER_INTENT_SERVICE_VALUE_KEY, 0)
                    }
                )
            }
        }
        if (!isStopped) {
            startService(Intent(this, CustomQuarterIntentService::class.java))
        } else {
            startActivity(
                quarterIntent.apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                    putExtra(MainActivity.QUARTER_INTENT_SERVICE_STOP_KEY, isStopped)
                }
            )
        }
    }

    override fun onDestroy() {
        isStopped = true
        super.onDestroy()
    }

}