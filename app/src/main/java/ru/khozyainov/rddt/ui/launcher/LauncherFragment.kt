package ru.khozyainov.rddt.ui.launcher

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.khozyainov.rddt.databinding.LogoBinding
import ru.khozyainov.rddt.ui.onboarding.OnboardingActivity
import ru.khozyainov.rddt.utils.ViewBindingFragment
import ru.khozyainov.rddt.utils.launchAndCollectLatest

class LauncherFragment : ViewBindingFragment<LogoBinding>(LogoBinding::inflate) {

    private val viewModel by viewModel<LauncherViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeState()
    }

    private fun observeState() {
        viewModel.uiState.launchAndCollectLatest(viewLifecycleOwner) { action ->
            runAction(action)
        }
    }

    private fun runAction(action: LauncherState) {
        when (action) {
            is LauncherState.NavigateToOnboardingAction -> {
                startActivity(Intent(requireContext(), OnboardingActivity::class.java))
                activity?.finish()
            }

            is LauncherState.NavigateToLoginActivityAction -> {
                Log.d("SomeTAG", "login")
            }

            is LauncherState.NavigateToMainActivityAction -> {
                Log.d("SomeTAG", "main")
            }

            is LauncherState.Error -> {
                findNavController().navigate(
                    LauncherFragmentDirections.actionLauncherFragmentToExceptionFragment()
                )
                //TODO action.exception ???
            }

            is LauncherState.Loading -> {}
        }
    }
}