package com.education.sirkel.weatherapps.Activities;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.education.sirkel.weatherapps.Component.Constants;
import com.education.sirkel.weatherapps.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WeatherActivity extends AppCompatActivity {

    private TextView kotaCuaca;
    private TextView mainCuaca;
    private TextView deskCuaca;
    private TextView tempMin;
    private TextView tempMax;
    private RequestQueue queue;
    private ImageView iconCuaca;
    private Toolbar toolbar;
    private ImageButton goToList;

    private AlertDialog.Builder alerDialogBuilder;
    private AlertDialog dialog;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        kotaCuaca = (TextView) findViewById(R.id.kotaWeather);
        mainCuaca = (TextView) findViewById(R.id.mainWeather);
        deskCuaca = (TextView) findViewById(R.id.descWeather);
        tempMin = (TextView) findViewById(R.id.tempMinWeather);
        tempMax = (TextView) findViewById(R.id.tempMaxWeather);
        iconCuaca = (ImageView) findViewById(R.id.imageWeather);
        goToList = (ImageButton) findViewById(R.id.imageButton);

        toolbar = (Toolbar) findViewById(R.id.toolbarId);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        String userId = mAuth.getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseRef = mDatabase.getReference().child("Users").child(userId).child("firstname");

//        mDatabaseRef.setValue("Hello Firebase");
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                Toast.makeText(WeatherActivity.this,"Hi, "+ value,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        queue = Volley.newRequestQueue(this);

        getWeather("7035024");

        goToList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogInput();
            }
        });

    }

    private void showDialogInput() {
        alerDialogBuilder = new AlertDialog.Builder(this);
        final View view = getLayoutInflater().inflate(R.layout.dialog_view,null);
        Button sbmBtn = (Button) view.findViewById(R.id.submitDialog);
        RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.radioGrup);

        alerDialogBuilder.setView(view);
        dialog = alerDialogBuilder.create();
        dialog.show();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                RadioButton radioButton = (RadioButton) view.findViewById(id);

                switch (radioButton.getId()){
                    case R.id.kotaBandung:
                        getWeather("7035024");
                        break;
                    case R.id.kotaJakarta:
                        getWeather("1642911");
                        break;
                    case R.id.kotaSurabaya:
                        getWeather("8018250");
                        break;
                    case R.id.kotaSemarang:
                        getWeather("1627896");
                        break;
                    case R.id.kotaBogor:
                        getWeather("1648473");
                        break;
                }
            }
        });

        sbmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Untuk membuat menu
        getMenuInflater().inflate(R.menu.menu_toolbar,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.logout){
            mAuth.signOut();
            startActivity(new Intent(WeatherActivity.this, MainActivity.class));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void getWeather(String id) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                Constants.LEFT_URL + id + Constants.RIGHT_URL, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray list = response.getJSONArray("list");

                    if (list.length() > 0){
                        JSONObject list0 = list.getJSONObject(0);
                        JSONObject main = list0.getJSONObject("main");

                        tempMin.setText(main.getString("temp_min"));
                        tempMax.setText(main.getString("temp_max"));

                        JSONObject city = response.getJSONObject("city");
                        kotaCuaca.setText(city.getString("name"));

                        JSONArray weather = list0.getJSONArray("weather");
                        JSONObject weather0 = weather.getJSONObject(0);

                        mainCuaca.setText(weather0.getString("main"));
                        deskCuaca.setText(weather0.getString("description"));

                        //image

                        String iconC = weather0.getString("icon");

                        Picasso.with(WeatherActivity.this)
                                .load(Constants.IMAGE_URL + iconC +".png")
                                .placeholder(android.support.v4.R.drawable.notification_bg_low_pressed)
                                .into(iconCuaca);


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(WeatherActivity.this,"Json Error", Toast.LENGTH_LONG)
                        .show();
            }
        });
        queue.add(jsonObjectRequest);

    }


}
