package com.example.ubagenda.bdd;

import static com.example.ubagenda.bdd.createBdUBAgenda.TABLE_QRCODE;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class QRCodeDAO {

    private DAO dao = null;
    private SQLiteDatabase db = null;

    //Constructeur
    public QRCodeDAO(Context context) {
        dao = new DAO(context);
        db = dao.open();
    }


    //Fonction permettant la création d'un qr code
    public long create(String url) {

        ContentValues values = new ContentValues();
        values.put("url", url);
        Log.d("bdd", "insert : " + url);
        return db.insert(TABLE_QRCODE, null, values);

    }

    //Fonction permettant de renvoyer le qr code stocker en base de données s'il est présent
    public String isScan() {
        String url = null;
        String sql = "SELECT * FROM "+ TABLE_QRCODE;
        Cursor c = db.rawQuery(sql, null);

        if (c.moveToNext()) {
            url = c.getString(1);
        }
        return url;


    }

    public void delete() {

        db.execSQL("DELETE FROM " + TABLE_QRCODE + ";");


    }


    public void close() {

        dao.close();

    }

}
