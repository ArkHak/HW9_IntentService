package com.example.hw9_intentservice

import android.content.Intent

private const val CUSTOM_SERVICE_THREAD_NAME = "CUSTOM_SERVICE_THREAD_NAME"
private const val TIMER_SECONDS = 15
private const val STOPPED_TIMER_VALUE = 0
private const val TIMER_SECONDS_DELAY = 1000L

class CustomQuarterIntentService : CustomIntentService(CUSTOM_SERVICE_THREAD_NAME) {

    private val quarterCustomIntent = Intent(MainActivity.ACTION_QUARTER)

    override fun onHandleIntent(intent: Intent?) {
        for (second in 0..TIMER_SECONDS) {
            sendBroadcast(
                quarterCustomIntent.putExtra(
                    MainActivity.QUARTER_CUSTOM_INTENT_SERVICE_VALUE_KEY,
                    second.toString()
                )
            )

            if (second != 0) {
                Thread.sleep(TIMER_SECONDS_DELAY)
            }

            if (second == TIMER_SECONDS) {
                sendBroadcast(
                    quarterCustomIntent
                        .putExtra(
                            MainActivity.QUARTER_CUSTOM_INTENT_SERVICE_VALUE_KEY,
                            STOPPED_TIMER_VALUE
                        )
                )
            }
        }

        if (!isStopped) {
            startService(Intent(this, QuarterService::class.java))
        } else {
            sendBroadcast(
                quarterCustomIntent
                    .putExtra(
                        MainActivity.QUARTER_CUSTOM_INTENT_SERVICE_STOP_KEY,
                        true
                    )
            )
        }
    }
}