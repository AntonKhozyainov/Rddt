package ru.khozyainov.rddt.ui.launcher

import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.khozyainov.rddt.ui.MainActivity
import ru.khozyainov.rddt.ui.exception.AppCompatActivityWithExceptionDialog
import ru.khozyainov.rddt.ui.login.LoginActivity
import ru.khozyainov.rddt.ui.onboarding.OnboardingActivity
import ru.khozyainov.rddt.utils.getIntentNewClearTask
import ru.khozyainov.rddt.utils.launchAndCollectLatestForActivity

class LauncherActivity : AppCompatActivityWithExceptionDialog() {

    private val viewModel by viewModel<LauncherViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().setKeepOnScreenCondition { true }
        super.onCreate(savedInstanceState)
        observeState()
    }

    override fun refreshState() {
        dismissExceptionDialogFragment()
        viewModel.refresh()
    }

    private fun observeState() {
        viewModel.uiState.launchAndCollectLatestForActivity(lifecycle) { action ->
            runAction(action)
        }
    }

    private fun runAction(action: LauncherState) {
        when (action) {
            is LauncherState.NavigateToLoginAction -> {
                startActivity(this.getIntentNewClearTask(LoginActivity::class.java))
            }

            is LauncherState.NavigateToOnboardingAction -> {
                startActivity(this.getIntentNewClearTask(OnboardingActivity::class.java))
            }

            is LauncherState.NavigateToMainActivityAction -> {
                startActivity(this.getIntentNewClearTask(MainActivity::class.java))
            }

            is LauncherState.Error -> {
                showExceptionDialogFragment()
            }

            is LauncherState.Default -> {}
        }
    }

}