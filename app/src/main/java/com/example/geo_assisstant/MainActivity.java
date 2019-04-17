package com.example.geo_assisstant;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay;
import org.mapsforge.map.android.layers.MyLocationOverlay;

public class MainActivity extends AppCompatActivity implements LocationListener{

    // Задания на ближайшую неделю
    // TODO: 1. Реализовать поиск пользователя по геолокации
    // TODO: 2. Написать код, позволяющий устанавливать метки на карте



    private MapView _mapview;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private AlertDialog.Builder _ad;
    private RotationGestureOverlay _rotationGestureOverlay;
    private GeoPoint _currentLocation;
    private LocationListener _locationListener;
    private LocationManager _locationManager;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Создание и инициализация карт
        super.onCreate(savedInstanceState);

        // Проверка разрешений
        if ((ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) &&
                (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) &&
                (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
            // Permission is not granted
            requestPermission();
        } else {
            onAllGood();
        }
}
//   Метод для получения необходимых разрешений
    protected void requestPermission()
    {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                MY_PERMISSIONS_REQUEST_LOCATION);
    }

//    Обработка разрешений, которые дал пользователь
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults)
    {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            _ad = new AlertDialog.Builder(this);
            _ad.setTitle("Отказ в получении геоданных");
            _ad.setMessage("Для работы приложения необходимо разрешить определять ваше местоположение");
            _ad.setPositiveButton("Выдать ращрешение", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    requestPermission();
                }
            });
            _ad.create()
                    .show();
        }
        else
                {
                // Permission has already been granted
                onAllGood();
                }
    }
    public void onResume(){
        super.onResume();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        _mapview.onResume(); //needed for compass, my location overlays, v6.0.0 and up
    }

    public void onPause(){
        super.onPause();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().save(this, prefs);
        _mapview.onPause();  //needed for compass, my location overlays, v6.0.0 and up
    }

    //Когда разрешения получены, выполняется этот метод
    @SuppressLint("MissingPermission")
    protected void onAllGood()
    {
        setupMap();

        IMapController mapController = _mapview.getController();
        mapController.setZoom(3.5);
        Bundle savedInstanceState = null;
        super.onCreate(savedInstanceState);
        _locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                _currentLocation = new GeoPoint(location);
                displayMyCurrentLocationOverlay();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        _locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        _locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, _locationListener);
        Location location = _locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if( location != null ) {
            _currentLocation = new GeoPoint(location.getLatitude(), location.getLongitude());
        }

    }
//    Метод инициализации карты
    private void setupMap()
    {
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        setContentView(R.layout.activity_main);
        _mapview =(MapView)findViewById(R.id.map);

        _mapview.setTileSource(TileSourceFactory.MAPNIK);
        _mapview.setMultiTouchControls(true);

//        Настраиваем "поворот" карт
        _rotationGestureOverlay = new RotationGestureOverlay(ctx, _mapview);
        _rotationGestureOverlay.setEnabled(true);
        _mapview.getOverlays().add(this._rotationGestureOverlay);
    }

//    По нажатию на кнопку "Найти себя" на карте отображается положение пользователя
//    TODO: реализовать поиск пользователя по нажатию кнопки
    public void onGPSButtonClick(View view)
    {
        Log.d("Geolocation", "Definition of user location...");
    }

    @Override
    public void onLocationChanged(Location location) {
        _currentLocation = new GeoPoint(location);
        displayMyCurrentLocationOverlay();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
    private void displayMyCurrentLocationOverlay() {
        if( _currentLocation != null) {
            ArrayItemizedOverlay currentLocationOverlay = null;
            if( currentLocationOverlay == null ) {
                currentLocationOverlay = new ArrayItemizedOverlay(myLocationMarker);
                myCurrentLocationOverlayItem = new OverlayItem(currentLocation, "My Location", "My Location!");
                currentLocationOverlay.addItem(myCurrentLocationOverlayItem);
                mapView.getOverlays().add(currentLocationOverlay);
            } else {
                myCurrentLocationOverlayItem.setPoint(currentLocation);
                currentLocationOverlay.requestRedraw();
            }
            mapView.getController().setCenter(currentLocation);
        }
    }
}