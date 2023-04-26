package ru.khozyainov.rddt.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationResponse
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.khozyainov.rddt.R
import ru.khozyainov.rddt.databinding.FragmentLoginBinding
import ru.khozyainov.rddt.ui.exception.ExceptionFragment.Companion.SOURCE_FRAGMENT_ID_KEY
import ru.khozyainov.rddt.ui.exception.ExceptionHandler
import ru.khozyainov.rddt.utils.ViewBindingFragment
import ru.khozyainov.rddt.utils.launchAndCollectLatest

class LoginFragment : ViewBindingFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate),
    ExceptionHandler {

    private val viewModel: LoginViewModel by viewModel()

    private val getAuthResponse =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
            val dataIntent = activityResult.data ?: return@registerForActivityResult
            handleAuthResponseIntent(dataIntent)
        }

    override fun navToExceptionFragment() {
        findNavController().navigate(
            R.id.loginFragment,
            bundleOf(SOURCE_FRAGMENT_ID_KEY to R.id.launcherFragment),
            navOptions {
                popUpTo(R.id.loginFragment) {
                    inclusive = true
                }
            }
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        changeColorStatusBar(
            attrColor = com.google.android.material.R.attr.colorPrimary
        )
        observeState()
        binding.loginButton.setOnClickListener {
            viewModel.getLoginPageIntent()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        changeColorStatusBar(
            attrColor = com.google.android.material.R.attr.colorSurface
        )
    }

    private fun observeState() {
        viewModel.uiState.launchAndCollectLatest(viewLifecycleOwner) { uiState ->
            when (uiState) {
                is LoginState.Success -> {
                    uiState.intent?.let {
                        viewModel.setLoadingState()
                        getAuthResponse.launch(it)
                    }
                }

                is LoginState.Error -> {
                    navToExceptionFragment()
                    //todo action.exception ???
                }

                is LoginState.NavigateToLaunchAction -> {
                    findNavController().navigate(
                        LoginFragmentDirections.actionLoginFragmentToLauncherFragment()
                    )
                }

                is LoginState.Loading -> {
                    binding.loginButton.isEnabled = false
                    binding.loginProgressBar.isVisible = true
                }

                is LoginState.Default -> {
                    binding.loginButton.isEnabled = true
                    binding.loginProgressBar.isVisible = false
                }
            }
        }
    }

    private fun handleAuthResponseIntent(intent: Intent) {
        val exception = AuthorizationException.fromIntent(intent)
        val tokenExchangeRequest = AuthorizationResponse.fromIntent(intent)
            ?.createTokenExchangeRequest()
        when {
            exception != null -> viewModel.onAuthFailed(exception)
            tokenExchangeRequest != null ->
                viewModel.getTokenByRequest(tokenExchangeRequest)
        }
    }

    private fun changeColorStatusBar(attrColor: Int) {
        val typedValue = TypedValue()
        requireContext().theme.resolveAttribute(
            attrColor, typedValue, true
        )
        requireActivity().window.statusBarColor = ContextCompat.getColor(
            requireContext(),
            typedValue.resourceId
        )
    }
}