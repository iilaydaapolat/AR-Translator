package com.example.artranslator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivityGiris extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    TextView login,signup,textView5;

    EditText emailText, passwordText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_giris);


        firebaseAuth = FirebaseAuth.getInstance();
        emailText = findViewById(R.id.emailTxt);
        passwordText = findViewById(R.id.passwordTxt);
        textView5=findViewById(R.id.textView5);
        textView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(MainActivityGiris.this,MainActivity.class);
                startActivity(intent);
            }
        });

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser != null){
            Intent intent = new Intent(MainActivityGiris.this, MainActivity.class);
            startActivity(intent);
            finish();
        }


    }
    public void signInClicked(View view){
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Intent intent = new Intent(MainActivityGiris.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivityGiris.this,e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
            }
        });
    }
    public void signUpClicked(View view){

        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(MainActivityGiris.this,"User Created",Toast.LENGTH_LONG).show();

                Intent intent = new Intent(MainActivityGiris.this, MainActivityGiris.class);
                startActivity(intent);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivityGiris.this,e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
            }
        });
    }

    }
