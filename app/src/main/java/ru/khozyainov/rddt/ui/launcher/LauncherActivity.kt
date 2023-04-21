package ru.khozyainov.rddt.ui.launcher

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.khozyainov.rddt.utils.launchAndCollectLatestForActivity

class LauncherActivity: AppCompatActivity() {

    private val viewModel by viewModel<LauncherViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.uiState.launchAndCollectLatestForActivity(lifecycle){ action ->
            when(action){
                is LauncherNavigationAction.NavigateToOnboardingAction -> {
                    Log.d("SomeTAG", "onb")
                }
                is LauncherNavigationAction.NavigateToLoginActivityAction -> {
                    Log.d("SomeTAG", "login")
                }
                is LauncherNavigationAction.NavigateToMainActivityAction -> {
                    Log.d("SomeTAG", "main")
                }
                is LauncherNavigationAction.Error -> {
                    Log.d("SomeTAG", action.exception.toString())
                }
                is LauncherNavigationAction.Loading -> {
                    Log.d("SomeTAG", "load")
                }
            }

            coroutineScope {
                delay(5000)
                viewModel.refresh()
            }

        }

    }
}