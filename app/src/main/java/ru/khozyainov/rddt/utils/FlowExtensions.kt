package ru.khozyainov.rddt.utils


import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.*
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.launch
import ru.khozyainov.rddt.R
import ru.khozyainov.rddt.model.UiPostSortType

inline fun <T> Flow<T>.launchAndCollect(
    owner: LifecycleOwner,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    crossinline action: suspend CoroutineScope.(T) -> Unit
) = owner.lifecycleScope.launch {
    owner.repeatOnLifecycle(minActiveState) {
        collect {
            action(it)
        }
    }
}

inline fun <T> Flow<T>.launchAndCollectLatest(
    owner: LifecycleOwner,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    crossinline action: suspend CoroutineScope.(T) -> Unit
) = owner.lifecycleScope.launch {
    owner.repeatOnLifecycle(minActiveState) {
        collectLatest {
            action(it)
        }
    }
}

inline fun <T> Flow<T>.launchAndCollectLatestForActivity(
    owner: Lifecycle,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    crossinline action: suspend CoroutineScope.(T) -> Unit
) = owner.coroutineScope.launch {
    owner.repeatOnLifecycle(minActiveState) {
        collectLatest {
            action(it)
        }
    }
}

fun SearchView.searchChangeFlow(): Flow<String> {
    return callbackFlow {
        val searchWatcher = object : SearchView.OnQueryTextListener, View.OnFocusChangeListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                trySendBlocking(query.orEmpty())
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                if (!hasFocus) trySendBlocking(String())
            }

        }

        this@searchChangeFlow.setOnQueryTextListener(searchWatcher)
        this@searchChangeFlow.setOnQueryTextFocusChangeListener(searchWatcher)

        awaitClose {
            this@searchChangeFlow.setOnQueryTextListener(null)
            this@searchChangeFlow.setOnQueryTextFocusChangeListener(null)
        }
    }
}

fun MaterialToolbar.postSortChangeFlow(): Flow<UiPostSortType?> {
    return callbackFlow {
        val onMenuItemClickListener = Toolbar.OnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.feedSortHot -> {
                    trySendBlocking(UiPostSortType.HOT)
                    true
                }

                R.id.feedSortNew -> {
                    trySendBlocking(UiPostSortType.NEW)
                    true
                }

                R.id.feedSortTop -> {
                    trySendBlocking(UiPostSortType.TOP)
                    true
                }

                R.id.feedSortControversial -> {
                    trySendBlocking(UiPostSortType.CONTROVERSIAL)
                    true
                }

                R.id.feedSortRising -> {
                    trySendBlocking(UiPostSortType.RISING)
                    true
                }

                else -> false
            }
        }

        this@postSortChangeFlow.setOnMenuItemClickListener(onMenuItemClickListener)

        awaitClose {
            this@postSortChangeFlow.setOnMenuItemClickListener(null)
        }
    }
}

/**
 * Emits the previous values (`null` if there is no previous values) along with the current one.
 * For example:
 * - origin flow:
 *   ```
 *   [
 *     "a",
 *     "b",
 *     "c",
 *     "d"
 *   ]
 *   ```
 * - resulting flow (count = 2):
 *   ```
 *   [
 *     (null, null)
 *     (null, "a"),
 *     ("a",  "b"),
 *     ("b",  "c"),
 *     ("c",  "d")
 *   ]
 *   ```
 */
@ExperimentalCoroutinesApi
fun <T> Flow<T>.simpleScan(count: Int): Flow<List<T?>> {
    val items = List<T?>(count) { null }
    return this.scan(items) { previous, value -> previous.drop(1) + value }
}

//fun ExceptionFragment.onException(activity: AppCompatActivity){
//    activity.supportFragmentManager.beginTransaction()
//        .add(R.id.containerLauncherActivity, ExceptionFragment())
//        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
//        .commit()
//}