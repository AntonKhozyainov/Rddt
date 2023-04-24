package ru.khozyainov.rddt.ui.exception

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.khozyainov.rddt.databinding.FragmentExceptionBinding
import ru.khozyainov.rddt.utils.ViewBindingFragment

class ExceptionFragment : ViewBindingFragment<FragmentExceptionBinding>(FragmentExceptionBinding::inflate) {

    //private val args: ExceptionFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.retryButton.setOnClickListener {
            //findNavController().setGraph(args.graphId)
            findNavController().popBackStack()
        }
    }
}