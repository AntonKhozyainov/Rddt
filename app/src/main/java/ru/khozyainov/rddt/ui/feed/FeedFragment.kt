package ru.khozyainov.rddt.ui.feed

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat.getDrawable
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.khozyainov.rddt.R
import ru.khozyainov.rddt.databinding.FragmentFeedBinding
import ru.khozyainov.rddt.model.UiPostSortType
import ru.khozyainov.rddt.ui.exception.ExceptionFragmentHandler
import ru.khozyainov.rddt.utils.ViewBindingFragment
import ru.khozyainov.rddt.utils.changeColorStatusBar
import ru.khozyainov.rddt.utils.launchAndCollectLatest

class FeedFragment : ViewBindingFragment<FragmentFeedBinding>(FragmentFeedBinding::inflate),
    ExceptionFragmentHandler {

    private val viewModel by viewModel<FeedViewModel>()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().changeColorStatusBar(attrColor = com.google.android.material.R.attr.colorSecondaryContainer)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeState()
        changeSortListener()
    }


    override fun navToExceptionFragment() {
//        findNavController().navigate(
//            R.id.exceptionFragment,
//            bundleOf(ExceptionFragment.SOURCE_FRAGMENT_ID_KEY to R.id.feedFragment),
//            navOptions {
//                popUpTo(R.id.feedFragment) {
//                    inclusive = true
//                }
//            }
//        )
    }

    override fun onDetach() {
        requireActivity().changeColorStatusBar(attrColor = com.google.android.material.R.attr.colorSurface)
        super.onDetach()
    }

    private fun observeState() {
        viewModel.uiState.launchAndCollectLatest(viewLifecycleOwner) { feedState ->
            when (feedState) {
                is FeedState.Default -> {
                    setIconSortType(feedState.sortType)
                }

                is FeedState.Loading -> {
                    //todo
                }

                is FeedState.Error -> {
                    //todo
                }
            }
        }
    }

    private fun setIconSortType(sortType: UiPostSortType) {
        val toolbarSortMenu = binding.feedToolBar.menu.findItem(R.id.toolbarSortMenu)
        val iconRes = when (sortType) {
            UiPostSortType.HOT -> R.drawable.sort_hot
            UiPostSortType.NEW -> R.drawable.sort_new
            UiPostSortType.TOP -> R.drawable.sort_top
            UiPostSortType.CONTROVERSIAL -> R.drawable.sort_controversial
            UiPostSortType.RISING -> R.drawable.sort_rising
        }
        toolbarSortMenu.icon = getDrawable(requireContext(), iconRes)
    }

    private fun changeSortListener() {

        binding.feedToolBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.feedSortHot -> {
                    viewModel.setPostSortType(UiPostSortType.HOT)
                    true
                }

                R.id.feedSortNew -> {
                    viewModel.setPostSortType(UiPostSortType.NEW)
                    true
                }

                R.id.feedSortTop -> {
                    viewModel.setPostSortType(UiPostSortType.TOP)
                    true
                }

                R.id.feedSortControversial -> {
                    viewModel.setPostSortType(UiPostSortType.CONTROVERSIAL)
                    true
                }

                R.id.feedSortRising -> {
                    viewModel.setPostSortType(UiPostSortType.RISING)
                    true
                }

                else -> false
            }

        }
    }
}