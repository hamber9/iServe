package com.halbert.iserve;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class BegudaActivity extends AppCompatActivity implements BegudaAdapter.OnItemClickListener {

    ListView listview;
    ArrayList<String> beguda_list = new ArrayList<>();
    FirebaseDatabase mDatabase;
    DatabaseReference mRef;
    FirebaseStorage mStorage;
    ImageButton imageButton;



    RecyclerView recyclerView;
    BegudaAdapter begudaAdapter;
    List<Beguda> begudalist;

    Button CrearBeguda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beguda);


        CrearBeguda = findViewById(R.id.buttonCrearBeguda);

        Button Llistar;
        Llistar = findViewById(R.id.llistar_beguda_id);
        //imageButton = findViewById(R.id.imageButton);

        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference().child("Begudes");
        mStorage = FirebaseStorage.getInstance();


        CrearBeguda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), CrearBegudaActivity.class));
            }
        });

        Llistar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                construirRecycle();
            }
        });


    }

    private void construirRecycle() {

        setContentView(R.layout.activity_retrive_data_in_recycler_view);
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference().child("Begudes");
        mStorage = FirebaseStorage.getInstance();

        recyclerView = findViewById(R.id.recyclerview_id);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //-----> recyclerView.setLayoutManager(new GridLayoutManager(this, 4));

        begudalist = new ArrayList<Beguda>();
        begudaAdapter = new BegudaAdapter(BegudaActivity.this, begudalist);
        recyclerView.setAdapter(begudaAdapter);

        begudaAdapter.setOnItemClickListener(BegudaActivity.this);



        mRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                Beguda beguda = snapshot.getValue(Beguda.class);
                beguda.setKey(snapshot.getKey());
                begudalist.add(beguda);
                begudaAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                begudaAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {
                begudaAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onItemClick(int position) {
        Toast.makeText(this, "Normal click at position: " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onModifyClick(int position) {
        Beguda selectedItem = begudalist.get(position);
        String selectedKey = selectedItem.getKey();
        StorageReference begudaRef = mStorage.getReferenceFromUrl(selectedItem.getImage());
        String nom = selectedItem.getNom();
        String descripcio = selectedItem.getDescripcio();
        String preu = selectedItem.getPreu();
        String image = selectedItem.getImage();

        Intent intent = new Intent(this, CrearBegudaActivity.class);
        intent.putExtra("nom", nom);
        intent.putExtra("descripcio", descripcio);
        intent.putExtra("preu", preu);
        intent.putExtra("imatge", image);
        intent.putExtra("key", selectedKey);

        startActivity(intent);


        Toast.makeText(this, "Modify position: " + nom, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeleteClick(int position) {

        Beguda selectedItem = begudalist.get(position);
        String selectedKey = selectedItem.getKey();
        StorageReference begudaRef = mStorage.getReferenceFromUrl(selectedItem.getImage());
        begudaRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                mRef.child(selectedKey).removeValue();
                begudalist.remove(position);
                Toast.makeText(BegudaActivity.this, "Beguda eliminada", Toast.LENGTH_SHORT).show();
            }
        });
    }

    }
