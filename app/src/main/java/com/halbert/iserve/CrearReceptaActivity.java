package com.halbert.iserve;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CrearReceptaActivity extends AppCompatActivity implements Custom_Dialog.Custom_DialogInterFace {

    ArrayList<Producte> producte_list = new ArrayList<>();
    ArrayList<String> producte_listnom = new ArrayList<>();
    List<Ingredient> ingredient_list = new ArrayList<>();
    ArrayList<String> ingredient_listnom = new ArrayList<>();
    Serializable ingredientsRecepta = new ArrayList<>();

    ArrayList<Recepta> recepta_list = new ArrayList<>();

    ArrayList<String> listKeys = new ArrayList<String>();

    Ingredient actual;
    Recepta recepta;

    ListView listview;
    ListView listingredients;
    TextView producteSeleccionat, quantitatProducte, nomrecepta, preparaciorecepta, preurecepta;

    String nom, preparacio, preu, imatge;
    ImageButton imageButton;

    FirebaseDatabase mDatabase;
    DatabaseReference mRef, mRefReceptes;

    Button acceptarINGS, actualitzarRecepta, netejarllista;
    private boolean trobat;

    private static final int Gallery_Code = 1;
    Uri imageUrl = null;
    ProgressDialog progressDialog;

    FirebaseStorage mStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_recepta);

        producteSeleccionat = findViewById(R.id.producteSeleccionat);
        quantitatProducte = findViewById(R.id.quantitatProducte);
        acceptarINGS = findViewById(R.id.buttomACCING);
        netejarllista = findViewById(R.id.buttonNetejar);
        nomrecepta = findViewById(R.id.editTextTextNomRecepta);
        preparaciorecepta = findViewById(R.id.editTextPreparacioRecepta);
        preurecepta = findViewById(R.id.editTextPreuRecepta);
        imageButton = findViewById(R.id.imageButtonRecepta);

        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference().child("Receptes");
        mStorage = FirebaseStorage.getInstance();

        actualitzarRecepta = findViewById(R.id.receptaActualitzarButton);
        mStorage = FirebaseStorage.getInstance();

        actualitzarRecepta.setVisibility(View.GONE);

        progressDialog = new ProgressDialog(this);

        netejarllista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ingredient_list.clear();
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, Gallery_Code);
            }
        });

        llistarProductes();

        if (getIntent().getExtras() != null) {
            Bundle b = getIntent().getExtras();

            nom = b.getString("nom");
            nomrecepta.setText(nom);

            ingredientsRecepta = b.getSerializable("Ingredients");
            ingredient_list = (List<Ingredient>) ingredientsRecepta;

            preparacio = b.getString("preparacio");
            preparaciorecepta.setText(preparacio);

            preu = b.getString("preu");
            preurecepta.setText(preu);

            imatge = b.getString("imatge");
            //imageButton.setImageURI(Uri.parse(imatge.toString()));
            Picasso.get().load(imatge).into(imageButton);

            String key = b.getString("key");

            actualitzarRecepta.setVisibility(View.VISIBLE);
            acceptarINGS.setVisibility(View.GONE);

            //mDatabase = FirebaseDatabase.getInstance();


            actualitzarRecepta.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    nom = nomrecepta.getText().toString();

                    preparacio = preparaciorecepta.getText().toString();
                    preu = preurecepta.getText().toString();

                    if (!nom.isEmpty()  &&!preparacio.isEmpty() && !preu.isEmpty()) {

                        progressDialog.setTitle("Actualitzant...");
                        progressDialog.show();


                        StorageReference filepath = mStorage.getReference().child("imageRecepta").child(nom);
                        if (imageUrl != null)
                            filepath.putFile(imageUrl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Task<Uri> downloadUrl = taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull @NotNull Task<Uri> task) {

                                            String t = task.getResult().toString();


                                            Map<String, Object> receptaMap = new HashMap<>();

                                            receptaMap.put("nom", nom);
                                            receptaMap.put("ingredients", ingredientsRecepta);
                                            receptaMap.put("preparacio", preparacio);
                                            receptaMap.put("preu", preu);
                                            receptaMap.put("image", t);

                                            mRef.child(key).updateChildren(receptaMap).addOnSuccessListener(new OnSuccessListener() {
                                                @Override
                                                public void onSuccess(Object o) {
                                                    Toast.makeText(CrearReceptaActivity.this, "Recepta actualitzada", Toast.LENGTH_SHORT).show();

                                                }
                                            });
                                            progressDialog.dismiss();
                                            Toast.makeText(getApplicationContext(), "Recepta actualitzada", Toast.LENGTH_SHORT).show();

                                        }
                                    });

                                }
                            });
                        else {

                            String t = imatge;

                            Map<String, Object> receptaMap = new HashMap<>();

                            receptaMap.put("nom", nom);
                            receptaMap.put("ingredients", ingredientsRecepta);
                            receptaMap.put("preparacio", preparacio);
                            receptaMap.put("preu", preu);
                            receptaMap.put("image", t);

                            mRef.child(key).updateChildren(receptaMap).addOnSuccessListener(new OnSuccessListener() {
                                @Override
                                public void onSuccess(Object o) {
                                    Toast.makeText(CrearReceptaActivity.this, "Recepta actualitzada", Toast.LENGTH_SHORT).show();

                                }
                            });
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Recepta actualitzada", Toast.LENGTH_SHORT).show();

                        }
                    }

                }


            });


            // Picasso.LoadedFrom(imatge.toString());
        }


            acceptarINGS.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    nom = nomrecepta.getText().toString();
                    preparacio = preparaciorecepta.getText().toString();
                    preu = preurecepta.getText().toString();

                    //recepta = new Recepta(nom, ingredient_list, preparacio);

                    mDatabase = FirebaseDatabase.getInstance();
                    mRefReceptes = mDatabase.getReference().child("Receptes");

                    if (!nom.isEmpty() && !preparacio.isEmpty() && !preu.isEmpty() && imageUrl != null) {

                        progressDialog.setTitle("Pujant...");
                        progressDialog.show();

                        StorageReference filepath = mStorage.getReference().child("imageRecepta").child(nom);
                        filepath.putFile(imageUrl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Task<Uri> downloadUrl = taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull @NotNull Task<Uri> task) {
                                        String t = task.getResult().toString();

                                        recepta = new Recepta(nom, ingredient_list, preparacio, preu, t);

                                        //mRefReceptes.push().setValue(recepta);

                                        Query query1 = mRefReceptes.orderByChild("nom").equalTo(recepta.nom);
                                        query1.addValueEventListener(valueEventListener);


                                    }
                                });
                            }
                        });


                    }


                    //Query query1 = mRefReceptes.orderByChild("nom").equalTo(recepta.nom);

                    //query1.addValueEventListener(valueEventListener);

                }
            });


        }



    private void llistarProductes() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(CrearReceptaActivity.this, R.layout.list_item, R.id.textview, producte_listnom);

        listview = (ListView) findViewById(R.id.listViewProductes);
        listview.setAdapter(adapter);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Productes");

        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                Producte producte = snapshot.getValue(Producte.class);
                String name = producte.getNom();
                producte_list.add(producte);
                producte_listnom.add(producte.getNom());
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

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                producteSeleccionat.setText(producte_list.get(i).Nom);
                actual = new Ingredient(listKeys.get(i), null, producte_list.get(i).getNom());

                openDialog(view);

                mDatabase = FirebaseDatabase.getInstance();
                //mRef = mDatabase.getReference().child("Ingredients");


                //ingredient_list.add(actual);
               // Toast.makeText(CrearReceptaActivity.this, producte_list.get(i), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void openDialog(View view){

        Custom_Dialog custom_dialog = new Custom_Dialog();
        custom_dialog.show(getSupportFragmentManager(),"");

    }

    @Override
    public void applyText(String quantitat) {
        quantitatProducte.setText(quantitat);
        actual.setQuantitat(quantitat);
        ingredient_list.add(actual);
        acceptarINGS.setVisibility(View.VISIBLE);
        //ingredient_listnom.add(actual.getNom());


        //mRef.push().setValue(actual);

    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
            recepta_list.clear();
            if (snapshot.exists()){

                for (DataSnapshot dataSnapshot: snapshot.getChildren()){

                    trobat = true;
                    progressDialog.dismiss();
                    //Toast.makeText(CrearReceptaActivity.this, "La recepta:" + recepta.getNom() + " ja existeix.", Toast.LENGTH_SHORT).show();


                }
            }
            else {
                if (!ingredient_list.isEmpty())
                mRefReceptes.push().setValue(recepta);
                ingredient_list.clear();
                progressDialog.dismiss();
                //Toast.makeText(CrearReceptaActivity.this, "Pujat:" + recepta.getNom(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onCancelled(@NonNull @NotNull DatabaseError error) {

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==Gallery_Code && resultCode == RESULT_OK){

            imageUrl = data.getData();
            imageButton.setImageURI(imageUrl);
        }

    }

}