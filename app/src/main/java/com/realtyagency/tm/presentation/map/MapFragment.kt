package com.realtyagency.tm.presentation.map

import android.app.Activity
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.toBitmap
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.realtyagency.tm.R
import com.realtyagency.tm.app.extensions.formatWithDot
import com.realtyagency.tm.app.extensions.getCompatDrawable
import com.realtyagency.tm.app.extensions.observe
import com.realtyagency.tm.app.extensions.observeEvent
import com.realtyagency.tm.app.platform.BaseFragment
import com.realtyagency.tm.presentation.common.ScrollMapFragment
import kotlinx.android.synthetic.main.fragment_map.*
import kotlinx.android.synthetic.main.fragment_new_request.tvCoordinates
import kotlinx.android.synthetic.main.layout_progress.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MapFragment : BaseFragment(R.layout.fragment_map), OnMapReadyCallback {

    override val toolbarTitle: Int?
        get() = R.string.toolbar_title_map

    override val toolbarDrawableClose: Int?
        get() = R.drawable.ic_toolbar_back

    private var map: GoogleMap? = null

    private val viewModel: MapViewModel by viewModel()

    override val screenViewModel by lazy { viewModel }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressLayout?.background = requireActivity().getCompatDrawable(R.color.colorWhite)

        setupMap(savedInstanceState)
        setupObservers()

        viewModel.getLocation()

        btnApply.setOnClickListener {
            viewModel.sendEvent()
            viewModel.navigateToNewRequest()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_LOCATION_INTENT_SENDER_FOR_RESULT_TAG -> if (resultCode == Activity.RESULT_OK) {
                viewModel.getLocation()
            }
        }
    }

    private fun setupObservers() {
        observe(viewModel.location, ::handleLocation)
        observeEvent(viewModel.resolvableApiEvent, {
            startIntentSenderForResult(
                it.resolution.intentSender,
                REQUEST_LOCATION_INTENT_SENDER_FOR_RESULT_TAG,
                null,
                0,
                0,
                0,
                null
            )
        })
    }

    private fun handleLocation(location: Location?) {
        location?.let {
            setMarkerOnMap(LatLng(location.latitude, location.longitude))
            tvCoordinates.text =
                resources.getString(
                    R.string.placeholder_coordinates,
                    it.latitude.formatWithDot(),
                    it.longitude.formatWithDot()
                )
        }
    }

    private fun setupMap(savedInstanceState: Bundle?) {
        val mapView =
            childFragmentManager.findFragmentById(R.id.fullScreenMapView) as? ScrollMapFragment
        mapView?.onCreate(savedInstanceState)
        mapView?.getMapAsync(this)

        btnZoomIn?.setOnClickListener {
            map?.animateCamera(CameraUpdateFactory.zoomIn())
        }

        btnZoomOut?.setOnClickListener {
            map?.animateCamera(CameraUpdateFactory.zoomOut())
        }

    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        map?.uiSettings?.isZoomControlsEnabled = false
        map?.uiSettings?.isScrollGesturesEnabledDuringRotateOrZoom = true
        map?.uiSettings?.setAllGesturesEnabled(true)

        map?.setOnMapClickListener {
            viewModel.setLocation(it.latitude, it.longitude)
            setMarkerOnMap(LatLng(it.latitude, it.longitude), false)
            tvCoordinates.text =
                resources.getString(
                    R.string.placeholder_coordinates,
                    it.latitude.formatWithDot(),
                    it.longitude.formatWithDot()
                )
        }

    }

    private fun setMarkerOnMap(latLng: LatLng, moveCamera: Boolean = true) {
        map?.clear()
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
        if (moveCamera)
            map?.moveCamera(
                CameraUpdateFactory.newLatLngZoom(latLng, 16f)
            )
    }

    companion object {
        const val REQUEST_LOCATION_INTENT_SENDER_FOR_RESULT_TAG = 110
    }
}