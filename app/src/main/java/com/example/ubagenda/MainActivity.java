package com.example.ubagenda;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.ubagenda.bdd.QRCodeDAO;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btScan;
    QRCodeDAO qrDAO = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        qrDAO = new QRCodeDAO(this);

        //Si on trouve le qr code dans la base données on lance l'affichage de l'EDT
        if(qrDAO.isScan() != null){

            Intent intent1 = new Intent(MainActivity.this, ADESemaineActivity.class);
            startActivity(intent1);

        }

        //Déclaration du boutton de scan
        btScan = findViewById(R.id.btScan);
        btScan.setOnClickListener(this);
    }


    //Evenement ce déclenchant au clique sur le boutton
    @Override
    public void onClick(View view) {

        scanCode();


    }

    public void scanCode(){

        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(CaptureAct.class);
        integrator.setOrientationLocked(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        integrator.setBeepEnabled(false);
        integrator.setPrompt("Scannez le QR Code de votre emploi du temps");
        integrator.initiateScan();



    }


    //Evenelent ce declenchant lorsqu'un résultat a été transmis
    @Override
    protected void  onActivityResult(int requestCode, int resultCode, Intent data){

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null){

            if (result.getContents() != null){

                AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogTheme);
                builder.setMessage(result.getContents());
                builder.setTitle("Résultat");
                builder.setPositiveButton("Valider", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        String resultat  = result.getContents();
                        // Ajout du resultat dans la base de données
                        qrDAO.create(resultat);

                        Intent intent = new Intent(MainActivity.this, ADESemaineActivity.class);
                        startActivity(intent);

                    }
                }).setNegativeButton("Recommencer", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d("Resultat",result.getContents());
                        scanCode();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();


            }
            else {

                Toast.makeText(this, "Aucun résultat", Toast.LENGTH_LONG).show();

            }


        } else{

            super.onActivityResult(requestCode, resultCode, data);

        }

    }
}