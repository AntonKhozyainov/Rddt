package ru.khozyainov.rddt.ui.exception

import androidx.appcompat.app.AppCompatActivity

abstract class AppCompatActivityWithExceptionDialog: AppCompatActivity(), ExceptionDialogListener {

    protected fun showExceptionDialogFragment() {
        if (getExceptionDialogFragment() == null) {
            ExceptionDialogFragment().show(supportFragmentManager,
                ExceptionDialogFragment.EXCEPTION_DIALOG_TAG
            )
        }
    }

    protected fun dismissExceptionDialogFragment() {
        getExceptionDialogFragment()?.dismiss()
    }

    private fun getExceptionDialogFragment(): ExceptionDialogFragment? =
        supportFragmentManager.findFragmentByTag(ExceptionDialogFragment.EXCEPTION_DIALOG_TAG)?.let {
            it as? ExceptionDialogFragment
        }
}