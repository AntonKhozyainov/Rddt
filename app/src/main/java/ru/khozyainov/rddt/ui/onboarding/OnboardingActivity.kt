package ru.khozyainov.rddt.ui.onboarding

import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.khozyainov.rddt.databinding.ActivityOnboardingBinding
import ru.khozyainov.rddt.ui.launcher.LauncherActivity
import ru.khozyainov.rddt.utils.ViewBindingActivity
import ru.khozyainov.rddt.utils.getIntentNewClearTask
import ru.khozyainov.rddt.utils.launchAndCollectLatestForActivity

class OnboardingActivity :
    ViewBindingActivity<ActivityOnboardingBinding>(ActivityOnboardingBinding::inflate) {

    private val viewModel by viewModel<OnboardingViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        observeState()
        setAdapter()
    }

    override fun refreshState() {
        dismissExceptionDialogFragment()
        viewModel.refreshState()
    }

    private fun observeState() {
        viewModel.uiState.launchAndCollectLatestForActivity(lifecycle) { uiState ->
            when (uiState) {
                is OnboardingState.Success -> {
                    startActivity(this@OnboardingActivity.getIntentNewClearTask(LauncherActivity::class.java))
                }

                is OnboardingState.Error -> {
                    showExceptionDialogFragment()
                }

                is OnboardingState.Default -> {}
            }
        }
    }

    private fun setAdapter() {
        with(binding) {

            viewPagerOnboarding.adapter = OnboardingAdapter(object : OnboardingAdapter.Listener {
                override fun showNextPage() {
                    viewPagerOnboarding.setCurrentItem(viewPagerOnboarding.currentItem + 1, true)
                }

                override fun showPrevPage() {
                    viewPagerOnboarding.setCurrentItem(viewPagerOnboarding.currentItem - 1, true)
                }

                override fun onboardingComplete() {
                    viewModel.onboardingCompleted()
                }

            })

            dotsIndicator.attachTo(viewPagerOnboarding)
        }
    }

}