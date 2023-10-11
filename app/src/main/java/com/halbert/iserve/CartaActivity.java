package com.halbert.iserve;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CartaActivity extends AppCompatActivity {

    Button crearCarta, llistarCarta;
    FirebaseDatabase mDatabase;
    DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carta);

    crearCarta = findViewById(R.id.buttonCrearCarta);
    llistarCarta = findViewById(R.id.llistar_carta_id);

        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference().child("Cartes");

    crearCarta.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivity(new Intent(getApplicationContext(), CrearCartaActivity.class));
        }
    });

    llistarCarta.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivity(new Intent(getApplicationContext(), CartaListActivity.class));
        }
    });

    }


}