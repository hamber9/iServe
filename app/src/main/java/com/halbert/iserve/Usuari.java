package com.halbert.iserve;

import com.google.firebase.database.Exclude;

public class Usuari {

    String email;
    String mKey;
    String categoria;

    public Usuari(String email, String categoria) {
        this.email = email;
        this.categoria = categoria;
    }



    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }




    @Exclude
    public String getKey(){
        return mKey;
    }

    @Exclude
    public void setKey(String key){
        mKey = key;
    }

    public Usuari(){

    }
}
