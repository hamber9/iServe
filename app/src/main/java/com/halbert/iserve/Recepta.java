package com.halbert.iserve;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Recepta implements Serializable {





    public Recepta(String nom, List<Ingredient> ingredients, String preparacio, String preu, String image) {
        this.nom = nom;
        this.ingredients = ingredients;
        this.preparacio = preparacio;
        this.preu = preu;
        this.image = image;
    }



    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public String getPreparacio() {
        return preparacio;
    }

    public void setPreparacio(String preparacio) {
        this.preparacio = preparacio;
    }

    public String getPreu() {
        return preu;
    }

    public void setPreu(String preu) {
        this.preu = preu;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    String preparacio;
    String preu;
    String image;
    String mKey;
    String nom;
    List<Ingredient> ingredients;



    @Exclude
    public String getKey(){
        return mKey;
    }

    @Exclude
    public void setKey(String key){
        mKey = key;
    }

    public Recepta() {

    }
}
