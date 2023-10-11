package com.halbert.iserve;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CartaListActivity extends AppCompatActivity {

    ListView listView, receptaList;
    FirebaseDatabase mDatabase;
    DatabaseReference mRef;
    List<Carta> carta_list = new ArrayList<>();
    List<Recepta> recepta_list = new ArrayList<>();
    List<String> listKeys = new ArrayList<>();
    List<String> listKeysRecepta = new ArrayList<>();
    List<String> cartalist_nom = new ArrayList<>();
    List<String> recepta_list_nom = new ArrayList<>();
    ArrayAdapter<String> adapter, adapterPerRecepta;
    String currentnom, currentnom2, currentnom3;
    boolean trobat= false;
    Recepta primer, segon, tercer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carta_list);

        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference().child("Cartes");

        listView = (ListView) findViewById(R.id.listViewCarta);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, cartalist_nom);
        listView.setAdapter(adapter);

        mRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                Carta carta = snapshot.getValue(Carta.class);
                String name = carta.getNom();
                carta_list.add(carta);
                cartalist_nom.add(carta.getNom());
                listKeys.add(snapshot.getKey());
                adapter.notifyDataSetChanged();
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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                currentnom = carta_list.get(i).getPrimers().getNom();
                currentnom2 = carta_list.get(i).getSegons().getNom();
                currentnom3 = carta_list.get(i).getTercers().getNom();
                primer = carta_list.get(i).primers;
                segon = carta_list.get(i).segons;
                tercer = carta_list.get(i).tercers;
                openDialog();
            }
        });


    }

    private void openDialog() {
        recepta_list_nom.clear();
        Dialog dialog = new Dialog(CartaListActivity.this);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        View custom_dialog = layoutInflater.inflate(R.layout.select_recepta_dialog, null);

        receptaList = custom_dialog.findViewById(R.id.listViewCarta);

        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference().child("Receptes");

        adapterPerRecepta = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, recepta_list_nom);
        receptaList.setAdapter(adapterPerRecepta);

       // Query query1 = mRef.orderByChild("nom").equalTo(currentnom);

        //query1.addValueEventListener(valueEventListener);

       // if (trobat)

        Query query = mRef.orderByChild("nom").equalTo(currentnom);
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                Recepta recepta = snapshot.getValue(Recepta.class);

                recepta_list_nom.add(recepta.getNom());

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
        Query query2 = mRef.orderByChild("nom").equalTo(currentnom2);
        query2.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                Recepta recepta = snapshot.getValue(Recepta.class);

                recepta_list_nom.add(recepta.getNom());

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
        Query query3 = mRef.orderByChild("nom").equalTo(currentnom3);
        query3.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                Recepta recepta = snapshot.getValue(Recepta.class);

                recepta_list_nom.add(recepta.getNom());

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


        dialog.setContentView(custom_dialog);
        dialog.setTitle("Receptes");
        dialog.show();





    }



}
