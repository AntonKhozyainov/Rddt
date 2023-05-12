//package ru.khozyainov.rddt.ui.launcher
//
//import android.content.Intent
//import android.os.Bundle
//import android.view.View
//import androidx.core.os.bundleOf
//import androidx.navigation.fragment.findNavController
//import androidx.navigation.navOptions
//import org.koin.androidx.viewmodel.ext.android.viewModel
//import ru.khozyainov.rddt.R
//import ru.khozyainov.rddt.databinding.LogoBinding
//import ru.khozyainov.rddt.ui.MainActivity
//import ru.khozyainov.rddt.ui.exception.ExceptionFragment.Companion.SOURCE_FRAGMENT_ID_KEY
//import ru.khozyainov.rddt.ui.exception.ExceptionHandler
//import ru.khozyainov.rddt.utils.ViewBindingFragment
//import ru.khozyainov.rddt.utils.launchAndCollectLatest
//
//class LauncherFragment : ViewBindingFragment<LogoBinding>(LogoBinding::inflate), ExceptionHandler {
//
//    private val viewModel by viewModel<LauncherViewModel>()
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        observeState()
//    }
//
//    override fun navToExceptionFragment() {
//        findNavController().navigate(
//            R.id.exceptionFragment,
//            bundleOf(SOURCE_FRAGMENT_ID_KEY to R.id.launcherFragment),
//            navOptions {
//                popUpTo(R.id.launcherFragment) {
//                    inclusive = true
//                }
//            }
//        )
//    }
//
//    private fun observeState() {
//        viewModel.uiState.launchAndCollectLatest(viewLifecycleOwner) { action ->
//            runAction(action)
//        }
//    }
//
//    private fun runAction(action: LauncherState) {
//        when (action) {
//            is LauncherState.NavigateToOnboardingAction -> {
//                findNavController().navigate(
//                    LauncherFragmentDirections.actionLauncherFragmentToOnboardingFragment()
//                )
//            }
//
//            is LauncherState.NavigateToLoginAction -> {
//                findNavController().navigate(
//                    LauncherFragmentDirections.actionLauncherFragmentToLoginFragment()
//                )
//            }
//
//            is LauncherState.NavigateToMainActivityAction -> {
//                val intent = Intent(requireContext(), MainActivity::class.java)
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
//                startActivity(intent)
//            }
//
//            is LauncherState.Error -> {
//                navToExceptionFragment()
//            }
//
//            is LauncherState.Loading -> {}
//        }
//    }
//
//}