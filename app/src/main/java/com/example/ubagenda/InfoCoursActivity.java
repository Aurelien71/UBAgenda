package com.example.ubagenda;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.ubagenda.Metiers.Cours;

public class InfoCoursActivity extends AppCompatActivity {

    private Toolbar sTool;
    private TextView tvDateDebutFin, tvMatiere, tvNomProf, tvLocation, tvTitreInfo;
    Bundle extras = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_cours);

        extras = getIntent().getExtras();

        initWidgets();
        affichageCours();

        sTool = findViewById(R.id.infoToolBar);
        setSupportActionBar(sTool);
    }

    public void initWidgets(){

        tvDateDebutFin = findViewById(R.id.tvDateDebutFinInfo);
        tvMatiere = findViewById(R.id.tvMatiereInfo);
        tvNomProf = findViewById(R.id.tvNomProfInfo);
        tvLocation = findViewById(R.id.tvLocationInfo);
        tvTitreInfo = findViewById(R.id.tvTitreInfo);

    }

    public void affichageCours(){

        // Récuperation du cours envoyer grâce à l'intent
        Cours unCours =  (Cours) extras.getSerializable("unCours");

        String date = unCours.getDateDebut().substring(0,10);

        String heureDebut = unCours.getDateDebut().substring(11,16);
        String heureFin = unCours.getDateFin().substring(11,16);
        String nomProf = unCours.getNomProf();
        heureDebut = heureDebut.replace("-",":");
        heureFin = heureFin.replace("-",":");
        date = date.replace("-","/");
        tvDateDebutFin.setText(heureDebut + " - " + heureFin);
        tvMatiere.setText(unCours.getMatiere());

        if(nomProf == ""){

            nomProf = "Aucune description";

        }
        tvNomProf.setText(nomProf);
        tvLocation.setText(unCours.getLocation());

        tvTitreInfo.setText("Information ("+date+") :");




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.info_menu, menu);
        return true;
    }

    // Fonction permettant d'attribuer les actions au boutton de la toolbar
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.btReturn){

            finish();

        }

        return true;


    }
}