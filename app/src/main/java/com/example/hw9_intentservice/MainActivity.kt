package com.example.hw9_intentservice

import android.content.*
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.hw9_intentservice.databinding.ActivityMainBinding

private const val START_BUTTON_ENABLE_KEY = "START_BUTTON_ENABLE_KEY"
private const val STOP_BUTTON_ENABLE_KEY = "STOP_BUTTON_ENABLE_KEY"

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    companion object {
        const val ACTION_QUARTER = "ACTION_QUARTER"
        const val QUARTER_INTENT_SERVICE_VALUE_KEY = "QUARTER_INTENT_SERVICE_VALUE_KEY"
        const val QUARTER_CUSTOM_INTENT_SERVICE_VALUE_KEY =
            "QUARTER_CUSTOM_INTENT_SERVICE_VALUE_KEY"
        const val QUARTER_INTENT_SERVICE_STOP_KEY = "QUARTER_INTENT_SERVICE_STOP_KEY"
        const val QUARTER_CUSTOM_INTENT_SERVICE_STOP_KEY =
            "QUARTER_CUSTOM_INTENT_SERVICE_STOP_KEY"
    }

    private val quarterIntentService by lazy {
        Intent(this, QuarterService::class.java)
    }

    private val customQuarterIntentService by lazy {
        Intent(this, CustomQuarterIntentService::class.java)
    }

    private val quarterReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            checkInputIntent(intent)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.apply {
            putBoolean(
                START_BUTTON_ENABLE_KEY,
                binding.startTimerButton.isEnabled
            )
            putBoolean(
                STOP_BUTTON_ENABLE_KEY,
                binding.stopTimerButton.isEnabled
            )
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        binding.startTimerButton.isEnabled =
            savedInstanceState.getBoolean(START_BUTTON_ENABLE_KEY)
        binding.stopTimerButton.isEnabled =
            savedInstanceState.getBoolean(STOP_BUTTON_ENABLE_KEY)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        registerReceiver(quarterReceiver, IntentFilter(ACTION_QUARTER))

        binding.startTimerButton.setOnClickListener {
            startQuarterServices()
        }
        binding.stopTimerButton.setOnClickListener {
            stopQuarterServices()
        }
    }

    private fun startQuarterServices() {
        startService(quarterIntentService)
        binding.startTimerButton.isEnabled = false
        binding.stopTimerButton.isEnabled = true
    }

    private fun stopQuarterServices() {
        stopService(quarterIntentService)
        stopService(customQuarterIntentService)
        binding.stopTimerButton.isEnabled = false
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        checkInputIntent(intent)
    }

    private fun checkInputIntent(intent: Intent?) {
        if (intent?.extras?.containsKey(QUARTER_INTENT_SERVICE_VALUE_KEY) == true) {
            binding.secondsTextView.text =
                intent.getStringExtra(QUARTER_INTENT_SERVICE_VALUE_KEY)
        }

        if (intent?.extras?.containsKey(QUARTER_CUSTOM_INTENT_SERVICE_VALUE_KEY) == true) {
            binding.minutesTextView.text =
                intent.getStringExtra(QUARTER_CUSTOM_INTENT_SERVICE_VALUE_KEY)
        }

        if ((intent?.extras?.containsKey(QUARTER_INTENT_SERVICE_STOP_KEY) == true) ||
            (intent?.extras?.containsKey(QUARTER_CUSTOM_INTENT_SERVICE_STOP_KEY) == true)
        ) {
            binding.secondsTextView.text = null
            binding.minutesTextView.text = null
            binding.startTimerButton.isEnabled = true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(quarterReceiver)
    }
}