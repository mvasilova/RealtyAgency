package com.realtyagency.tm.presentation.detailrealty

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.toBitmap
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.realtyagency.tm.R
import com.realtyagency.tm.app.extensions.*
import com.realtyagency.tm.app.platform.Action
import com.realtyagency.tm.app.platform.BaseFragment
import com.realtyagency.tm.data.db.entities.Realty
import com.realtyagency.tm.presentation.common.CommonBottomDialogFragment
import com.realtyagency.tm.presentation.common.CommonBottomDialogFragment.BottomSheetDialogListener
import com.realtyagency.tm.presentation.common.ItemDialog
import com.realtyagency.tm.presentation.common.ScrollMapFragment
import com.realtyagency.tm.presentation.delegates.featuresContentRealtyDelegate
import com.realtyagency.tm.presentation.delegates.mainContentRealtyDelegate
import com.realtyagency.tm.presentation.delegates.realtyDetailPhotoDelegate
import kotlinx.android.synthetic.main.fragment_detail_realty.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class DetailRealtyFragment : BaseFragment(R.layout.fragment_detail_realty),
    BottomSheetDialogListener {

    override val statusBarColor: Int
        get() = R.color.colorTransparent

    override val statusBarLightMode: Boolean
        get() = false

    override val toolbarDrawableClose: Int?
        get() = R.drawable.ic_toolbar_back_white

    override val screenViewModel by viewModel<DetailRealtyViewModel> {
        parametersOf(realtyOfBundle)
    }

    private var map: GoogleMap? = null

    private val realtyOfBundle by lazy { arguments?.getSerializable(DETAIL_REALTY) as? Realty }

    private val imageAdapter by lazy {
        ListDelegationAdapter(
            realtyDetailPhotoDelegate {
                screenViewModel.navigateToViewMediaFiles(it)
            }
        )
    }

    private val contentAdapter by lazy {
        ListDelegationAdapter(
            mainContentRealtyDelegate(
                {
                    requireContext().goToPhoneDial()
                },
                {
                    checkListComparison()
                }),
            featuresContentRealtyDelegate()
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observe(screenViewModel.isFavorite, ::handleRealtyFavorite)
        observe(screenViewModel.comparisonId, ::handleAddingComparison)
        observe(screenViewModel.comparisons, {})

        setupMap(savedInstanceState)
        setupContent()
        setupAppBar()
        setRealty()

        ivFavorite.setOnClickListener {
            screenViewModel.changeFavorite()
        }
    }

    private fun handleRealtyFavorite(isFavorite: Boolean?) {
        ivFavorite.background = if (isFavorite == true) {
            requireContext().getCompatDrawable(R.drawable.ic_favorite_fill)
        } else {
            requireContext().getCompatDrawable(R.drawable.ic_favorite_border)
        }
    }

    private fun handleAddingComparison(comparisonId: Int?) {
        comparisonId?.let {
            notifySnack(
                R.string.dialog_realty_added_successful,
                Snackbar.LENGTH_LONG,
                Action(R.string.btn_show) {
                    screenViewModel.navigateToDetailComparison(it)
                })
        }
    }

    private fun setRealty() {
        realtyOfBundle?.let { realty ->

            tvAddress.text = realty.address

            val contentItems = mutableListOf<Any>()
            contentItems.add(realty)

            realty.parameters?.let { p ->
                realty.typeAdvert?.let { contentItems.add(getString(R.string.label_advert_type) to it) }
                p.datePublic?.let {
                    contentItems.add(
                        getString(R.string.label_advert_date) to it.toDateFormatGmt(
                            requireContext()
                        )
                    )
                }
                p.countRoom?.let { contentItems.add(getString(R.string.label_advert_count_room) to it) }
                p.countFloor?.let { contentItems.add(getString(R.string.label_advert_count_floor) to it) }
                p.floorFlat?.let { contentItems.add(getString(R.string.label_advert_floor_flat) to it) }
                p.squareHome?.let {
                    contentItems.add(
                        getString(R.string.label_advert_square_home) to getString(
                            R.string.placeholder_sq_metres,
                            it.toString()
                        )
                    )
                }
                p.squarePlot?.let {
                    contentItems.add(
                        getString(R.string.label_advert_square_plot) to getString(
                            R.string.placeholder_acres,
                            it.toString()
                        )
                    )
                }
                p.typeHousing?.let { contentItems.add(getString(R.string.label_advert_type_housing) to it) }
                p.liftEnabled?.let { contentItems.add(getString(R.string.label_advert_lift) to it) }
                p.repair?.let { contentItems.add(getString(R.string.label_advert_repair) to it) }
                p.distanceFromCenter?.let {
                    contentItems.add(
                        getString(R.string.label_advert_distance_from_center) to getString(
                            R.string.placeholder_kilometres,
                            it.toString()
                        )
                    )
                }
                p.yearOfConstruction?.let { contentItems.add(getString(R.string.label_advert_year_of_construction) to it) }
            }

            contentAdapter.setData(contentItems)
            imageAdapter.setData(realty.photos)
            pivImages.count = realty.photos?.count() ?: 0

        }
    }

    private fun setupContent() {
        rvContent.layoutManager = LinearLayoutManager(activity)
        rvContent.isNestedScrollingEnabled = false
        rvContent.adapter = contentAdapter
        vpRealtyImage.adapter = imageAdapter
        pivImages.setViewPager(vpRealtyImage)
    }

    private fun setupAppBar() {
        (activity as AppCompatActivity).setSupportActionBar(toolbarRealty)
        (activity as AppCompatActivity).title = ""
        toolbarRealty?.setNavigationOnClickListener {
            screenViewModel.onExit()
        }
    }

    private fun setupMap(savedInstanceState: Bundle?) {
        val mapView = childFragmentManager.findFragmentById(R.id.mapView) as? ScrollMapFragment
        mapView?.onCreate(savedInstanceState)
        mapView?.getMapAsync { googleMap ->
            map = googleMap

            if (realtyOfBundle != null && realtyOfBundle?.lat != null && realtyOfBundle?.lon != null) {
                setMarkerOnMap(LatLng(realtyOfBundle!!.lat!!, realtyOfBundle!!.lon!!))
            }
        }

        mapView?.setListener {
            scrollView.requestDisallowInterceptTouchEvent(true)
        }

        btnZoomIn?.setOnClickListener {
            map?.animateCamera(CameraUpdateFactory.zoomIn())
        }

        btnZoomOut?.setOnClickListener {
            map?.animateCamera(CameraUpdateFactory.zoomOut())
        }

        map?.uiSettings?.isZoomControlsEnabled = false
        map?.uiSettings?.isScrollGesturesEnabledDuringRotateOrZoom = true
        map?.uiSettings?.setAllGesturesEnabled(true)
    }

    private fun setMarkerOnMap(latLng: LatLng) {
        map?.addMarker(
            MarkerOptions().position(latLng).icon(
                BitmapDescriptorFactory.fromBitmap(
                    AppCompatResources.getDrawable(
                        requireContext(),
                        R.drawable.ic_mapview_marker
                    )?.toBitmap()
                )
            )
        )
        map?.moveCamera(
            CameraUpdateFactory.newLatLngZoom(latLng, 16f)
        )
    }

    private fun checkListComparison() {
        val comparisons = screenViewModel.getComparisons()
        if (comparisons.isNullOrEmpty()) {
            screenViewModel.insertNewComparison()
        } else {
            val list = mutableListOf<ItemDialog>()
            list.add(
                ItemDialog(
                    0,
                    R.drawable.ic_request_new,
                    getString(R.string.label_new_comparison)
                )
            )
            comparisons.forEach {
                list.add(
                    ItemDialog(
                        it.idComparison, R.drawable.ic_request_edit,
                        getString(R.string.placeholder_comparison_name, it.idComparison)
                    )
                )
            }

            showBottomSheetDialog(list)
        }
    }

    private fun showBottomSheetDialog(list: List<ItemDialog>) {
        val dialog = CommonBottomDialogFragment.newInstance(list)

        dialog.setTargetFragment(this, 0)
        dialog.show(supportFragmentManager, TAG_BOTTOM_SHEET_DIALOG)
    }

    override fun onItemDialogClick(itemDialog: ItemDialog) {
        when (itemDialog.id) {
            0 -> {
                screenViewModel.insertNewComparison()
            }
            else -> {
                screenViewModel.addRealtyToListComparison(itemDialog.id)
            }
        }
    }

    companion object {
        const val TAG_BOTTOM_SHEET_DIALOG = "BottomDialog"
        private const val DETAIL_REALTY: String = "DetailRealty"
        fun newInstance(realty: Realty) =
            DetailRealtyFragment().apply {
                arguments = bundleOf(DETAIL_REALTY to realty)
            }
    }
}