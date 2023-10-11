package com.halbert.iserve;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ShowPlatActivity extends AppCompatActivity {

    String nom, ingredients, preparacio, imatge;
    ImageView imageView;
    TextView tvnom, tvingredients, tvpreparacio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_plat);

        imageView = findViewById(R.id.imageViewRecepta);
        tvnom = findViewById(R.id.textViewNomRecepta);
        tvingredients = findViewById(R.id.textViewIngredient);
        tvpreparacio = findViewById(R.id.textViewPreparacio);

        if (getIntent().getExtras() != null) {
            Bundle b = getIntent().getExtras();

            nom = b.getString("nom");

            ingredients = b.getString("ingredients");

            preparacio = b.getString("preparacio");


            imatge = b.getString("imatge");
            //imageButton.setImageURI(Uri.parse(imatge.toString()));
            Picasso.get().load(imatge).into(imageView);

            tvnom.setText(nom);
            tvingredients.setText(ingredients);
            tvpreparacio.setText(preparacio);

        }
    }
}