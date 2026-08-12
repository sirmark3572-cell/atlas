package com.sirmark.goldcapsule

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.widget.TextView

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        GoldUpdateWorker.schedule(this)
        GoldUpdateWorker.refreshNow(this)

        setContentView(TextView(this).apply {
            text = getString(R.string.setup_message)
            setTextColor(Color.WHITE)
            textSize = 18f
            gravity = Gravity.CENTER
            setPadding(48, 48, 48, 48)
            setBackgroundColor(Color.rgb(12, 12, 14))
        })
    }
}
