package com.halbert.iserve;

import com.google.firebase.database.Exclude;

import java.io.Serializable;

public class Ingredient implements Serializable {

    public Ingredient(String mKeyProducte, String quantitat, String nom) {
        this.mKeyProducte = mKeyProducte;
        this.quantitat = quantitat;
        this.nom = nom;
    }

    String mKeyProducte;
    String quantitat;
    String nom;
    String mKey;

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }



    public String getmKeyProducte() {
        return mKeyProducte;
    }

    public void setmKeyProducte(String mKeyProducte) {
        this.mKeyProducte = mKeyProducte;
    }

    public String getQuantitat() {
        return quantitat;
    }

    public void setQuantitat(String quantitat) {
        this.quantitat = quantitat;
    }

    public Ingredient(){

    }

    @Exclude
    public String getKey(){
        return mKey;
    }

    @Exclude
    public void setKey(String key){
        mKey = key;
    }



}
