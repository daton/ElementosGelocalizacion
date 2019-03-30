package org.unitec.elementosgelocalizacion

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.widget.Toast
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity(), ActivityCompat.OnRequestPermissionsResultCallback  {

    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    private var mPermissionDenied = false
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)





        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)


        //La location callback
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                for (location in locationResult.locations) {
                    // Update UI with location data
                    // ...
                }
            }
        }

        enableMyLocation()
        //Invocamos el metodo de obtenerUbicacion()
        obtenerUbicacion()

        localizar.setOnClickListener {
            //Aqui vamos a navegar hacia el mapa
            //Forzamos la ubicacion a cada cliqueo
            obtenerUbicacion()
            var usuario=Usuario()
            var loca=Localizacion();
            usuario.id=999;
            usuario.nombre="Juan carlos"
          loca.lon=Constantes.milongi
          loca.lat =Constantes.milati
            usuario.localizacion=loca

            TareaLocaliar(applicationContext,usuario, this).execute(null,null,null)
            var i=Intent(this, MapsActivity::class.java);
            startActivity(i)
        }


    }

    private fun enableMyLocation() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(
                this, LOCATION_PERMISSION_REQUEST_CODE,
                Manifest.permission.ACCESS_FINE_LOCATION, true
            )
        }
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError()
            mPermissionDenied = false
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private fun showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog.newInstance(true).show(supportFragmentManager, "dialog")
    }

    /***
     * ESTE METODO DE OBTENER UBICACION ES SUMAMEMTE IMPORTANTE, ES EL QUE NOS VA  Y SE INVOCA
     * EN LA CLASE INTERNA DE REGISTRARSE PARA QUE NOS DE EN AUTOMÁTICO LA GEOLOCALIZACION
     * CON EL THREAD QUE ESTA CONSTANTEMENTE ACTUALIZANDOSE
     */
    @SuppressLint("MissingPermission")
    fun obtenerUbicacion() {


        // Este es otro cdigo

        val locationRequest = LocationRequest().apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        }

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);


        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                println("PEEEEERRRRRROOOO");

             Constantes.milati=   location?.latitude
             Constantes.milongi=location?.longitude

              //  Toast.makeText(
               //     applicationContext,
              //      "Lati " + Constantes.milati + " Longi" + Constantes.milongi + " alti " + location?.altitude
              //      ,
              //      Toast.LENGTH_LONG
             //   ).show()



            }

        println("ACEPTADO");

    }
}
