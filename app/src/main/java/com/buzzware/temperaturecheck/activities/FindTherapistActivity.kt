package com.buzzware.temperaturecheck.activities

import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.buzzware.temperaturecheck.retrofit.Controller
import com.buzzware.temperaturecheck.R
import com.buzzware.temperaturecheck.adapters.LocationAdapter
import com.buzzware.temperaturecheck.classes.LocationUtility
import com.buzzware.temperaturecheck.databinding.ActivityFindTherapistBinding
import com.buzzware.temperaturecheck.model.ClinicAdress
import com.buzzware.temperaturecheck.model.GooglePlaceResponse
import com.buzzware.temperaturecheck.model.Location
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import retrofit2.Call
import retrofit2.Response

class FindTherapistActivity : BaseActivity(),OnMapReadyCallback {

    private val binding : ActivityFindTherapistBinding by lazy {
        ActivityFindTherapistBinding.inflate(layoutInflater)
    }

    private lateinit var mMap: GoogleMap

    private val clicnicList : ArrayList<ClinicAdress> = ArrayList<ClinicAdress>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setView()
        setListener()

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    private fun setView() {

    }

    private fun setListener() {

        binding.backIV.setOnClickListener { onBackPressed() }


    }


    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(fadeIn, fadeOut)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mDialog.show()
        mMap = googleMap
        mMap.isMyLocationEnabled = true

        LocationUtility(this).getLastKnownLocation { latLng, location ->
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17f))
            hitApi(latLng)
        }
    }

    private fun hitApi(latLng: LatLng) {

        Controller.instance.getNearbyPlaces(
            "${latLng.latitude},${latLng.longitude}",
            "Psychologist",
            getString(R.string.google_api_key)
        )
            .enqueue(object : retrofit2.Callback<GooglePlaceResponse> {
                override fun onResponse(call: Call<GooglePlaceResponse>, response: Response<GooglePlaceResponse>) {
                    val body = response.body()
                    if (response.isSuccessful) {
                       // Log.d("API_RESPONSE", "Response Success: ${body?.results}")

                        body?.results?.forEach {
                            clicnicList.add(ClinicAdress(it.place_id, it.name, it.formatted_address, it.geometry.location))
                        }

                        Log.d("API_RESPONSE", "${clicnicList}")

                        placeMarkerOnMap()

                    } else {
                        mDialog.dismiss()
                        showAlert("${response.message()}")
                    }
                }
                override fun onFailure(call: Call<GooglePlaceResponse>, t: Throwable) {
                    mDialog.dismiss()
                    showAlert( "${t.message}")
                    Log.d("API_RESPONSE", "${t.message}")

                }
            })
    }

    private fun placeMarkerOnMap() {
        val bitmap = getBitmapFromDrawable()
        clicnicList.forEach { model ->
            mMap.addMarker(
                MarkerOptions()
                    .position(LatLng(model.location.lat, model.location.lng))
                    .title(model.name)
                    .icon(bitmap)
            )

        }

        binding.therapistRecycler.layoutManager = LinearLayoutManager(this)
        binding.therapistRecycler.adapter = LocationAdapter(this, clicnicList)
        mDialog.dismiss()
    }


    fun getBitmapFromDrawable(): BitmapDescriptor {
        val drawable = ContextCompat.getDrawable(this, R.mipmap.ic_launcher_round) ?: return BitmapDescriptorFactory.defaultMarker()
        val bitmap = Bitmap.createBitmap(60, 60, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, 60, 60)
        drawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }
}