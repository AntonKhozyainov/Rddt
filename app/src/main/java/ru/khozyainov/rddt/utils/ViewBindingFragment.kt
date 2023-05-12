package ru.khozyainov.rddt.utils

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import ru.khozyainov.rddt.ui.exception.AppCompatActivityWithExceptionDialog

abstract class ViewBindingFragment<T : ViewBinding>(
    private val inflateBinding: (
        inflater: LayoutInflater,
        root: ViewGroup?,
        attachToRoot: Boolean
    ) -> T
) : Fragment() {

    private var _binding: T? = null

    protected val binding: T
        get() = _binding!!

    final override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = inflateBinding.invoke(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}

abstract class ViewBindingActivity<T : ViewBinding>(
    private val inflateBinding: (
        inflater: LayoutInflater
    ) -> T
) : AppCompatActivityWithExceptionDialog() {

    private var _binding: T? = null

    protected val binding: T
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = inflateBinding.invoke(layoutInflater)
        setContentView(binding.root)
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}