package com.realtyagency.tm.app.platform

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.TextViewCompat
import androidx.fragment.app.DialogFragment
import com.realtyagency.tm.R
import com.realtyagency.tm.app.extensions.setTextOrHide
import kotlinx.android.synthetic.main.dialog_common.view.*

class CommonDialog : DialogFragment() {

    private var listener: CommonDialogListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        listener = (targetFragment as? CommonDialogListener) ?: activity as? CommonDialogListener

        val view = View.inflate(context, R.layout.dialog_common, null)
        val builder = AlertDialog.Builder(requireContext(), R.style.AlertDialog_Theme)

        builder.setView(view)

        setupTexts(view)
        setupButtons(view, builder)

        return builder.create()
    }

    private fun setupTexts(view: View) {
        val title = arguments?.getString(TITLE_KEY)
        val message = arguments?.getString(MESSAGE_KEY)
        val messageTextAppearance = arguments?.getInt(MESSAGE_APPEARANCE_KEY) ?: DEFAULT_VALUE_KEY

        view.tvTitle.setTextOrHide(title)
        view.tvMessage.setTextOrHide(message)

        if (messageTextAppearance != DEFAULT_VALUE_KEY) {
            TextViewCompat.setTextAppearance(view.tvMessage, messageTextAppearance)
        }
    }

    private fun setupButtons(view: View, builder: AlertDialog.Builder) {
        val tag = arguments?.getString(TAG_KEY)

        var positiveButton = arguments?.getInt(BTN_OK_KEY)
        var negativeButton = arguments?.getInt(BTN_CANCEL_KEY)

        if (positiveButton == null || positiveButton == DEFAULT_VALUE_KEY) {
            positiveButton = R.string.btn_ok
        }

        if (negativeButton == null || negativeButton == DEFAULT_VALUE_KEY) {
            negativeButton = R.string.btn_cancel
        }

        val isCancelVisible = arguments?.getBoolean(CANCEL_VISIBLE_KEY) ?: true

        builder.setPositiveButton(positiveButton) { _, _ -> positiveAction(tag) }

        if (isCancelVisible) {
            builder.setNegativeButton(negativeButton) { _, _ ->
                negativeAction(tag)
            }
        }
    }

    private fun positiveAction(tag: String?) {
        listener?.onCommonDialogEvent(CommonDialogEvent(DialogButton.OK, tag))
        dismiss()
    }

    private fun negativeAction(tag: String?) {
        listener?.onCommonDialogEvent(
            CommonDialogEvent(
                DialogButton.CANCEL,
                tag
            )
        )
        dismiss()
    }

    override fun onStart() {
        super.onStart()
        isCancelable = arguments?.getBoolean(CANCELABLE_KEY) ?: false
    }

    class Builder {

        private var title: String? = null
        private var message: String? = null
        private var messageTextAppearance: Int? = null
        private var tag: String? = null
        private var btnOk: Int? = null
        private var btnCancel: Int? = null
        private var cancelVisible: Boolean = true
        private var cancelable: Boolean = false

        fun title(title: String): Builder {
            this.title = title
            return this
        }

        fun message(message: String): Builder {
            this.message = message
            return this
        }

        fun messageTextAppearance(textAppearance: Int): Builder {
            this.messageTextAppearance = textAppearance
            return this
        }

        fun btnOk(@StringRes btnOk: Int): Builder {
            this.btnOk = btnOk
            return this
        }

        fun btnCancel(@StringRes btnCancel: Int): Builder {
            this.btnCancel = btnCancel
            return this
        }

        fun cancelVisible(cancelVisible: Boolean): Builder {
            this.cancelVisible = cancelVisible
            return this
        }

        fun tag(tag: String): Builder {
            this.tag = tag
            return this
        }

        fun cancelable(cancelable: Boolean): Builder {
            this.cancelable = cancelable
            return this
        }

        fun build(): CommonDialog {
            val instance = CommonDialog()
            val args = Bundle()

            args.putString(TITLE_KEY, title)
            args.putString(MESSAGE_KEY, message)
            args.putInt(MESSAGE_APPEARANCE_KEY, messageTextAppearance ?: DEFAULT_VALUE_KEY)
            args.putString(TAG_KEY, tag)
            args.putBoolean(CANCELABLE_KEY, cancelable)
            args.putBoolean(CANCEL_VISIBLE_KEY, cancelVisible)
            args.putInt(BTN_OK_KEY, btnOk ?: DEFAULT_VALUE_KEY)
            args.putInt(BTN_CANCEL_KEY, btnCancel ?: DEFAULT_VALUE_KEY)

            instance.arguments = args
            return instance
        }

    }

    companion object {

        const val TAG = "CommonDialog"

        const val TITLE_KEY = "TITLE_KEY"
        const val MESSAGE_KEY = "MESSAGE_KEY"
        const val MESSAGE_APPEARANCE_KEY = "MESSAGE_APPEARANCE_KEY"
        const val TAG_KEY = "TAG_KEY"
        const val BTN_OK_KEY = "BTN_OK_KEY"
        const val BTN_CANCEL_KEY = "BTN_CANCEL_KEY"
        const val CANCEL_VISIBLE_KEY = "CANCEL_VISIBLE_KEY"
        const val CANCELABLE_KEY = "CANCELABLE_KEY"

        const val DEFAULT_VALUE_KEY = 0
    }

}


class CommonDialogEvent(
    val button: DialogButton,
    val tag: String?
)

enum class DialogButton {
    OK,
    CANCEL
}

interface CommonDialogListener {
    fun onCommonDialogEvent(event: CommonDialogEvent)
}