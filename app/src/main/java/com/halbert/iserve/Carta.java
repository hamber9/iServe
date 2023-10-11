package com.halbert.iserve;

import com.google.firebase.database.Exclude;

import java.util.List;

public class Carta {

    String nom;
    Recepta primers;
    Recepta segons;
    Recepta tercers;
    String comentaris;
    String mKey;

    public Carta(String nom,Recepta primers, Recepta segons, Recepta tercers, String comentaris) {
        this.nom = nom;
        this.primers = primers;
        this.segons = segons;
        this.tercers = tercers;
        this.comentaris = comentaris;
    }



    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Recepta getPrimers() {
        return primers;
    }

    public void setPrimers(Recepta primers) {
        this.primers = primers;
    }

    public Recepta getSegons() {
        return segons;
    }

    public void setSegons(Recepta segons) {
        this.segons = segons;
    }

    public Recepta getTercers() {
        return tercers;
    }

    public void setTercers(Recepta tercers) {
        this.tercers = tercers;
    }

    public String getComentaris() {
        return comentaris;
    }

    public void setComentaris(String comentaris) {
        this.comentaris = comentaris;
    }

    @Exclude
    public String getKey(){
        return mKey;
    }

    @Exclude
    public void setKey(String key){
        mKey = key;
    }



    public Carta(){

    }


}
