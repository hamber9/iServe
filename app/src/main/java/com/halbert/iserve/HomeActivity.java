package com.halbert.iserve;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import org.jetbrains.annotations.NotNull;

public class HomeActivity extends AppCompatActivity {

    String currentUserId, currentUserEmail,currentcategoria,  key;
    Button sortir,GoProducte, GoPlat, GoBeguda, GoCarta;
    FirebaseDatabase mDatabase;
    DatabaseReference mRef;
    FirebaseAuth fAuth;
    RelativeLayout layoutHome;
    TextView tvUsuari;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        layoutHome = findViewById(R.id.layoutHome);

        sortir = findViewById(R.id.signUpButton);
        GoProducte = findViewById(R.id.ProductesButton);

        GoPlat = findViewById(R.id.PlatsButton);

        GoBeguda = findViewById(R.id.buttonBegudes);

        GoCarta = findViewById(R.id.buttonCarta);

        fAuth = FirebaseAuth.getInstance();

        currentUserId = fAuth.getCurrentUser().getUid();
        currentUserEmail = fAuth.getCurrentUser().getEmail();

        tvUsuari = findViewById(R.id.textViewUsuari);

        tvUsuari.setText(fAuth.getCurrentUser().getEmail());


        mDatabase = FirebaseDatabase.getInstance();
       // mRef = mDatabase.getReference().child("Usuaris");

        Query Qemail = mDatabase.getReference().child("Usuaris").orderByChild("email").equalTo(currentUserEmail);
        Qemail.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                if(snapshot.exists()){

                    Usuari usuari = snapshot.getValue(Usuari.class);
                    usuari.setKey(snapshot.getKey());
                    currentcategoria = usuari.getCategoria();


                    key = usuari.getKey();

                    if (currentcategoria.equals("Cuiner")){
                        GoBeguda.setVisibility(View.GONE);
                        layoutHome.setBackground(ContextCompat.getDrawable(HomeActivity.this, R.drawable.cuiner));
                    }

                    if(currentcategoria.equals("Cambrer")){
                        layoutHome.setBackground(ContextCompat.getDrawable(HomeActivity.this, R.drawable.cambrera));
                        GoProducte.setVisibility(View.GONE);
                        GoPlat.setVisibility(View.GONE);
                        GoCarta.setVisibility(View.GONE);
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



        //logout
        sortir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fAuth != null){

                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(getApplicationContext(), AuthActivity.class));
                        finish();

                }
            }
        });

        //GoTo Producte
        GoProducte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ProducteActivity.class));
            }
        });

        GoPlat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), PlatActivity.class));
            }
        });

        GoBeguda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), BegudaActivity.class));
            }
        });

        GoCarta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), CartaActivity.class));
            }
        });
    }
    @Override
    public void onBackPressed()
    {

    }

}