package ru.khozyainov.rddt.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationResponse
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.khozyainov.rddt.databinding.ActivityLoginBinding
import ru.khozyainov.rddt.ui.launcher.LauncherActivity
import ru.khozyainov.rddt.utils.ViewBindingActivity
import ru.khozyainov.rddt.utils.getIntentNewClearTask
import ru.khozyainov.rddt.utils.launchAndCollectLatestForActivity

class LoginActivity : ViewBindingActivity<ActivityLoginBinding>(ActivityLoginBinding::inflate) {

    private val viewModel: LoginViewModel by viewModel()

    private val getAuthResponse =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
            val dataIntent = activityResult.data ?: return@registerForActivityResult
            handleAuthResponseIntent(dataIntent)
        }

    override fun refreshState() {
        dismissExceptionDialogFragment()
        viewModel.refreshState()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        changeColorStatusBar(attrColor = com.google.android.material.R.attr.colorPrimary)

        observeState()
        binding.loginButton.setOnClickListener {
            viewModel.getLoginPageIntent()
        }
    }

    override fun onDestroy() {
        changeColorStatusBar(attrColor = com.google.android.material.R.attr.colorSurface)
        super.onDestroy()
    }

    private fun observeState() {
        viewModel.uiState.launchAndCollectLatestForActivity(lifecycle) { uiState ->
            when (uiState) {
                is LoginState.Success -> {
                    uiState.intent?.let {
                        viewModel.setLoadingState()
                        getAuthResponse.launch(it)
                    }
                }

                is LoginState.Error -> {
                    showExceptionDialogFragment()
                }

                is LoginState.NavigateToLaunchAction -> {
                    startActivity(this@LoginActivity.getIntentNewClearTask(LauncherActivity::class.java))
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
        this.theme.resolveAttribute(
            attrColor, typedValue, true
        )
        this.window.statusBarColor = ContextCompat.getColor(
            this,
            typedValue.resourceId
        )
    }
}