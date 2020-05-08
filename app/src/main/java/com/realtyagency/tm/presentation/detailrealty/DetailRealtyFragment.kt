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
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.realtyagency.tm.R
import com.realtyagency.tm.app.extensions.*
import com.realtyagency.tm.app.platform.BaseFragment
import com.realtyagency.tm.data.db.entities.Realty
import com.realtyagency.tm.presentation.common.ScrollMapFragment
import com.realtyagency.tm.presentation.delegates.featuresContentRealtyDelegate
import com.realtyagency.tm.presentation.delegates.mainContentRealtyDelegate
import com.realtyagency.tm.presentation.delegates.realtyDetailPhotoDelegate
import kotlinx.android.synthetic.main.fragment_detail_realty.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class DetailRealtyFragment : BaseFragment(R.layout.fragment_detail_realty) {

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
                    //TODO
                }),
            featuresContentRealtyDelegate()
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observe(screenViewModel.isFavorite, ::handleRealtyFavorite)

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

    companion object {
        private const val DETAIL_REALTY: String = "DetailRealty"
        fun newInstance(realty: Realty) =
            DetailRealtyFragment().apply {
                arguments = bundleOf(DETAIL_REALTY to realty)
            }
    }
}