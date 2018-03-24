package com.example.lobo.radiomickyandonie;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import gr.net.maroulis.library.EasySplashScreen;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EasySplashScreen config = new EasySplashScreen(SplashScreen.this)
                .withFullScreen()
                .withTargetActivity(Login.class)
                .withSplashTimeOut(5000)
                .withBackgroundColor(Color.parseColor("#074E72"))
                .withLogo(R.mipmap.ic_launcher)
                .withHeaderText("Bienvenido")
                .withFooterText("Otro proyecto más de Honduras en sus manos");

        config.getHeaderTextView().setTextColor(android.graphics.Color.WHITE);
        config.getFooterTextView().setTextColor(android.graphics.Color.WHITE);

        View view = config.create();

        setContentView(view);
    }
}
