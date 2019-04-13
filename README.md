# Clase TareaLocalizarUsuarios
```
class TareaLocalizarUsuarios(private var ctx: Context?,
                    private var activity: MapsActivity?)
    : AsyncTask<Void,Void,Void>() {

var respuesta="Nada"


    override fun onPostExecute(result: Void?) {

        //Invocamos nuestra vistaa del MainActivity
        //  activity?.findViewById<TextView>(R.id.txtActual)?.text=estacion?.temp_c
        Toast.makeText(ctx, "SE cargo con tama "+Constantes.usuarios?.size, Toast.LENGTH_LONG).show()


    }

    override fun onPreExecute() {
        super.onPreExecute()


    }

    override fun doInBackground(vararg p0: Void?): Void? {
        try {
            var url = "https://daton1903.herokuapp.com/api/usuario";


            val restTemplate = RestTemplate()
            restTemplate.messageConverters.add(MappingJackson2HttpMessageConverter())


            val maper = ObjectMapper()
            //  usuarios = maper.readValue(estring, object : TypeReference<ArrayList<Usuario>>() {})

            respuesta = restTemplate.getForObject(url ,String::class.java)

           Constantes.usuarios= maper.readValue(respuesta, Array<Usuario>::class.java)


            println("DESPUES DE REST"+respuesta);
        } catch (t: Throwable) {
            println("SI HAY EXCEPCION DE REST  $t");
        }
        return null

    }


}
```

## MOdificar el MapsActivity

```
 mMap.addMarker(MarkerOptions().position(ecatepec).title("Juan Carlos").snippet("Tacos de carnitas"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(ecatepec))
        mMap.moveCamera(CameraUpdateFactory.zoomTo(18f))

       var options = GoogleMapOptions()
        options.mapType(GoogleMap.MAP_TYPE_SATELLITE)
            .compassEnabled(false)
            .rotateGesturesEnabled(false)
            .tiltGesturesEnabled(false);


      val  usus=Constantes.usuarios
        usus?.forEach {

            if(it.localizacion!=null&&it.orden!=null){
                var ecatepec =
                   LatLng(it?.localizacion?.lat!!,it.localizacion?.lon!!)
                mMap.addMarker(MarkerOptions().position(ecatepec).title(it?.nombre).snippet(it.orden?.comida))
            }


        }

```
