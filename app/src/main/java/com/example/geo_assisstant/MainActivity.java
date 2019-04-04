package com.example.geo_assisstant;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PointF;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.layers.ObjectEvent;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.CompositeIcon;
import com.yandex.mapkit.map.IconStyle;
import com.yandex.mapkit.map.RotationType;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.mapkit.user_location.UserLocationLayer;
import com.yandex.mapkit.user_location.UserLocationObjectListener;
import com.yandex.mapkit.user_location.UserLocationView;
import com.yandex.runtime.image.ImageProvider;

public class MainActivity extends AppCompatActivity implements UserLocationObjectListener {


    private MapView _mapview;
    private UserLocationLayer _userLocationLayer;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //onRequestPermissionsResult();

        // Создание карты и всякая хуйня
        super.onCreate(savedInstanceState);
        MapKitFactory.setApiKey("6510675d-95b1-40ed-9948-689720a64118");
        MapKitFactory.initialize(this);
        setContentView(R.layout.activity_main);
        _mapview = (MapView)findViewById(R.id.mapview);

        // Запрос разрешений

        checkLocationPermission();
        _userLocationLayer = _mapview.getMap().getUserLocationLayer();
        _mapview.getMap().move(
                new CameraPosition(new Point(0, 0), 16, 0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, 2),
                null);
        _mapview.getMap().setRotateGesturesEnabled(false);
        _mapview.getMap().move(new CameraPosition(new Point(0, 0), 16, 0, 0));
        _userLocationLayer.setEnabled(true);
        _userLocationLayer.setHeadingEnabled(true);
        _userLocationLayer.setObjectListener(this);
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
    @Override
    public void onObjectAdded(UserLocationView userLocationView) {
        _userLocationLayer.setAnchor(
                new PointF((float)(_mapview.getWidth() * 0.5), (float)(_mapview.getHeight() * 0.5)),
                new PointF((float)(_mapview.getWidth() * 0.5), (float)(_mapview.getHeight() * 0.83)));

        userLocationView.getArrow().setIcon(ImageProvider.fromResource(
                this, R.drawable.user_arrow));

        CompositeIcon pinIcon = userLocationView.getPin().useCompositeIcon();

        /* Это иконка около местоположения юзверя, но смотрится она хуёво, поэтому убрал  (скрин https://pp.userapi.com/c851520/v851520494/f1d60/bBiJVwiCYMk.jpg)
        pinIcon.setIcon(
                "icon",
                ImageProvider.fromResource(this, R.drawable.user_arrow),
                new IconStyle().setAnchor(new PointF(0f, 0f))
                        .setRotationType(RotationType.ROTATE)
                        .setZIndex(0f)
                        .setScale(1f)
        );
*/
        pinIcon.setIcon(
                "pin",
                ImageProvider.fromResource(this, R.drawable.search_result),
                new IconStyle().setAnchor(new PointF(0.5f, 0.5f))
                        .setRotationType(RotationType.ROTATE)
                        .setZIndex(1f)
                        .setScale(0.5f)
        );

        userLocationView.getAccuracyCircle().setFillColor(Color.argb(50,51,255,255));
    }

    @Override
    public void onObjectRemoved(UserLocationView view) {
    }

    @Override
    public void onObjectUpdated(UserLocationView view, ObjectEvent event) {
    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                new AlertDialog.Builder(this)
                        .setTitle("Разрешение на получение геоданных")
                        .setMessage("Для работы приложения предоставьте разрешение на определение вашего местоположения")
                        .setPositiveButton("Разрешить", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // разрешение получено
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {


                        //Сюда можно воткнуть действия после выдачи разрешений
                        //locationManager.requestLocationUpdates(provider, 400, 1, this);
                    }

                } else {
                    // отказ в выдаче разрешений
                    new AlertDialog.Builder(this)
                            .setTitle("Отказ в получении геоданных")
                            .setMessage("Для работы приложения необходимо разрешить определять ваше местоположение")
                            .setPositiveButton("Закрыть приложение", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    finish();
                                    System.exit(0);
                                }
                            })
                            .create()
                            .show();
                }
            }
            return;
        }

    }
}

