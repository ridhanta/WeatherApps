package com.education.sirkel.weatherapps.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.education.sirkel.weatherapps.Component.ColorBackground;
import com.education.sirkel.weatherapps.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button loginBtn;
    private Button createBtn;
    private Button changeBg;
    private TextView emailText;
    private TextView passText;
    private ConstraintLayout constraintLayout;
    private ColorBackground colorBackground;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseUser mUser;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Set Up Ui
        loginBtn = (Button) findViewById(R.id.loginBtn);
        createBtn = (Button) findViewById(R.id.createAcc);
        changeBg = (Button) findViewById(R.id.changeBackgroundLogin);
        emailText = (TextView) findViewById(R.id.emailTextLogin);
        passText = (TextView) findViewById(R.id.passTextLogin);
        constraintLayout = (ConstraintLayout) findViewById(R.id.mainLayout);

        mAuth = FirebaseAuth.getInstance();

        loginBtn.setOnClickListener(this);
        changeBg.setOnClickListener(this);
        createBtn.setOnClickListener(this);

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mUser = firebaseAuth.getCurrentUser();

                if (mUser != null){
                    startActivity(new Intent(MainActivity.this,WeatherActivity.class));
                    finish();
                }
                else {
                    Toast.makeText(MainActivity.this,"Not Signed in",Toast.LENGTH_LONG).show();
                }
            }
        };

    }

    @Override
    public void onClick(View view) {
        //untuk pindah dari activity ke activity lainnya
        switch (view.getId()){
            case R.id.loginBtn:

                //if ((emailText.getText != "") && (passText.getText != ""))
                if (!TextUtils.isEmpty(emailText.getText()) &&
                        !TextUtils.isEmpty(passText.getText())){

                    login(emailText.getText().toString(),passText.getText().toString());

                }
                else {
                    Toast.makeText(MainActivity.this, "Please Insert Your Email And Password",
                            Toast.LENGTH_LONG).show();
                }

                break;
            case R.id.changeBackgroundLogin:
                //String warnaString = "#EA80FC";
                //int warna = Color.parseColor(warnaString);
                colorBackground = new ColorBackground(); //Jangan lupa
                int color = colorBackground.getColor();
                constraintLayout.setBackgroundColor(color);
                break;
            case R.id.createAcc:
                Intent goToRegister = new
                        Intent(MainActivity.this,RegisterClass.class);
                startActivity(goToRegister);
                break;

        }


    }

    private void login(String email, String pwd) {

        mAuth.signInWithEmailAndPassword(email,pwd)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()){
                            Intent goToWeather = new
                                    Intent(MainActivity.this,WeatherActivity.class);
                            startActivity(goToWeather);
                        }else {
                            Toast.makeText(getApplicationContext(),"Login Failed"
                                    ,Toast.LENGTH_LONG).show();
                        }

                    }
                });

    }


    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthStateListener != null){
            mAuth.removeAuthStateListener(mAuthStateListener);
        }
    }

}
