package com.example.ubagenda;

import static com.example.ubagenda.Util.CalendarUtils.joursDeSemaineArray;
import static com.example.ubagenda.Util.CalendarUtils.localDateToString;
import static com.example.ubagenda.Util.CalendarUtils.moisAnneeDeDate;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ubagenda.Adapter.CalendarAdapter;
import com.example.ubagenda.Adapter.CoursAdapter;
import com.example.ubagenda.Metiers.Cours;
import com.example.ubagenda.Util.CalendarUtils;
import com.example.ubagenda.Util.RecupICal;
import com.example.ubagenda.bdd.QRCodeDAO;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;

public class ADESemaineActivity extends AppCompatActivity implements CalendarAdapter.OnItemListener {

    private TextView moisAnnee, tvDateSelec, tvVide;
    private RecyclerView calendarRecyclerView;
    private ListView lvNotes;
    private Toolbar mTool;
    private QRCodeDAO qrDAO = null;
    private DatePickerDialog datePickerDialog;

    private Button btAvant, btApres;
    //Variable d'animation
    private Animation scaleUp, scaleDown;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adesemaine);

        //Mode pleine écran
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        qrDAO = new QRCodeDAO(this);

        //Initialisation du sélecteur de date
        initDatePicker();

        //Autorisation
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //Configuration du ToolBar
        mTool = findViewById(R.id.semaineToolBAr);
        setSupportActionBar(mTool);
        Log.d("Date", String.valueOf(CalendarUtils.selectedDate));
        initWidgets();

        CalendarUtils.selectedDate = LocalDate.now();
        setWeekView();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.semaine_menu, menu);
        return true;
    }

    // Fonction permettant d'attribuer les actions au boutton de la toolbar
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();


        //Boutton de changement de Qr Code
        if (id == R.id.qrChange) {


            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogTheme);
            builder.setMessage("Es-tu sûr de vouloir modifier l'agenda ?");
            builder.setTitle("Confirmation");
            builder.setCancelable(true);
            builder.setPositiveButton("Valider", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    qrDAO.delete();
                    startActivity(new Intent(ADESemaineActivity.this, MainActivity.class));

                }
            }).setNegativeButton("Retour", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    dialogInterface.cancel();

                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }

        //Boutton de rafraichissement
        if (id == R.id.btRefresh) {
            setWeekView();
        }

        //Boutton d'aide
        if (id == R.id.helpButton) {

            LayoutInflater factory = LayoutInflater.from(ADESemaineActivity.this);

            final View view = factory.inflate(R.layout.aide_dialog, null);

            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogTheme);
            builder.setView(view);
            builder.setTitle("Aide");
            builder.setCancelable(true);
            builder.setPositiveButton("Retour", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();

        }


        //Boutton de selection de date
        if (id == R.id.selecDate) {

            datePickerDialog.show();
        }

        return super.onOptionsItemSelected(item);
    }


    //Mise en arriere plan de l'application lorsque que le bouton retour est présser
    @Override
    public void onBackPressed() {

        moveTaskToBack(true);

    }

    private void initDatePicker() {


        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int annee, int mois, int jour) {

                mois =+ 1;

            }
        };

        Calendar cal = Calendar.getInstance();
        int annee = cal.get(Calendar.YEAR);
        int mois = cal.get(Calendar.MONTH);
        Log.d("Mois", "Mois : " + mois);
        int jour = cal.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(this, R.style.AlertDialogTheme, dateSetListener, annee, mois, jour);
        datePickerDialog.setCancelable(true);

        //Evenement lors de l'annulation du selecteur de date
        datePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Annuler", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.cancel();

            }
        });

        datePickerDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Valider", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                DatePicker datePicker = datePickerDialog.getDatePicker();

                String date = makeDateString(datePicker.getDayOfMonth(), datePicker.getMonth() + 1, datePicker.getYear());

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
                Log.d("dateSelec", "String : " + CalendarUtils.selectedDate);
                CalendarUtils.selectedDate = LocalDate.parse(date, formatter);
                Log.d("dateSelec", "date : " + CalendarUtils.selectedDate);
                setWeekView();


            }
        });


    }


    //Transforme la date en String
    private String makeDateString(int jour, int mois, int annee) {

        return jour + "/" + moisConvert(mois) + "/" + annee;
    }


    //Transforme le format du mois en ajoutant un 0 avant les chiffres 1 -> 01
    private String moisConvert(int mois) {

        switch (mois) {

            case 1:
                return "01";

            case 2:
                return "02";

            case 3:
                return "03";

            case 4:
                return "04";

            case 5:
                return "05";

            case 6:
                return "06";

            case 7:
                return "07";

            case 8:
                return "08";

            case 9:
                return "09";

            case 10:
                return "10";

            case 11:
                return "11";

            case 12:
                return "12";

        }

        return "01";


    }


    //Initialisation des éléments
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initWidgets() {

        calendarRecyclerView = findViewById(R.id.calRecyclerView);
        moisAnnee = findViewById(R.id.tvMoisAnnee);
        tvDateSelec = findViewById(R.id.tvDateSelec);
        lvNotes = findViewById(R.id.lvCours);
        btApres = findViewById(R.id.btApres);
        btAvant = findViewById(R.id.btAvant);
        tvVide = findViewById(R.id.tvVide);

        //Animation
        scaleUp = AnimationUtils.loadAnimation(this, R.anim.scale_up);
        scaleDown = AnimationUtils.loadAnimation(this, R.anim.scale_down);


        btApres.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    semaineSuivantAction(view);
                    btApres.startAnimation(scaleUp);
                } else {
                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        btApres.startAnimation(scaleDown);
                    }
                }
                return true;

            }


        });

        btAvant.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    semainePrecedentAction(view);
                    btAvant.startAnimation(scaleUp);
                } else {
                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        btAvant.startAnimation(scaleDown);
                    }
                }
                return true;

            }


        });

        lvNotes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Cours unCours = (Cours) lvNotes.getItemAtPosition(i);
                Log.d("Resultat", unCours.toString());
                Log.d("Resultat", unCours.getLocation());
                Intent intent = new Intent(ADESemaineActivity.this, InfoCoursActivity.class);
                intent.putExtra("unCours", unCours);
                startActivity(intent);
            }
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setWeekView() {

        moisAnnee.setText(moisAnneeDeDate(CalendarUtils.selectedDate));
        tvDateSelec.setText(localDateToString(CalendarUtils.selectedDate));

        ArrayList<LocalDate> jours = joursDeSemaineArray(CalendarUtils.selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(jours, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
        setCoursAdapter();


    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void semainePrecedentAction(View view) {

        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusWeeks(1);
        setWeekView();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void semaineSuivantAction(View view) {

        CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusWeeks(1);
        setWeekView();
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onItemClick(int position, LocalDate date) {

        CalendarUtils.selectedDate = date;
        setWeekView();

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onResume() {

        super.onResume();
        setCoursAdapter();

    }

    //Etablissement de l'adapter
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setCoursAdapter() {

        String resultat = qrDAO.isScan();

        ArrayList<Cours> coursQuotidien = RecupICal.coursDeLaDate(CalendarUtils.selectedDate, RecupICal.getICalendar(resultat));

        CoursAdapter coursAdapter = new CoursAdapter(getApplicationContext(), coursQuotidien);

        if (coursQuotidien.isEmpty()){

            tvVide.setVisibility(View.VISIBLE);

        } else {

            tvVide.setVisibility(View.GONE);

        }

        lvNotes.setAdapter(coursAdapter);




    }

}