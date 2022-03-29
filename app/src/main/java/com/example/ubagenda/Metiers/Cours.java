package com.example.ubagenda.Metiers;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.Serializable;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Cours implements Comparable<Cours>, Serializable{

    private String dateDebut;
    private String dateFin;
    private String matiere;
    private String location;
    private String nomProf;

    public Cours() {
    }

    /**
     *
     * @param dateDebut Date début du cours
     * @param dateFin Date fin du cours
     * @param matiere Matière du cours
     * @param location Location du cours
     * @param nomProf Nom du prof et description
     */
    public Cours(String dateDebut, String dateFin, String matiere, String location, String nomProf) {
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.matiere = matiere;
        this.location = location;
        this.nomProf = nomProf;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public String getMatiere() {
        return matiere;
    }

    public void setMatiere(String matiere) {
        this.matiere = matiere;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getNomProf() {
        return nomProf;
    }

    public void setNomProf(String nomProf) {
        this.nomProf = nomProf;
    }

    @Override
    public String toString() {
        return "Cours{" +
                "dateDebut='" + dateDebut + '\'' +
                ", dateFin='" + dateFin + '\'' +
                ", matiere='" + matiere + '\'' +
                ", location='" + location + '\'' +
                ", nomProf='" + nomProf + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cours cours = (Cours) o;
        return Objects.equals(dateDebut, cours.dateDebut) && Objects.equals(dateFin, cours.dateFin) && Objects.equals(matiere, cours.matiere) && Objects.equals(location, cours.location) && Objects.equals(nomProf, cours.nomProf);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dateDebut, dateFin, matiere, location, nomProf);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int compareTo(Cours cours) {

        String heure1 = this.getDateDebut().substring(11,13) + this.getDateDebut().substring(14,16);
        String heure2 = cours.getDateDebut().substring(11,13) + cours.getDateDebut().substring(14,16);
        int heure1Int = Integer.parseInt(heure1);
        int heure2Int = Integer.parseInt(heure2);

        return (heure1Int - heure2Int);
    }
}
