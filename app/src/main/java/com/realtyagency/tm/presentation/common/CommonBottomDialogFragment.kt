package com.realtyagency.tm.presentation.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.realtyagency.tm.R
import kotlinx.android.synthetic.main.dialog_bottom_sheet.*

class CommonBottomDialogFragment : BottomSheetDialogFragment() {

    private var listener: BottomSheetDialogListener? = null

    private val bottomDialogAdapter by lazy {
        ListDelegationAdapter(
            bottomDialogDelegate(clickListener)
        )
    }

    private val clickListener: (ItemDialog) -> Unit = {
        listener?.onItemDialogClick(it)
        dismiss()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = LayoutInflater.from(context).inflate(R.layout.dialog_bottom_sheet, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        listener = targetFragment as? BottomSheetDialogListener
        val items = arguments?.getSerializable(ARG_ITEMS) as? List<ItemDialog>
        rvBottomSheetDialog.layoutManager = LinearLayoutManager(activity)
        rvBottomSheetDialog.adapter = bottomDialogAdapter

        bottomDialogAdapter.items = items
    }

    interface BottomSheetDialogListener {
        fun onItemDialogClick(itemDialog: ItemDialog)
    }

    companion object {
        private const val ARG_ITEMS = "items"

        fun newInstance(items: List<ItemDialog>) =
            CommonBottomDialogFragment().apply {
                arguments = bundleOf(ARG_ITEMS to items)
            }
    }
}