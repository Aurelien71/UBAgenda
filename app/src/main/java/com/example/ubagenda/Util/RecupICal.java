package com.example.ubagenda.Util;

import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.ubagenda.Metiers.Cours;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.Property;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class RecupICal {


    //Fonction permettant de récuperer le ICalendar du Qr Code grâce à la librairie ICal4J

    /**
     *
     * @param resultat URL renvoyé par le QR Code
     * @return Tout les cours dans une collection (ArrayList)
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static ArrayList<Cours> getICalendar(String resultat) {

        ArrayList<Cours> lesCours = new ArrayList<>();

        String yyyy;
        String MM;
        String dd;
        String HH;
        int HHint;
        String mm;





        Log.d("Reponse", resultat);

        try {
            //Ajout de l'URL
            URL url = new URL(resultat);
            //Lecture des données de l'URL
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            //Ajout des données sous le format de calendrier
            Calendar cal = new CalendarBuilder().build(br);


            //Parcours des elements calendrier
            for (Iterator i = cal.getComponents().iterator(); i.hasNext(); ) {
                Component component = (Component) i.next();
                Cours unCours = new Cours();
                Log.d("Reponse", "Component [" + component.getName() + "]");

                //Parcours des valeurs
                for (Iterator j = component.getProperties().iterator(); j.hasNext(); ) {
                    Property property = (Property) j.next();

                    String value = property.getValue();

                    switch (property.getName()) {

                        case "DTSTART":
                            yyyy = value.substring(0,4);
                            MM = value.substring(4,6);
                            dd = value.substring(6,8);
                            HH = value.substring(9,11);
                            HHint = Integer.parseInt(HH)+2;
                            if (HHint <= 9){

                                HH = "0"+HHint;

                            } else {

                                HH = ""+HHint;

                            }



                            mm = value.substring(11,13);
                            value = dd+"-"+MM+"-"+yyyy+"-"+HH+"-"+mm;
                            unCours.setDateDebut(value);
                            break;

                        case "DTEND":

                            yyyy = value.substring(0,4);
                            MM = value.substring(4,6);
                            dd = value.substring(6,8);
                            HH = value.substring(9,11);
                            HHint = Integer.parseInt(HH)+2;
                            if (HHint <= 9){

                                HH = "0"+HHint;

                            } else {

                                HH = ""+HHint;

                            }
                            mm = value.substring(11,13);
                            value = dd+"-"+MM+"-"+yyyy+"-"+HH+"-"+mm;
                            unCours.setDateFin(value);
                            break;

                        case "SUMMARY":
                            unCours.setMatiere(value);
                            break;

                        case "LOCATION":
                            unCours.setLocation(value);
                            break;

                        case "DESCRIPTION":

                            //On enleve tout ce qui a entre paranthèse
                            value = value.replaceAll("\\(.*\\)", "");
                            value = value.substring(2, value.length()-2);
                            unCours.setNomProf(value);
                            break;


                    }
                    Log.d("Reponse", "Property [" + property.getName() + ", " + value + "]");
                }
                lesCours.add(unCours);
            }

        } catch (Exception e) {

            Log.d("Reponse", "Exception : " + e.getMessage());
        }
        Log.d("Resultat",lesCours.toString());
        return lesCours;


    }


    /**
     *
     * @param date Date selectionnée
     * @param lesCours Collection de tout les cours récupérés
     * @return La liste des cours du jour sélèctionné
     */

    // Fonction renvoyant la liste des cours de la date en paramètre
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static  ArrayList<Cours> coursDeLaDate(LocalDate date, ArrayList<Cours> lesCours){

        ArrayList<Cours> cours = new ArrayList<>();

        for(Cours unCour : lesCours){

            //Récuperation de la date sans l'heure et les minutes
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            String dateCoursStr = unCour.getDateDebut().substring(0,10);

            LocalDate dateCours = LocalDate.parse(dateCoursStr, formatter);
            Log.d("Date", "Date du cours  : " + dateCours + " Date select : " + date);

            if(dateCours.equals(date)){

                Log.d("Date", "Egale !  ");

                cours.add(unCour);

            }

        }

        Collections.sort(cours);

        return cours;

    }
}
