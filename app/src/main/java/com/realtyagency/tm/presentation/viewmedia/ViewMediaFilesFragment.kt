package com.realtyagency.tm.presentation.viewmedia

import android.os.Bundle
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.realtyagency.tm.R
import com.realtyagency.tm.app.extensions.setData
import com.realtyagency.tm.app.platform.BaseFragment
import com.realtyagency.tm.presentation.viewmedia.delegates.viewImageFilesDelegate
import kotlinx.android.synthetic.main.fragment_view_media_files.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class ViewMediaFilesFragment : BaseFragment(R.layout.fragment_view_media_files) {

    override val statusBarColor: Int
        get() = R.color.colorMediaBackground

    override val statusBarLightMode: Boolean
        get() = false

    override val showBottomNavWhenFragmentLaunch: Boolean
        get() = false

    override val screenViewModel by viewModel<ViewMediaFilesViewModel>()

    private val mediaAdapter by lazy {
        ListDelegationAdapter(
            viewImageFilesDelegate()
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ivViewClose.setOnClickListener {
            screenViewModel.onExit()
        }
        setupContent()
    }

    private fun setupContent() {
        val list = (arguments?.getSerializable(MEDIA_LIST) as? ArrayList<String>).orEmpty()
        val item = arguments?.getSerializable(MEDIA_ITEM)
        val itemPosition = list.indexOf(item)

        val data = mutableListOf<Any>().apply {
            addAll(list)
        }
        mediaAdapter.setData(data)

        vpMedia.adapter = mediaAdapter
        vpMedia.setCurrentItem(itemPosition, false)
        vpMedia.offscreenPageLimit = 1
        tvMediaCount.text =
            getString(R.string.placeholder_count_media, itemPosition + 1, list.size)

        vpMedia.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                tvMediaCount.text =
                    getString(R.string.placeholder_count_media, position + 1, list.size)
            }
        })
    }

    companion object {
        private const val MEDIA_ITEM: String = "MediaItem"
        private const val MEDIA_LIST: String = "MediaList"

        fun newInstance(item: String, list: ArrayList<String>): ViewMediaFilesFragment =
            ViewMediaFilesFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(MEDIA_ITEM, item)
                    putSerializable(MEDIA_LIST, list)
                }
            }
    }
}