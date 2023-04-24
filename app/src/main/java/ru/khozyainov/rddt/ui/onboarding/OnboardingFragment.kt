package ru.khozyainov.rddt.ui.onboarding

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.khozyainov.rddt.databinding.FragmentOnboardingBinding
import ru.khozyainov.rddt.ui.launcher.LauncherActivity
import ru.khozyainov.rddt.utils.ViewBindingFragment
import ru.khozyainov.rddt.utils.launchAndCollectLatest

class OnboardingFragment: ViewBindingFragment<FragmentOnboardingBinding>(FragmentOnboardingBinding::inflate) {

    private val viewModel by viewModel<OnboardingViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeState()
        setAdapter()
    }

    private fun observeState(){
        viewModel.uiState.launchAndCollectLatest(viewLifecycleOwner){ uiState ->
            when(uiState){
                is OnboardingState.Success -> {
                    startActivity(Intent(requireContext(), LauncherActivity::class.java))
                    activity?.finish()
                }
                is OnboardingState.Error -> {
                    findNavController().navigate(
                        OnboardingFragmentDirections.actionOnboardingFragmentToExceptionFragment2()
                    )
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