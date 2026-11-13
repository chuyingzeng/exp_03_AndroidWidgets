package com.example.exp_03_androidwidgets

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.core.view.WindowCompat

abstract class LabActivity(private val showUp: Boolean = true) : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actionBar?.setDisplayHomeAsUpEnabled(showUp)
    }

    @Suppress("DEPRECATION")
    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) {
            WindowCompat.getInsetsController(window, window.decorView)
                .isAppearanceLightNavigationBars = true
            findViewById<View>(android.R.id.content).setOnApplyWindowInsetsListener { view, insets ->
                // The framework ActionBar adds its height to the content's system window insets.
                view.setPadding(
                    insets.systemWindowInsetLeft,
                    insets.systemWindowInsetTop,
                    insets.systemWindowInsetRight,
                    insets.systemWindowInsetBottom
                )
                insets
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
