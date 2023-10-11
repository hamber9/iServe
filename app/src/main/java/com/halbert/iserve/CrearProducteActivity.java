package com.halbert.iserve;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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

public class CrearProducteActivity extends AppCompatActivity {


    //private StorageReference mStorageRef;
    //private DatabaseReference mDatabaseRef;

    FirebaseDatabase mDatabase;
    DatabaseReference mRef;
    FirebaseStorage mStorage;
    EditText nomProducte,descripcioProducte,preuProducte;
    ImageButton imageButton;
    Button crearProducte, actualitzarProducte;

    private static final int Gallery_Code = 1;
    Uri imageUrl = null;
    ProgressDialog progressDialog;

    String nom, descripcio, preu, imatge;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_producte);

        imageButton = findViewById(R.id.imageButtonCrear);
        crearProducte = findViewById(R.id.buttonCrearProducte);
        actualitzarProducte = findViewById(R.id.producteUpdate);
        nomProducte = findViewById(R.id.editTextNomProducte);
        descripcioProducte = findViewById(R.id.editTextDescripcioProducte);
        preuProducte = findViewById(R.id.editTextPreuProducte);

        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference().child("Productes");
        mStorage = FirebaseStorage.getInstance();
        progressDialog= new ProgressDialog(this);

        actualitzarProducte.setVisibility(View.GONE);

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
            nomProducte.setText(nom);

            descripcio = b.getString("descripcio");
            descripcioProducte.setText(descripcio);

            preu = b.getString("preu");
            preuProducte.setText(preu);

            imatge = b.getString("imatge");
            //imageButton.setImageURI(Uri.parse(imatge.toString()));
            Picasso.get().load(imatge).into(imageButton);

            String key = b.getString("key");

            actualitzarProducte.setVisibility(View.VISIBLE);
            crearProducte.setVisibility(View.GONE);





            actualitzarProducte.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    nom = nomProducte.getText().toString();
                    descripcio = descripcioProducte.getText().toString();
                    preu = preuProducte.getText().toString();

                    if (!nom.isEmpty() && !descripcio.isEmpty() && !preu.isEmpty()){

                        progressDialog.setTitle("Actualitzant...");
                        progressDialog.show();


                        StorageReference filepath = mStorage.getReference().child("imagePost").child(nom);
                        if(imageUrl != null)
                            filepath.putFile(imageUrl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Task<Uri> downloadUrl=taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull @NotNull Task<Uri> task) {

                                        String t = task.getResult().toString();

                                        Map<String, Object> producteMap = new HashMap<>();

                                        producteMap.put("Nom", nom);
                                        producteMap.put("Descripcio", descripcio);
                                        producteMap.put("Preu", preu);
                                        producteMap.put("image", t);

                                        mRef.child(key).updateChildren(producteMap).addOnSuccessListener(new OnSuccessListener() {
                                            @Override
                                            public void onSuccess(Object o) {
                                                Toast.makeText(CrearProducteActivity.this, "Producte actualitzat", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                        progressDialog.dismiss();
                                        Toast.makeText(getApplicationContext(), "Producte actualitzat.", Toast.LENGTH_SHORT).show();

                                    }
                                });

                            }
                        });
                        else{
                            String t = imatge;

                            Map<String, Object> producteMap = new HashMap<>();

                            producteMap.put("Nom", nom);
                            producteMap.put("Descripcio", descripcio);
                            producteMap.put("Preu", preu);
                            producteMap.put("image", t);

                            mRef.child(key).updateChildren(producteMap).addOnSuccessListener(new OnSuccessListener() {
                                @Override
                                public void onSuccess(Object o) {
                                    Toast.makeText(CrearProducteActivity.this, "Producte actualitzat", Toast.LENGTH_SHORT).show();
                                }
                            });
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Producte actualitzat.", Toast.LENGTH_SHORT).show();

                        }
                    }
                        }




            });




           // Picasso.LoadedFrom(imatge.toString());
        }
        //String nom = intent.getStringExtra(Intent.EXTRA_TEXT);
       // nomProducte.setText(nom);


        crearProducte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nom = nomProducte.getText().toString();
                descripcio = descripcioProducte.getText().toString();
                preu = preuProducte.getText().toString();

                if (!nom.isEmpty() && !descripcio.isEmpty() && !preu.isEmpty() && imageUrl != null){

                    progressDialog.setTitle("Pujant...");
                    progressDialog.show();

                    StorageReference filepath = mStorage.getReference().child("imagePost").child(nom);
                    filepath.putFile(imageUrl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Task<Uri> downloadUrl=taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<Uri> task) {
                                    String t = task.getResult().toString();

                                    Producte producte = new Producte();

                                    Map<String, Object> producteMap = new HashMap<>();
                                    producteMap.put("Nom", nom);
                                    producteMap.put("Descripcio", descripcio);
                                    producteMap.put("Preu", preu);
                                    producteMap.put("image", t);
                                    mDatabase.getReference().child("Productes").push().setValue(producteMap);

                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "Producte creat.", Toast.LENGTH_SHORT).show();

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