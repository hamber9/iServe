package com.halbert.iserve;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class AuthActivity extends AppCompatActivity {

    private FirebaseAnalytics mFirebaseAnalytics;
    EditText mEmail, mPassword;
    Button loginButton, registerButton;
    FirebaseAuth fAuth;
    String email, password, categoria, currentcategoria, key;
    Spinner spCategoria;
    FirebaseDatabase mDatabase;
    DatabaseReference mRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        // Analytics Event
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putString("message", "Integracion de Firebase completa");
        mFirebaseAnalytics.logEvent("InitScreen", bundle);

        // Register fields
        mEmail = findViewById(R.id.emailEditText);
        mPassword = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.logInButton);
        registerButton = findViewById(R.id.signUpButton);
        spCategoria = findViewById(R.id.spinnerCategoria);
        String [] categories = {"Cuiner","Cambrer"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        spCategoria.setAdapter(adapter);


        fAuth = FirebaseAuth.getInstance();

        if (fAuth.getCurrentUser() != null){
            Toast.makeText(this, "Already loged", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            finish();
        }



        loginButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                email = mEmail.getText().toString().trim();
                password = mPassword.getText().toString().trim();
                categoria = spCategoria.getSelectedItem().toString();

                if (TextUtils.isEmpty(email)) {
                    mEmail.setError("Es necessita un email");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    mPassword.setError("Es necessita una contrasenya");
                    return;
                }

                if (password.length() < 6) {
                    mPassword.setError("La contrasenya té que tindre mínim 6 caracters.");
                }

                fAuth = FirebaseAuth.getInstance();

                if (fAuth.getCurrentUser() == null) {

                    FirebaseAuth.getInstance().signOut();

                    mDatabase = FirebaseDatabase.getInstance();
                    mRef = mDatabase.getReference().child("Usuaris");

                    Query Qemail = mDatabase.getReference().child("Usuaris").orderByChild("email").equalTo(email);
                    Qemail.addChildEventListener(new ChildEventListener() {


                        @Override
                        public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                            {
                                if(snapshot.exists()){

                                        Usuari usuari = snapshot.getValue(Usuari.class);
                                        usuari.setKey(snapshot.getKey());
                                        currentcategoria = usuari.getCategoria();


                                        key = usuari.getKey();

                                        if (categoria.equals(currentcategoria)){
                                            sign();
                                        }

                                        mDatabase = FirebaseDatabase.getInstance();
                                        mRef = mDatabase.getReference("Usuaris").child(key).child("categoria");


                                }
                            }
                        }

                        @Override
                        public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                        }

                        @Override
                        public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {

                        }

                        @Override
                        public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    });



                }
            }
        });


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                email = mEmail.getText().toString().trim();
                password = mPassword.getText().toString().trim();
                categoria = spCategoria.getSelectedItem().toString();

                if (TextUtils.isEmpty(email)) {
                    mEmail.setError("Es necessita un email");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    mPassword.setError("Es necessita una contrasenya");
                    return;
                }

                if (password.length() < 6) {
                    mPassword.setError("La contrasenya té que tindre mínim 6 caracters.");
                }


                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Map<String, Object> usuariMap = new HashMap<>();
                            usuariMap.put("email", email);
                            usuariMap.put("categoria", categoria);
                            mDatabase = FirebaseDatabase.getInstance();
                            mDatabase.getReference().child("Usuaris").push().setValue(usuariMap);
                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));

                            Toast.makeText(AuthActivity.this, "Usuari creat", Toast.LENGTH_SHORT).show();
                            //startActivity(new Intent(getApplicationContext(), HomeActivity.class));

                        } else {
                            Toast.makeText(AuthActivity.this, "Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });

            }
        });





    }

    public void sign (){
        fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(AuthActivity.this, "Logged in successfuly", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                } else {
                    Toast.makeText(AuthActivity.this, "Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}