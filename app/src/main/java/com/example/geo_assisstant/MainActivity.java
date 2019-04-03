package com.example.geo_assisstant;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.mapview.MapView;

public class MainActivity extends AppCompatActivity {


    private MapView _mapview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapKitFactory.setApiKey("6510675d-95b1-40ed-9948-689720a64118");
        MapKitFactory.initialize(this);
        setContentView(R.layout.activity_main);

        _mapview = (MapView)findViewById(R.id.mapview);
        _mapview.getMap().move(
                new CameraPosition(new Point(51.670209, 39.205196), 11.0f, 0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, 0),
                null);

    }

    @Override
    protected void onStop()
    {
        super.onStop();
        _mapview.onStop();
        MapKitFactory.getInstance().onStop();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        _mapview.onStart();
        MapKitFactory.getInstance().onStart();
    }


}
