package uz.doniyorbekurinboyev.avitotech

import com.google.android.gms.maps.model.LatLng

data class Pin(val id:Int, val service:String, val coordinates: Coordinate) {}
data class Coordinate(val lat: Double, val lng: Double){
    fun toLatLng():LatLng{
        return LatLng(lat, lng)
    }
}