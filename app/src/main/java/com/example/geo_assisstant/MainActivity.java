package com.example.geo_assisstant;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.mylocation.DirectedLocationOverlay;

public class MainActivity extends AppCompatActivity implements LocationListener {

    MapView map = null;
    private MapView osm;
    private MapController _mapControl;
    private LocationManager locationManager;
    private CompassOverlay compassOverlay;
    private DirectedLocationOverlay locationOverlay;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private boolean _followMe = true;
    private boolean _locationOverAdded = false;
    private GeoPoint _myLocation;
    private Marker _myLocationMarker;
    private AlertDialog.Builder _ad;



    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        osm = findViewById(R.id.map);
        osm.setTileSource(TileSourceFactory.MAPNIK);
        osm.setBuiltInZoomControls(true);
        osm.setMultiTouchControls(true);
        _mapControl = (MapController) osm.getController();
        _mapControl.setZoom(8);



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
        //onde mostra a imagem do mapa

        /*
        _myLocationMarker.setPosition(new GeoPoint(_myLocation.getLatitude(), _myLocation.getLongitude()));;
        _mapControl.animateTo((IGeoPoint) _myLocationMarker);
        addMarker(center);
        */

    }

    @SuppressLint({"ClickableViewAccessibility", "MissingPermission"})
    private void onAllGood() {



        /* grade no mapa
        LatLonGridlineOverlay2 overlay = new LatLonGridlineOverlay2();
        osm.getOverlays().add(overlay);
        */

        osm.setMapListener(new MapListener() {
            @Override
            public boolean onScroll(ScrollEvent event) {
                Log.i("Script", "onScroll()");
                return false;
            }

            @Override
            public boolean onZoom(ZoomEvent event) {
                Log.i("Script", "onZoom()");
                return false;
            }
        });
/*
        findViewById(R.id.mapLocationButton).setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    ImageButton view = (ImageButton) v;
                    view.getBackground().setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
                    v.invalidate();
                    break;
                }
                case MotionEvent.ACTION_UP:

                    // Your action here on button click
                    _followMe = !_followMe;

                case MotionEvent.ACTION_CANCEL: {
                    ImageButton view = (ImageButton) v;
                    view.getBackground().clearColorFilter();
                    view.invalidate();
                    break;
                }
            }
            return true;
        });*/

        addOverlays();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, 0f, this);

    }

    protected void requestPermission()
    {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                MY_PERMISSIONS_REQUEST_LOCATION);
    }
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

    public void addOverlays() {
        _myLocationMarker = new Marker(osm);
        _myLocationMarker.setIcon(getResources().getDrawable(R.drawable.search_result));
        _myLocationMarker.setImage(getResources().getDrawable(R.drawable.search_result));


    }

    public void addMarker (GeoPoint center){
        Marker marker = new Marker(osm);
        marker.setPosition(center);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        marker.setIcon(getResources().getDrawable(R.drawable.tapimage));
        osm.getOverlays().clear();
        osm.getOverlays().add(marker);
        osm.invalidate();
        marker.setTitle("Casa do Josileudo");
    }

    public void onResume() {
        super.onResume();

    }


    public void onPause(){
        super.onPause();
    }
    /*
        @Override
        public void onLocationChanged(Location location) {
            GeoPoint center = new GeoPoint(location.getLatitude(), location.getLongitude());

            mc.animateTo(center);
            addMarker(center);

        }*/

    @Override
    public void onLocationChanged(Location location) {
        _myLocation = new GeoPoint(location.getLatitude(),location.getLongitude());
        _myLocationMarker.setPosition(_myLocation);
        if (!_locationOverAdded) {
            osm.getOverlayManager().add(_myLocationMarker);
            _locationOverAdded = true;
        }

        IMapController mapController = osm.getController();
        mapController.setZoom(15);
        mapController.setCenter(_myLocation);
        osm.getController().animateTo(_myLocation);

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
    @Override
    public void onDestroy(){
        super.onDestroy();//`enter code here`
        if (locationManager != null){
            locationManager.removeUpdates(this);
        }
    }

//    По нажатию на кнопку "Найти себя" на карте отображается положение пользователя
//    TODO: реализовать поиск пользователя по нажатию кнопки


    public void onLocationButtonClick(View view)
    {
        if (_followMe)
        {
            _followMe = false;
        }
        else
        {
            _followMe = true;
        }
        // Log.d("Geolocation", "Definition of user location...");
    }

    //    Переход в меню по нажатию кнопки
    public void onMenuButtonClick(View view) {
        Intent intent =new Intent(MainActivity.this,MenuActivity.class);
        startActivity(intent);
    }
} 	