package ru.khozyainov.rddt.ui.feed

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat.getDrawable
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.fold
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.reduce
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.khozyainov.rddt.R
import ru.khozyainov.rddt.databinding.FragmentFeedBinding
import ru.khozyainov.rddt.model.UiPostSortType
import ru.khozyainov.rddt.ui.exception.ExceptionFragmentHandler
import ru.khozyainov.rddt.utils.ViewBindingFragment
import ru.khozyainov.rddt.utils.changeColorStatusBar
import ru.khozyainov.rddt.utils.launchAndCollectLatest
import ru.khozyainov.rddt.utils.postSortChangeFlow
import ru.khozyainov.rddt.utils.searchChangeFlow

@OptIn(ExperimentalCoroutinesApi::class)
class FeedFragment : ViewBindingFragment<FragmentFeedBinding>(FragmentFeedBinding::inflate),
    ExceptionFragmentHandler {

    private val viewModel by viewModel<FeedViewModel>()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().changeColorStatusBar(attrColor = com.google.android.material.R.attr.colorSecondaryContainer)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindSearchAndSortChangeFlow()
        observeState()
        //changeSortListener()
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
            setIconSortType(feedState.sortType)
            Toast.makeText(requireContext(), feedState.query, Toast.LENGTH_LONG).show()
//            when (feedState) {
//                is FeedState.Default -> {
//                    setIconSortType(feedState.sortType)
//                    Toast.makeText(requireContext(), feedState.query, Toast.LENGTH_LONG).show()
//                }
//
//                is FeedState.Loading -> {
//                    //todo
//                }
//
//                is FeedState.Error -> {
//                    //todo
//                }
//            }
        }
    }


    private fun bindSearchAndSortChangeFlow() = viewLifecycleOwner.lifecycleScope.launch{
        val searchView = getToolbarMenuItem(itemId = R.id.toolbarSearch).actionView as SearchView
        viewModel.bindSearchAndSortState(
            queryFlow = searchView.searchChangeFlow().onStart { emit("") },
            postSortFlow = binding.feedToolBar.postSortChangeFlow().onStart { emit(null) }
        )
    }

    private fun setIconSortType(sortType: UiPostSortType) {
        val toolbarSortMenu = getToolbarMenuItem(itemId = R.id.toolbarSortMenu)
        val iconRes = when (sortType) {
            UiPostSortType.HOT -> R.drawable.sort_hot
            UiPostSortType.NEW -> R.drawable.sort_new
            UiPostSortType.TOP -> R.drawable.sort_top
            UiPostSortType.CONTROVERSIAL -> R.drawable.sort_controversial
            UiPostSortType.RISING -> R.drawable.sort_rising
        }
        toolbarSortMenu.icon = getDrawable(requireContext(), iconRes)
    }

    private fun getToolbarMenuItem(@IdRes itemId: Int): MenuItem =
        binding.feedToolBar.menu.findItem(itemId)

}