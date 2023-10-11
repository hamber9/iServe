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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlatActivity extends AppCompatActivity implements ReceptaAdapter.OnItemClickListener {

    String KEY_TEXTPSS = "TEXTPSS";
    FirebaseDatabase mDatabase;
    DatabaseReference mRef;
    FirebaseStorage mStorage;
    RecyclerView recyclerView;
    ReceptaAdapter receptaAdapter;
    List<Recepta> receptalist;
    List<Ingredient> RecIngredients;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plat);

        Button CrearPlat;
        CrearPlat = findViewById(R.id.CrearPlatButton);

        Button LlistarPlat;
        LlistarPlat = findViewById(R.id.llistarPlat_id);





        CrearPlat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), CrearReceptaActivity.class));
            }
        });

        LlistarPlat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                construirRecycle();
            }
        });


    }

    private void construirRecycle() {

        setContentView(R.layout.activity_retrive_data_in_recycler_view);
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference().child("Receptes");
        mStorage = FirebaseStorage.getInstance();

        recyclerView = findViewById(R.id.recyclerview_id);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //-----> recyclerView.setLayoutManager(new GridLayoutManager(this, 4));

        receptalist = new ArrayList<Recepta>();
        receptaAdapter = new ReceptaAdapter(PlatActivity.this, receptalist);
        recyclerView.setAdapter(receptaAdapter);

        receptaAdapter.setOnItemClickListener(PlatActivity.this);

        mRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                Recepta recepta = snapshot.getValue(Recepta.class);
                recepta.setKey(snapshot.getKey());
                receptalist.add(recepta);
                receptaAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                receptaAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {
                receptaAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                receptaAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                receptaAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        Toast.makeText(this, "Normal click at position: " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onOpenClick(int position) {

        Recepta selectedItem = receptalist.get(position);
        String selectedKey = selectedItem.getKey();
        StorageReference producteRef = mStorage.getReferenceFromUrl(selectedItem.getImage());

        List<Ingredient> ingredients = selectedItem.getIngredients();


        //String str = Arrays.toString(ingredients.toArray());

        StringBuilder out = new StringBuilder();

        for (Ingredient ingredient : ingredients){
            out.append(ingredient.getQuantitat() + "x " + ingredient.getNom());
            out.append("\n");

        }

        String nom = selectedItem.getNom();

        String ingredientString = out.toString();

        String preparacio = selectedItem.getPreparacio();

        String image = selectedItem.getImage();

        Intent intent = new Intent(this, ShowPlatActivity.class);
        intent.putExtra("nom", nom);
        intent.putExtra("ingredients", ingredientString);
        intent.putExtra("preparacio", preparacio);
        intent.putExtra("imatge", image);

        startActivity(intent);



    }

    @Override
    public void onModifyClick(int position) {
        Recepta selectedItem = receptalist.get(position);
        String selectedKey = selectedItem.getKey();
        StorageReference receptaRef = mStorage.getReferenceFromUrl(selectedItem.getImage());
        String nom = selectedItem.getNom();
        //ingredients
        RecIngredients = new ArrayList<>();
        RecIngredients = receptalist.get(position).getIngredients();


        String preparacio = selectedItem.getPreparacio();
        String preu = selectedItem.getPreu();
        String image = selectedItem.getImage();

        Intent intent = new Intent(this, CrearReceptaActivity.class);
        intent.putExtra("nom", nom);
        intent.putExtra("Ingredients", (Serializable) RecIngredients);
        intent.putExtra("preparacio", preparacio);
        intent.putExtra("preu", preu);
        intent.putExtra("imatge", image);
        intent.putExtra("key", selectedKey);

        startActivity(intent);


        Toast.makeText(this, "Modify position: " + nom, Toast.LENGTH_SHORT).show();



    }

    @Override
    public void onDeleteClick(int position) {

        Recepta selectedItem = receptalist.get(position);
        String selectedKey = selectedItem.getKey();
        StorageReference receptaRef = mStorage.getReferenceFromUrl(selectedItem.getImage());
        receptaRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                mRef.child(selectedKey).removeValue();
                receptalist.remove(position);
                //mRef.keepSynced(false);

                Toast.makeText(PlatActivity.this, "Recepta eliminada", Toast.LENGTH_SHORT).show();
            }
        });
    }

}