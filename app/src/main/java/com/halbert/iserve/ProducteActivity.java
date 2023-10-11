package com.halbert.iserve;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ArrayAdapter;
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
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ProducteActivity extends AppCompatActivity implements ProducteAdapter.OnItemClickListener {

    ListView listview;
    ArrayList<String> producte_list = new ArrayList<>();
    FirebaseDatabase mDatabase;
    DatabaseReference mRef;
    FirebaseStorage mStorage;
    ImageButton imageButton;



    RecyclerView recyclerView;
    ProducteAdapter producteAdapter;
    List<Producte> productelist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producte);

        Button CrearProducte;
        CrearProducte = findViewById(R.id.CrearProducteButton);
        //CrearProducte.setText(Crear);

        Button Llistar;
       Llistar = findViewById(R.id.llistar_id);
        //imageButton = findViewById(R.id.imageButton);

        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference().child("Productes");
        mStorage = FirebaseStorage.getInstance();

        Llistar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llistarProdRecycler();
            }
        });

        /*imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
            }
        });*/

        //LlistarProducte();





        //GoTo crear producte
        CrearProducte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), CrearProducteActivity.class));
            }
        });


    }

    private void llistarProdRecycler() {
        construirRecycle();
        //Intent intent = new Intent (ProducteActivity.this, RetriveDataInRecyclerView.class);
        //startActivity(intent);
    }

    private void construirRecycle(){

        setContentView(R.layout.activity_retrive_data_in_recycler_view);
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference().child("Productes");
        mStorage = FirebaseStorage.getInstance();

        recyclerView = findViewById(R.id.recyclerview_id);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //-----> recyclerView.setLayoutManager(new GridLayoutManager(this, 4));

        productelist = new ArrayList<Producte>();
        producteAdapter = new ProducteAdapter(ProducteActivity.this, productelist);
        recyclerView.setAdapter(producteAdapter);

        producteAdapter.setOnItemClickListener(ProducteActivity.this);



        mRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                Producte producte = snapshot.getValue(Producte.class);
                producte.setKey(snapshot.getKey());
                productelist.add(producte);
                producteAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                producteAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {
                producteAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


    }

    private void LlistarProducte() {

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ProducteActivity.this, R.layout.list_item, R.id.textview, producte_list);

        //listview = (ListView) findViewById(R.id.listview);
        //listview.setAdapter(adapter);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Productes");

        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                Producte producte = snapshot.getValue(Producte.class);
                String name = producte.getNom();
                producte_list.add(name);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                adapter.notifyDataSetChanged();
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

    @Override
    public void onItemClick(int position) {
        Toast.makeText(this, "Normal click at position: " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onModifyClick(int position) {
        Producte selectedItem = productelist.get(position);
        String selectedKey = selectedItem.getKey();
        StorageReference producteRef = mStorage.getReferenceFromUrl(selectedItem.getImage());
        String nom = selectedItem.getNom();
        String descripcio = selectedItem.getDescripcio();
        String preu = selectedItem.getPreu();
        String image = selectedItem.getImage();

        Intent intent = new Intent(this, CrearProducteActivity.class);
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
        //Toast.makeText(this, "Delete position: " + position, Toast.LENGTH_SHORT).show();
        Producte selectedItem = productelist.get(position);
        String selectedKey = selectedItem.getKey();
        StorageReference producteRef = mStorage.getReferenceFromUrl(selectedItem.getImage());
        producteRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                mRef.child(selectedKey).removeValue();
                productelist.remove(position);
                Toast.makeText(ProducteActivity.this, "Producte eliminat", Toast.LENGTH_SHORT).show();
            }
        });
    }
}