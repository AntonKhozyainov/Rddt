package ru.khozyainov.rddt.utils

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.util.TypedValue
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
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

fun Activity.changeColorStatusBar(attrColor: Int) {
    val typedValue = TypedValue()
    this.theme.resolveAttribute(
        attrColor, typedValue, true
    )
    this.window.statusBarColor = ContextCompat.getColor(
        this,
        typedValue.resourceId
    )
}


