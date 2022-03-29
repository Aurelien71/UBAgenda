package com.example.ubagenda.Util;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class CalendarUtils {


    //Variable récuperant la date selectionner
    public static LocalDate selectedDate;


    /**
     *
     * @param date Date sélectionnée
     * @return Le mois et l'année correspondant à la date sélectionnée
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String moisAnneeDeDate(LocalDate date){

        // Format : Mois/Année
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        // On transforme la date en String
        String moisAnneeDeDate = date.format(formatter);

        return moisAnneeDeDate.substring(0, 1).toUpperCase() + moisAnneeDeDate.substring(1);


    }


    /**
     *
     * @param date Date sélectionnée
     * @return Le jour du mois, le jour, le mois, l'années de la date sélectionnée en String
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String localDateToString(LocalDate date){


        // Format : JoursDuMois/Jours/Mois/Année
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE dd MMMM yyyy");
        // On transforme la date en String
        String dateString = date.format(formatter);
        // On mets la première lettre de la chaîne de caractère en majuscule
        String firstLetter = dateString.substring(0,1).toUpperCase();
        dateString = firstLetter + dateString.substring(1);


        return dateString;


    }

    /**
     *
     * @param selectedDate Date sélectionner
     * @return Collection regroupant les jours de la semaine
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static ArrayList<LocalDate> joursDeSemaineArray(LocalDate selectedDate)
    {

        // Collection correspond aux jours de la semaine
        ArrayList<LocalDate> jours = new ArrayList<>();
        // On récupère le lundi de la semaine sélectionné
        LocalDate current = mondayDeDate(selectedDate);
        // On récupère la date limite de la semaine (le lundi suivant comme limite)
        LocalDate endDate = current.plusWeeks(1);

        // Tant que le jour est avant la date limite on ajoute ce jour dans la collection 'jours' puis on ajoute 1 à la valeur pour récuperer tout les jours de la semaine
        while (current.isBefore(endDate))
        {
            jours.add(current);
            current = current.plusDays(1);


        }

        return jours;


    }


    /**
     *
     * @param current Date sélectionné
     * @return le Lundi de le semaine
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private static LocalDate mondayDeDate(LocalDate current)
    {
        //On récupère la semaine d'avant
        LocalDate uneSemainePasse = current.minusWeeks(1);

        while (current.isAfter(uneSemainePasse)){

            if (current.getDayOfWeek() == DayOfWeek.MONDAY)
                return current;

            current = current.minusDays(1);

        }

        return null;

    }


}
