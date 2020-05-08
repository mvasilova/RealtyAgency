package com.realtyagency.tm.presentation.common

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.realtyagency.tm.R
import com.realtyagency.tm.app.platform.ErrorHandler
import kotlinx.android.synthetic.main.dialog_server_error.view.*
import kotlinx.android.synthetic.main.fragment_connection_error.view.*
import org.koin.android.ext.android.inject


class FullScreenDialog : DialogFragment() {

    private val errorHandler: ErrorHandler by inject()

    private var listener: FullScreenDialogListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)
        listener = (targetFragment as? FullScreenDialogListener) ?: activity as? FullScreenDialogListener

        val layout = arguments?.getInt(ARG_LAYOUT)
        val title = arguments?.getInt(ARG_TITLE)
        val description = arguments?.getInt(ARG_DESCRIPTION)
        val isListener = arguments?.getBoolean(ARG_LISTENER)

        val view = layout?.let { LayoutInflater.from(context).inflate(it, null) }
        val builder = AlertDialog.Builder(requireContext(), android.R.style.Theme_Material_NoActionBar)
        builder.setView(view)
        isCancelable = false

        when (layout) {
            R.layout.fragment_connection_error -> {
                view?.btnConnectionErrorConfirm?.setOnClickListener {
                    listener?.onClick()
                    if (!errorHandler.withoutNetworkConnection()) {
                        dismiss()
                    }
                }
            }
            R.layout.dialog_server_error -> {
                view?.tvServerErrorLabel?.text = title?.let { getString(it) }
                view?.tvServerErrorDescription?.text = description?.let { getString(it) }

                view?.btnServerErrorConfirm?.setOnClickListener {
                    if (isListener == true) {
                        listener?.onClick()
                    }
                    dismiss()
                }
            }
        }
        return builder.create()
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.statusBarColor = requireActivity().getColor(R.color.colorWhite)
        dialog?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }

    companion object {
        const val DIALOG_FULL_SCREEN_TAG = "DialogFullScreen"

        private const val ARG_LAYOUT = "ArgLayout"
        private const val ARG_TITLE = "ArgTitle"
        private const val ARG_DESCRIPTION = "ArgDescription"
        private const val ARG_LISTENER = "ArgListener"

        fun newInstance(
            layout: Int? = R.layout.dialog_server_error,
            title: Int? = null,
            description: Int? = null,
            listener: Boolean? = false
        ) =
            FullScreenDialog().apply {
                arguments = bundleOf(
                    ARG_LAYOUT to layout,
                    ARG_TITLE to title,
                    ARG_DESCRIPTION to description,
                    ARG_LISTENER to listener
                )
            }
    }
}

interface FullScreenDialogListener {
    fun onClick()
}