package com.halbert.iserve;

import com.google.firebase.database.Exclude;

public class Beguda {

    String Nom;
    String Descripcio;
    String Preu;
    String image;
    String mKey;

    public String getNom() {
        return Nom;
    }

    public void setNom(String nom) {
        Nom = nom;
    }

    public String getDescripcio() {
        return Descripcio;
    }

    public void setDescripcio(String descripcio) {
        Descripcio = descripcio;
    }

    public String getPreu() {
        return Preu;
    }

    public void setPreu(String preu) {
        Preu = preu;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }



    public Beguda(String nom, String descripcio, String preu, String image) {
        Nom = nom;
        Descripcio = descripcio;
        Preu = preu;
        this.image = image;
    }

    @Exclude
    public String getKey(){
        return mKey;
    }

    @Exclude
    public void setKey(String key){
        mKey = key;
    }



    public Beguda(){

    }

}
