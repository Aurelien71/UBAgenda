package com.example.ubagenda.bdd;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DAO {

    private static final int VERSION_BDD = 3;
    private static final String NOM_BDD = "ubagenda.db";
    private createBdUBAgenda createBd = null;
    private SQLiteDatabase db = null;


    public DAO (Context context){

        createBd = new createBdUBAgenda(context, NOM_BDD, null, VERSION_BDD);
        Log.d("bdd","Appel au constructeur de DAO ok");
    }

    public SQLiteDatabase open(){

        if(db == null){
            db = createBd.getWritableDatabase();
            Log.d("bdd","Bdd ouverte");

        } else {


            Log.d("bdd","Bdd accessible");

        }
        return db;

    }

    public void close(){
        if(db != null){
            db.close();
            Log.d("bdd","bdd fermé");


        }


    }
}
