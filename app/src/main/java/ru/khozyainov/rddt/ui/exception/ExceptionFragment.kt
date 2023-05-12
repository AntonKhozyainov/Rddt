package ru.khozyainov.rddt.ui.exception

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navOptions
import ru.khozyainov.rddt.R
import ru.khozyainov.rddt.databinding.FragmentExceptionBinding
import ru.khozyainov.rddt.ui.launcher.LauncherActivity
import ru.khozyainov.rddt.utils.ViewBindingFragment
import ru.khozyainov.rddt.utils.getIntentNewClearTask

class ExceptionFragment : ViewBindingFragment<FragmentExceptionBinding>(FragmentExceptionBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.retryButton.setOnClickListener {
//            setFragmentResult(
//                Activity.RESULT_OK,
//                //RESULT_KEY,
//                bundleOf()
//            )
            activity?.apply {
                setResult(Activity.RESULT_OK)
                finish()
                //startActivity(requireContext().getIntentNewClearTask(LauncherActivity::class.java))
            }

//            val intent = Intent(requireContext(), LauncherActivity::class.java).apply {
//                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
//            }
//            startActivity(intent)
//           // activity?.finish()
//         startActivity(requireContext().getIntentNewClearTask(LauncherActivity::class.java))
//            findNavController().navigate(
//                args.sourceFragmentId,
//                null,
//                navOptions { popUpTo(R.id.exceptionFragment) { inclusive = true } }
//            )
        }
    }

    companion object {
        const val SOURCE_FRAGMENT_ID_KEY = "sourceFragmentId"
        const val RESULT_KEY = "RESULT_KEY"
    }
}