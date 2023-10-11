package com.halbert.iserve;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class CrearBegudaActivity extends AppCompatActivity {

    FirebaseDatabase mDatabase;
    DatabaseReference mRef;
    FirebaseStorage mStorage;
    EditText nomBeguda,descripcioBeguda,preuBeguda;
    ImageButton imageButton;
    Button crearBeguda, actualitzarBeguda;

    private static final int Gallery_Code = 1;
    Uri imageUrl = null;
    ProgressDialog progressDialog;

    String nom, descripcio, preu, imatge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_beguda);

        imageButton = findViewById(R.id.imageButtonCrearBeguda);
        crearBeguda = findViewById(R.id.buttonCrearBeguda);
        actualitzarBeguda = findViewById(R.id.actualitzarBeguda);
        nomBeguda = findViewById(R.id.editTextNomBeguda);
        descripcioBeguda = findViewById(R.id.editTextDescripcioBeguda);
        preuBeguda = findViewById(R.id.editTextPreuBeguda);

        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference().child("Begudes");
        mStorage = FirebaseStorage.getInstance();
        progressDialog= new ProgressDialog(this);

        actualitzarBeguda.setVisibility(View.GONE);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,Gallery_Code );
            }
        });

        if (getIntent().getExtras() != null){
            Bundle b = getIntent().getExtras();

            nom = b.getString("nom");
            nomBeguda.setText(nom);

            descripcio = b.getString("descripcio");
            descripcioBeguda.setText(descripcio);

            preu = b.getString("preu");
            preuBeguda.setText(preu);

            imatge = b.getString("imatge");
            //imageButton.setImageURI(Uri.parse(imatge.toString()));
            Picasso.get().load(imatge).into(imageButton);

            String key = b.getString("key");

            actualitzarBeguda.setVisibility(View.VISIBLE);
            crearBeguda.setVisibility(View.GONE);





            actualitzarBeguda.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    nom = nomBeguda.getText().toString();
                    descripcio = descripcioBeguda.getText().toString();
                    preu = preuBeguda.getText().toString();

                    if (!nom.isEmpty() && !descripcio.isEmpty() && !preu.isEmpty()){

                        progressDialog.setTitle("Actualitzant...");
                        progressDialog.show();


                        StorageReference filepath = mStorage.getReference().child("imageBeguda").child(nom);
                        if(imageUrl != null)
                            filepath.putFile(imageUrl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Task<Uri> downloadUrl=taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull @NotNull Task<Uri> task) {

                                            String t = task.getResult().toString();

                                            Map<String, Object> begudaMap = new HashMap<>();

                                            begudaMap.put("Nom", nom);
                                            begudaMap.put("Descripcio", descripcio);
                                            begudaMap.put("Preu", preu);
                                            begudaMap.put("image", t);

                                            mRef.child(key).updateChildren(begudaMap).addOnSuccessListener(new OnSuccessListener() {
                                                @Override
                                                public void onSuccess(Object o) {
                                                    Toast.makeText(CrearBegudaActivity.this, "Beguda actualitzat", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                            progressDialog.dismiss();
                                            Toast.makeText(getApplicationContext(), "Beguda actualitzada.", Toast.LENGTH_SHORT).show();

                                        }
                                    });

                                }
                            });
                        else{
                            String t = imatge;

                            Map<String, Object> begudaMap = new HashMap<>();

                            begudaMap.put("Nom", nom);
                            begudaMap.put("Descripcio", descripcio);
                            begudaMap.put("Preu", preu);
                            begudaMap.put("image", t);

                            mRef.child(key).updateChildren(begudaMap).addOnSuccessListener(new OnSuccessListener() {
                                @Override
                                public void onSuccess(Object o) {
                                    Toast.makeText(CrearBegudaActivity.this, "Beguda actualitzat", Toast.LENGTH_SHORT).show();
                                }
                            });
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Beguda actualitzat.", Toast.LENGTH_SHORT).show();

                        }
                    }
                }




            });




            // Picasso.LoadedFrom(imatge.toString());
        }
        //String nom = intent.getStringExtra(Intent.EXTRA_TEXT);
        // nomProducte.setText(nom);


        crearBeguda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nom = nomBeguda.getText().toString();
                descripcio = descripcioBeguda.getText().toString();
                preu = preuBeguda.getText().toString();

                if (!nom.isEmpty() && !descripcio.isEmpty() && !preu.isEmpty() && imageUrl != null){

                    progressDialog.setTitle("Pujant...");
                    progressDialog.show();

                    StorageReference filepath = mStorage.getReference().child("imageBeguda").child(nom);
                    filepath.putFile(imageUrl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Task<Uri> downloadUrl=taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<Uri> task) {
                                    String t = task.getResult().toString();

                                    Producte producte = new Producte();

                                    Map<String, Object> begudaMap = new HashMap<>();
                                    begudaMap.put("Nom", nom);
                                    begudaMap.put("Descripcio", descripcio);
                                    begudaMap.put("Preu", preu);
                                    begudaMap.put("image", t);
                                    mDatabase.getReference().child("Begudes").push().setValue(begudaMap);

                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "Beguda creat.", Toast.LENGTH_SHORT).show();

                                    /*DatabaseReference newPost = mRef.push();

                                    newPost.child("Nom").setValue(nom);
                                    newPost.child("Descripcio").setValue(descripcio);
                                    newPost.child("Preu").setValue(preu);
                                    newPost.child("image").setValue(task.getResult().toString());
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "Producte creat.", Toast.LENGTH_SHORT).show();*/
                                    //Intent intent = new Intent (CrearProducteActivity.this, ProducteActivity.class);
                                    //startActivity(intent);


                                }
                            });
                        }
                    });
                }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==Gallery_Code && resultCode == RESULT_OK){

            imageUrl = data.getData();
            imageButton.setImageURI(imageUrl);
        }

    }


    }
