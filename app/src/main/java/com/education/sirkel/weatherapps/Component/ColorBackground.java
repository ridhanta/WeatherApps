package com.education.sirkel.weatherapps.Component;

import android.graphics.Color;

import java.util.Random;

/**
 * Created by user on 1/6/2018.
 */

public class ColorBackground {

    //Array string warna
    public String[] mColors = {
            "#ffEBEE","#F8BBD0","#B39DDB","#B3E5FC","#00E676"
    };

    //Fungsi ngambil warna
    public int getColor(){
        Random random = new Random();
        int randomAngka = random.nextInt(mColors.length);
        int color = Color.parseColor(mColors[randomAngka]);
        return color;
    }
}
