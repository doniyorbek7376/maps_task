package uz.doniyorbekurinboyev.avitotech

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonParser

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var mFloatingActionButton: FloatingActionButton
    private var mPins: MutableList<Pin> = mutableListOf()

    companion object{
        const val REQUEST_FILTER:Int = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        mFloatingActionButton = findViewById(R.id.floating_action_button)
        mFloatingActionButton.setOnClickListener{
            startActivityForResult(FilterActivity.newIntent(this), REQUEST_FILTER)
        }
        parseJSON()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        updateMarkers()
    }

    private fun updateMarkers() {
        mMap.clear()
        for (pin in mPins) {
            if (Filter.filter[pin.service] == true)
                mMap.addMarker(
                    MarkerOptions()
                        .position(pin.coordinates.toLatLng())
                        .title(pin.service)
                        .icon(
                            BitmapDescriptorFactory.defaultMarker(
                                when (pin.service) {
                                    "a" -> BitmapDescriptorFactory.HUE_RED
                                    "b" -> BitmapDescriptorFactory.HUE_YELLOW
                                    "c" -> BitmapDescriptorFactory.HUE_GREEN
                                    else -> BitmapDescriptorFactory.HUE_ROSE
                                }
                            )
                        )
                )
        }
    }

    private fun parseJSON(){
        val json = assets.open("pins.json").reader().readText()
        val element = JsonParser.parseString(json).asJsonObject

        val services = element.get("services").asJsonArray

        for(service: JsonElement in services){
            if(Filter.filter[service.asString]==null)
                Filter.filter[service.asString] = true
        }
        val pins = element.get("pins").asJsonArray
        for(pinJson in pins){
            val pin:Pin = Gson().fromJson(pinJson, Pin::class.java)
            mPins.add(pin)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_FILTER)
            updateMarkers()
    }
}