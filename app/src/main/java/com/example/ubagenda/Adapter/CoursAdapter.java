package com.example.ubagenda.Adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.ubagenda.Metiers.Cours;
import com.example.ubagenda.R;

import java.util.ArrayList;

public class CoursAdapter extends ArrayAdapter<Cours> {

    private int lastPosition = -1;


    public CoursAdapter(@NonNull Context context, ArrayList<Cours> cours) {
        super(context, 0, cours);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Cours unCours = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.cours_cellule, parent, false);
        }

        //Récuperation des champ du layout

        TextView tvCellCoursMatiere = convertView.findViewById(R.id.tvCellCoursMatiere);
        TextView tvCellCoursSalleProf = convertView.findViewById(R.id.tvCellCoursSalleProf);
        TextView tvCellCoursHeure = convertView.findViewById(R.id.tvCellCoursHeure);

        //Récuperation de l'heure et les minutes de dates

        String heureDebut = unCours.getDateDebut().substring(11,16);
        String heureFin = unCours.getDateFin().substring(11,16);
        heureDebut = heureDebut.replace("-",":");
        heureFin = heureFin.replace("-",":");


        //Alimentation du layout
        String cellCoursMatiere = unCours.getMatiere();
        tvCellCoursMatiere.setText(cellCoursMatiere);

        String cellCoursSalleProf = unCours.getLocation()+"\n" + unCours.getNomProf();
        tvCellCoursSalleProf.setText(cellCoursSalleProf);

        String cellCoursHeure = heureDebut + " - " + heureFin;
        tvCellCoursHeure.setText(cellCoursHeure);

        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.slide_right);
        convertView.startAnimation(animation);




        return convertView;

    }





}
