package com.education.sirkel.weatherapps.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.education.sirkel.weatherapps.R;

import gr.net.maroulis.library.EasySplashScreen;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View easySplashScreenView = new EasySplashScreen(SplashScreen.this)
                .withFullScreen()
                .withTargetActivity(MainActivity.class)
                .withSplashTimeOut(4000)
                .withBackgroundResource(android.R.color.holo_blue_dark)
                .withHeaderText("")
                .withFooterText("Copyright 2018")
                .withBeforeLogoText("")
                .withLogo(R.drawable.logoww

                )
                .withAfterLogoText("")
                .create();

        setContentView(easySplashScreenView);
    }
}
