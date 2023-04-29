package ru.khozyainov.rddt.ui.feed

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import ru.khozyainov.rddt.R
import ru.khozyainov.rddt.databinding.FragmentFeedBinding
import ru.khozyainov.rddt.ui.exception.ExceptionFragment
import ru.khozyainov.rddt.ui.exception.ExceptionHandler
import ru.khozyainov.rddt.utils.ViewBindingFragment

class FeedFragment: ViewBindingFragment<FragmentFeedBinding>(FragmentFeedBinding::inflate), ExceptionHandler {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun navToExceptionFragment() {
        findNavController().navigate(
            R.id.exceptionFragment,
            bundleOf(ExceptionFragment.SOURCE_FRAGMENT_ID_KEY to R.id.feedFragment),
            navOptions {
                popUpTo(R.id.feedFragment) {
                    inclusive = true
                }
            }
        )
    }
}