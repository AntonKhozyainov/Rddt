package ru.khozyainov.rddt.ui.launcher

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import ru.khozyainov.rddt.R

class LauncherActivity : AppCompatActivity(R.layout.activity_launcher) {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
    }
}