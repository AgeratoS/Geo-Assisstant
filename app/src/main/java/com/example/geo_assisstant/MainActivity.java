package com.example.geo_assisstant;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import org.mapsforge.map.layer.download.tilesource.TileSource;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

public class MainActivity extends AppCompatActivity {

    // Задания на ближайшую неделю
    // TODO: 1. Реализовать поиск пользователя по геолокации
    // TODO: 2. Написать код, позволяющий устанавливать метки на карте



    private MapView _mapview;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private AlertDialog.Builder _ad;
    RotationGestureOverlay _rotationGestureOverlay;

    private MyLocationNewOverlay mLocationOverlay;


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

   /* @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        _mapview = new MapView(inflater.getContext(), 256, getContext());
        return _mapview;
    }*/

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
    protected void onAllGood()
    {
        setupMap();

        IMapController mapController = _mapview.getController();
        mapController.setZoom(3.5);
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
    public void onLocationButtonClick(View view)
    {
        Log.d("Geolocation", "Definition of user location...");
    }

//    Переход в меню по нажатию кнопки
    public void onMenuButtonClick(View view) {
        Intent intent =new Intent(MainActivity.this,MenuActivity.class);
        startActivity(intent);
    }
}