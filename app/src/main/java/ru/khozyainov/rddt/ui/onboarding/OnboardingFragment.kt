package ru.khozyainov.rddt.ui.onboarding

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.khozyainov.rddt.R
import ru.khozyainov.rddt.databinding.FragmentOnboardingBinding
import ru.khozyainov.rddt.ui.exception.ExceptionFragment
import ru.khozyainov.rddt.ui.exception.ExceptionHandler
import ru.khozyainov.rddt.utils.ViewBindingFragment
import ru.khozyainov.rddt.utils.launchAndCollectLatest

class OnboardingFragment: ViewBindingFragment<FragmentOnboardingBinding>(FragmentOnboardingBinding::inflate),
    ExceptionHandler {

    private val viewModel by viewModel<OnboardingViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeState()
        setAdapter()
    }

    override fun navToExceptionFragment() {
        findNavController().navigate(
            R.id.exceptionFragment,
            bundleOf(ExceptionFragment.SOURCE_FRAGMENT_ID_KEY to R.id.onboardingFragment),
            navOptions {
                popUpTo(R.id.onboardingFragment) {
                    inclusive = true
                }
            }
        )
    }

    private fun observeState(){
        viewModel.uiState.launchAndCollectLatest(viewLifecycleOwner){ uiState ->
            when(uiState){
                is OnboardingState.Success -> {
//                    val intent = Intent(requireContext(), LauncherActivity::class.java)
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
//                    startActivity(intent)
                    findNavController().navigate(
                        OnboardingFragmentDirections.actionOnboardingFragmentToLauncherFragment()
                    )
                }
                is OnboardingState.Error -> {
                    navToExceptionFragment()
                    //TODO action.exception ???
                }
                is OnboardingState.Default -> {}
            }
        }
    }

    private fun setAdapter(){
        with(binding){

            viewPagerOnboarding.adapter = OnboardingAdapter(object : OnboardingAdapter.Listener{
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