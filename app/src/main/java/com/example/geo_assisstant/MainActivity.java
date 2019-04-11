package com.example.geo_assisstant;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.yandex.mapkit.user_location.UserLocationLayer;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

public class MainActivity extends AppCompatActivity  {


    private org.osmdroid.views.MapView _mapview = null;
    private UserLocationLayer _userLocationLayer;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private AlertDialog.Builder _ad;
    private MyLocationNewOverlay mLocationOverlay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Создание карты и всякая хуйня
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _mapview = (org.osmdroid.views.MapView) findViewById(R.id.map);
        _mapview.setTileSource(TileSourceFactory.MAPNIK);
        _mapview.setBuiltInZoomControls(true);
        _mapview.setMultiTouchControls(true);
        // Проверка разрешений
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            requestPermission();
        }
        else
        {
            onAllGood();
        }
    }
   /* @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        _mapview = new MapView(inflater.getContext(), 256, getContext());
        return _mapview;
    }*/
    protected void requestPermission()
    {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
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
    protected void onAllGood()
    {
        IMapController mapController = _mapview.getController();
        mapController.setZoom(9.5);
        GeoPoint startPoint = new GeoPoint(48.8583, 2.2944);
        mapController.setCenter(startPoint);
        /*Context context = null;
        this.mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(context),_mapview);
        this.mLocationOverlay.enableMyLocation();
        _mapview.getOverlays().add(this.mLocationOverlay);*/
    }
/*
    public void onGPSButtonClick(View view) {
        _mapview.getMap().move(
                new CameraPosition(_userLocationLayer.cameraPosition().getTarget(), 16.0f, 0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, 1),
                null);
    }*/
}