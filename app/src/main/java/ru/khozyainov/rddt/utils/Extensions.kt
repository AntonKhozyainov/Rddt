package ru.khozyainov.rddt.utils

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat

fun String.withBearer() = "Bearer $this"

fun AppCompatActivity.setStatusBarTextColor() {
    val textColorIsBlack =
        when (resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_NO -> true
            Configuration.UI_MODE_NIGHT_YES -> false
            else -> true
        }
    WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars =
        textColorIsBlack
}

fun Context.getIntentNewClearTask(cls: Class<*>?): Intent = Intent(this, cls).apply {
    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
}


