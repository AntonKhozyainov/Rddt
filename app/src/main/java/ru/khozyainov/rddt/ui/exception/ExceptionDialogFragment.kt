package ru.khozyainov.rddt.ui.exception

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import ru.khozyainov.rddt.R

class ExceptionDialogFragment : DialogFragment() {

    private val exceptionDialogListener: ExceptionDialogListener?
        get() = activity?.let {
            it as? ExceptionDialogListener
        }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setView(R.layout.dialog_exception)
            .setPositiveButton(R.string.retry) { _, _ ->
                exceptionDialogListener?.refreshState()
            }
            .create()
    }

    companion object {
        const val EXCEPTION_DIALOG_TAG = "EXCEPTION_DIALOG_TAG"
    }
}