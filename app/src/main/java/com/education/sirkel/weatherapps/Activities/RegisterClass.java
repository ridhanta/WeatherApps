package com.education.sirkel.weatherapps.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.education.sirkel.weatherapps.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterClass extends AppCompatActivity {

    private EditText firstName;
    private EditText lastName;
    private EditText email;
    private EditText password;
    private Button submit;
    private ProgressDialog mProgressDialog;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_class);

        firstName = (EditText) findViewById(R.id.firstNameReg);
        lastName = (EditText) findViewById(R.id.lastNameReg);
        email = (EditText) findViewById(R.id.emailTextReg);
        password = (EditText) findViewById(R.id.passTextReg);
        submit = (Button) findViewById(R.id.submitReg);

        mProgressDialog = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference().child("Users");

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String fName = firstName.getText().toString();
                final String lName = lastName.getText().toString();
                final String em = email.getText().toString();
                final String pw = password.getText().toString();

                if (!TextUtils.isEmpty(fName)
                        && !TextUtils.isEmpty(lName)
                        && !TextUtils.isEmpty(em)
                        && !TextUtils.isEmpty(pw)){

                    mProgressDialog.setTitle("Register");
                    mProgressDialog.setMessage("Creating Account");
                    mProgressDialog.show();

//                    mAuth.createUserWithEmailAndPassword(em,pw).addOnSuccessListener
//                            (new OnSuccessListener<AuthResult>() {
//                        @Override
//                        public void onSuccess(AuthResult authResult) {
//                            String userid = mAuth.getCurrentUser().getUid();
//                            DatabaseReference currentDb = mDatabaseReference.child(userid);
//
//                            currentDb.child("firstname").setValue(fName);
//                            currentDb.child("lastname").setValue(lName);
//                            currentDb.child("email").setValue(em);
//                            currentDb.child("password").setValue(pw);
//
//                            mProgressDialog.dismiss();
//
//                            //Go to WeatherActivity
//
//                            Intent intent = new Intent(RegisterClass.this,WeatherActivity.class);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            startActivity(intent);
//
//                        }
//                    });
                    mAuth.createUserWithEmailAndPassword(em,pw).addOnCompleteListener
                            (new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                String userid = mAuth.getCurrentUser().getUid();
                                DatabaseReference currentDb = mDatabaseReference.child(userid);

                                currentDb.child("firstname").setValue(fName);
                                currentDb.child("lastname").setValue(lName);
                                currentDb.child("email").setValue(em);
                                currentDb.child("password").setValue(pw);

                                mProgressDialog.dismiss();

                                //Go to WeatherActivity

                                Intent intent = new Intent(RegisterClass.this, WeatherActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                            else {
                                mProgressDialog.dismiss();
                                Toast.makeText(RegisterClass.this,"Register Failed",Toast.LENGTH_LONG)
                                        .show();
                            }
                        }
                    });

                }
                else {
                    Toast.makeText(RegisterClass.this,"Please Insert Your...",Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

    }
}
