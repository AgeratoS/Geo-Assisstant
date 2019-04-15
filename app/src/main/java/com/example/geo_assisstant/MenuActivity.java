package com.example.geo_assisstant;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class MenuActivity extends AppCompatActivity {
    private long backPressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }



//  Закрытие приложения
    public void onButtonExit(View view) {

        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            finishAffinity();
            android.os.Process.killProcess(android.os.Process.myPid());
        } else {
            Toast.makeText(getBaseContext(), "Нажмите еще раз для выхода из приложения", Toast.LENGTH_SHORT).show();
        }

        backPressedTime = System.currentTimeMillis();
    }

    public void onButtonGeoCreate(View view) {
        super.onBackPressed();
    }
}
