package com.example.ubagenda.bdd;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class createBdUBAgenda extends SQLiteOpenHelper {

    //Nom de la base de données
    public static final String TABLE_QRCODE = "qrcode";

    //Creation de la base de donnée
    private static final String CREATE_TABLE_QRCODE =  "CREATE TABLE " + TABLE_QRCODE + "("+
            "id INTEGER PRIMARY KEY AUTOINCREMENT, "+
            "url TEXT);";



    public createBdUBAgenda(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_QRCODE);
        Log.d("bdd", "Base créee");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE " + TABLE_QRCODE + ";");
        Log.d("bdd", "Table " + TABLE_QRCODE + " supprimée");
        onCreate(db);

    }
}
