package ru.khozyainov.rddt.ui.exception

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navOptions
import ru.khozyainov.rddt.R
import ru.khozyainov.rddt.databinding.FragmentExceptionBinding
import ru.khozyainov.rddt.utils.ViewBindingFragment

class ExceptionFragment : ViewBindingFragment<FragmentExceptionBinding>(FragmentExceptionBinding::inflate) {

    private val args: ExceptionFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.retryButton.setOnClickListener {
            findNavController().navigate(
                args.sourceFragmentId,
                null,
                navOptions {
                    popUpTo(R.id.exceptionFragment) {
                        inclusive = true
                    }
                }
            )
        }
    }

    companion object {
        const val SOURCE_FRAGMENT_ID_KEY = "sourceFragmentId"
    }
}