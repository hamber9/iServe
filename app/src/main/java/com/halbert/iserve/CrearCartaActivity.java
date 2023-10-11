package com.halbert.iserve;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CrearCartaActivity extends AppCompatActivity {

    FirebaseDatabase mDatabase;
    DatabaseReference mRef, mRefCartes;
    FirebaseStorage mStorage;
    Spinner spPrimers, spSegons, spTercers;
    List<Recepta> receptalist = new ArrayList<Recepta>();
    List<String> receptalist_nom = new ArrayList<String>();
    ArrayList<String> listKeys = new ArrayList<String>();
    Recepta primer, segon, tercer;
    List<Recepta> segons = new ArrayList<Recepta>();
    List<Recepta> tercers = new ArrayList<Recepta>();
    Carta carta;
    String nomCarta, comentarisCarta;

    Recepta actual;

    Button crearRecepta;
    EditText nom, comentaris;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_carta);

        crearRecepta = findViewById(R.id.buttonNewCarta);
        spPrimers= findViewById(R.id.spinner1);
        spSegons = findViewById(R.id.spinner2);
        spTercers = findViewById(R.id.spinner3);
        nom = findViewById(R.id.editTextTextNomCarta);
        comentaris = findViewById(R.id.editTextTextComentarisCarta);

        String [] test = {"1","2","3"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, receptalist_nom);


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Receptes");

        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                Recepta recepta = snapshot.getValue(Recepta.class);
                String name = recepta.getNom();
                receptalist.add(recepta);
                receptalist_nom.add(recepta.getNom());
                listKeys.add(snapshot.getKey());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                adapter.notifyDataSetChanged();
            }
        });

        spPrimers.setAdapter(adapter);
        spSegons.setAdapter(adapter);
        spTercers.setAdapter(adapter);

        spPrimers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                primer = receptalist.get(i);
                Toast.makeText(getApplicationContext(), primer.getNom(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spSegons.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                segon = receptalist.get(i);
                Toast.makeText(getApplicationContext(), segon.getNom(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spTercers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                tercer = receptalist.get(i);
                Toast.makeText(getApplicationContext(), tercer.getNom(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        crearRecepta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // if (!(nomCarta.isEmpty() && comentarisCarta.isEmpty()))
                nomCarta = nom.getText().toString();
                comentarisCarta = comentaris.getText().toString();
                if(!(primer == null && segon == null && tercer== null && nom == null && comentaris == null)){
                    carta = new Carta(nomCarta, primer, segon, tercer, comentarisCarta);
                    mRefCartes = FirebaseDatabase.getInstance().getReference().child("Cartes");
                    mRefCartes.push().setValue(carta);

                }
            }
        });


    }
}