package fabi.javewaze;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class GPSTracker extends Service implements LocationListener {
    //Contexto de la aplicación
    private final Context mContext;

    // bandera para estado del GPS (habilitado)
    private boolean isGPSEnabled = false;

    // bandera para red
    private boolean isNetworkEnabled = false;

    // bandera para estado del GPS (obtener lugar)
    private boolean canGetLocation = false;
    public static GPSTracker gps=null;
    private Location location; // localizacion
    private double latitude; // latitud
    private double longitude; // longitud

    // Distancia minima en metros para actualización
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = (long) 2; // 10 metros

    // Tiempo minimo de actualización por tiempo
    private static final long MIN_TIME_BW_UPDATES = 1000 * 5 * 1; // 2 segundos

    // Manager de Localización
    protected LocationManager locationManager;

    public static NotificationManager mNotificationManager;

    //Constructor
    public GPSTracker(Context context) {
        this.mContext = context; //Obtener el contexto
    }
    public static GPSTracker getInstance (Context c , NotificationManager n   ) {
        if(gps==null){
            gps=new GPSTracker(c);
            mNotificationManager = n;
        }
        gps.getLocation();
        return gps;
    }
    //    Obtener localización
    public Location getLocation() {
        try {
            //Se obtiene localización
            locationManager = (LocationManager) mContext
                    .getSystemService(LOCATION_SERVICE);

            // se obtiene el estado del GPS
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // obteniendo estado de la red
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no hay posibilidad de localizacion
            }
            else {
                this.canGetLocation = true;
                // Obtener localización de la red
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    Log.d("Red", "Proveedor de red");
                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }
                // si se inicia el GOS obtener longitud y latitud
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("GPS", "GPS Habilitado");
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }

    /**
     * Detener el GPS
     * Esta funcion detiene el uso de GPS por la aplicacion
     * */
    public void stopUsingGPS(){
        if(locationManager != null){
            locationManager.removeUpdates(GPSTracker.this);
        }
    }

    /**
     * Obtener latitud
     * */
    public double getLatitude(){
        if(location != null){
            latitude = location.getLatitude();
        }

        // return latitude
        return latitude;
    }

    /**
     * Obtener longitud
     * */
    public double getLongitude(){
        if(location != null){
            longitude = location.getLongitude();
        }

        // return longitude
        return longitude;
    }

    /**
     * verificar si esta disponible el GPS o la red
     * */
    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    /**
     Funcion que muestra la pantalla de configuración
     * */
    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        // Titulo del alertDialog
        alertDialog.setTitle("Configuracion GPS");

        // Mensaje del alertDialog
        alertDialog.setMessage("Debe ha bilitar el GPS, desea ir al menu de configuracion?");

        // Evento del boton Settings
        alertDialog.setPositiveButton("Configuracion", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });

        // evento presionar cancelar
        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // mostrar alertDialog
        alertDialog.show();
    }

    @Override
    public void onLocationChanged(Location location) {
        String stLongitude = Location.convert(location.getLongitude() , Location.FORMAT_MINUTES);
        String stLatitude = Location.convert(location.getLatitude(), Location.FORMAT_MINUTES);
        String [] ar1 = stLatitude.split("[.,]");
        String [] ar2 = stLongitude.split("[.,]");
        double lat = -1 , lon = -1;
        if(ar1.length >1 && ar2.length >1) {
            lat = Double.parseDouble("0." + ar1[1]);
            lon = Double.parseDouble("0." + ar2[1]);
            lat = lat * 60;
            lon = lon * -60;
        }

        if(!primerPlano()) {
            notificationBuilder("Holi", ObraActivity.class, 1);
        }else if (ObraActivity.isActivityVisible() == false){
            Intent i = new Intent(mContext , CafeteriaActivity.class);
            i.putExtra("id" , 2);
            mContext.startActivity(i);
        }

        for(MainActivity.Evento e : MainActivity.getSistema().eventos){
                String estado = MainActivity.getSistema().persona.estado;
                if(lat >=  e.infizqlat && lon >= e.infizqlon && lat <= e.supderlat && lon <= e.supderlon ){
                    if(e.tipo==1 ){ //tipo estatua
                        for(MainActivity.Estatua es : MainActivity.getSistema().estatuas) {
                            if(es.id == e.id && ( MainActivity.getSistema().persona.estado.equals(MainActivity.MODO_MUSEO) || MainActivity.getSistema().persona.estado.equals(MainActivity.MODO_TODOTERRENO) ) ) {
                                if(!primerPlano()) {
                                    Toast.makeText(mContext, es.nombre, Toast.LENGTH_LONG).show();
                                    notificationBuilder(es.nombre, EstatuaActivity.class, e.id);
                                }else if (EstatuaActivity.isActivityVisible() == false){
                                    Intent i = new Intent(mContext , EstatuaActivity.class);
                                    i.putExtra("id", es.id);
                                    mContext.startActivity(i);
                                }
                            }
                        }
                    }
                    if(e.tipo==2){
                        for(MainActivity.Cafeteria caf : MainActivity.getSistema().cafeterias) {
                            if(caf.id == e.id && (MainActivity.getSistema().persona.estado.equals(MainActivity.MODO_COMIDA) || MainActivity.getSistema().persona.estado.equals(MainActivity.MODO_TODOTERRENO) ) ) {
                                if(!primerPlano() ) {
                                    Toast.makeText(mContext, caf.nombre, Toast.LENGTH_LONG).show();
                                    notificationBuilder(caf.nombre, CafeteriaActivity.class, e.id);
                                }else if (CafeteriaActivity.isActivityVisible() == false ){
                                    Intent i = new Intent(mContext , CafeteriaActivity.class);
                                    i.putExtra("id" , caf.id);
                                    mContext.startActivity(i);
                                }
                            }

                        }
                    }
                    if(e.tipo==3){
                        for(MainActivity.Obra ob : MainActivity.getSistema().obras) {
                            if (ob.id == e.id) {
                                if(!primerPlano() ) {
                                    Toast.makeText(mContext, ob.nombre, Toast.LENGTH_LONG).show();
                                    notificationBuilder(ob.nombre, ObraActivity.class, e.id);
                                }else if (ObraActivity.isActivityVisible() == false){
                                    Intent i = new Intent(mContext , ObraActivity.class);
                                    i.putExtra("id" , ob.id);
                                    mContext.startActivity(i);
                                }
                            }
                        }
                    }
                }
        }

        /*Intent broadcastIntent = new Intent("com.example.broadcast.gps.location_change");
        broadcastIntent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        broadcastIntent.putExtra("lat", location.getLatitude());
        broadcastIntent.putExtra("lon", location.getLongitude());

        mContext.sendBroadcast(broadcastIntent);*/
        //Toast.makeText(mContext, "La localizacion es: - \nLat: " + lat + "\nLong: " + lon, Toast.LENGTH_LONG).show();

    }

    public void notificationBuilder( String title, Class c, int id ){
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext)
                .setSmallIcon(R.drawable.star_notification)
                .setContentTitle(title)
                .setContentText("Pulsa para ver mas detalles");

        Intent resultIntent = new Intent(mContext , c);
        resultIntent.putExtra("id", id);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(mContext);
        stackBuilder.addParentStack(c);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);



        if(mNotificationManager == null) {
            mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        mNotificationManager.notify(1, mBuilder.build());
    }

    public boolean primerPlano( ){
        if(MainActivity.isActivityVisible() || EstatuaActivity.isActivityVisible() || CafeteriaActivity.isActivityVisible()
                || ObraActivity.isActivityVisible()){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

}