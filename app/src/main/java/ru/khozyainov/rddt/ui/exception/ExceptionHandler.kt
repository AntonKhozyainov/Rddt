package ru.khozyainov.rddt.ui.exception

/**
* This interface is implemented by fragments that handle error conditions
*/
interface ExceptionHandler {

    /**
    * this function should be called when it is necessary to notify the user that an error has occurred
    *
    * function body example:
    *
    * findNavController().navigate(
    *        R.id.exceptionFragment,
    *        bundleOf(SOURCE_FRAGMENT_ID_KEY to [current fragment id]),
    *        navOptions {
    *            popUpTo([current fragment id]) {
    *                inclusive = true
    *            }
    *        }
    *    )
    */
    fun navToExceptionFragment()
}