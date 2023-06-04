package fr.kunze.crossepsv2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;

import com.google.zxing.Result;
import com.google.zxing.client.android.BeepManager;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.BarcodeView;
import com.journeyapps.barcodescanner.CompoundBarcodeView;
import com.journeyapps.barcodescanner.DecoderFactory;

import java.util.ArrayList;
import java.util.List;

public class Course extends AppCompatActivity {

    Chronometer chrono;
    Button start;
    ListView listView;
    String nomCourse;
    TextView nomCours;
    BarcodeView barcodeView;

    CoureurDAO base;
    Coureur c;

    long time = 0;
    String temps = "";

    String nom;
    int place = 0;

    ArrayList<Coureur> listcoureur;
    ArrayList<String> li;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        chrono = (Chronometer) findViewById(R.id.chrono);
        start = findViewById(R.id.start);
        listView = findViewById(R.id.listview);
        nomCours = findViewById(R.id.nomCoursego);
        barcodeView = findViewById(R.id.barcode_scanner);

        Intent i = getIntent();
        nomCourse = i.getStringExtra(MainActivity.NOM_COURSE);
        nomCours.setText(nomCourse);

        base = new CoureurDAO(this);
        base.openDb();

        listcoureur = new ArrayList<>();
        li = new ArrayList<>();

        chrono.setFormat("00:%s");
        chrono.setOnChronometerTickListener(chronometer -> {
            long elapsedmillis = SystemClock.elapsedRealtime() - chronometer.getBase();
            if (elapsedmillis > 3600000L) {
                chronometer.setFormat("0%s");
            } else {
                chronometer.setFormat("00:%s");
            }
        });


        start.setOnClickListener(v -> {

            if (start.getText().toString().matches("STOP")) {

                LayoutInflater factory = LayoutInflater.from(Course.this);
                final View alertDialogView = factory.inflate(R.layout.boite_de_dialogue_arreter, null);

                //Création de l'AlertDialog
                final AlertDialog.Builder adb = new AlertDialog.Builder(Course.this);

                //On affecte la vue personnalisé que l'on a crée à notre AlertDialog
                adb.setView(alertDialogView);

                //On donne un titre à l'AlertDialog
                adb.setTitle("Arrêter la course");

                //On affecte un bouton "OK" à notre AlertDialog et on lui affecte un évènement
                adb.setPositiveButton("Ok", (dialog, which) -> {

                    chrono.stop();
                    Intent i1 = new Intent(Course.this, MainActivity.class);
                    startActivity(i1);
                    overridePendingTransition(R.anim.slideleft, R.anim.slideoutright);
                    finish();

                });

                //On crée un bouton "Annuler" à notre AlertDialog et on lui affecte un évènement
                adb.setNegativeButton("Annuler", (dialog, which) -> dialog.dismiss());

                adb.show();

            } else {

                MediaPlayer mediaPlayer = MediaPlayer.create(Course.this, R.raw.sifflet);
                mediaPlayer.start();
                chrono.setBase(SystemClock.elapsedRealtime());
                time = chrono.getBase();
                chrono.start();
                start.setText("STOP");
                barcodeView.setVisibility(View.VISIBLE);
                barcodeView.decodeContinuous(callback);
            }
        });

    }

    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(final BarcodeResult result) {
            //code to handle “result”
            if (result.getText() != null) {
                temps = chrono.getText().toString();
                if (li.contains(result.getText())) {

                } else {
                    li.add(result.getText());
                    place = place + 1;
                    nom = result.getText();
                    c = new Coureur(nom, place, temps, nomCourse);
                    base.ajoutCoureur(c);
                    listcoureur.add(c);
                    listCoureurAdapter adapter = new listCoureurAdapter(Course.this, R.layout.liste_coureur, listcoureur);
                    listView.setAdapter(adapter);
                    Toast.makeText(Course.this, result.getText(), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(Course.this,
                        "Aucune donnée reçue!", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void possibleResultPoints(List resultPoints) {
        }
    };

   /* @SuppressLint("MissingSuperCall")
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

// nous utilisons la classe IntentIntegrator et sa fonction parseActivityResult pour parser le résultat du scan

        super.onActivityResult(requestCode, resultCode, intent);
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {

// nous récupérons le contenu du code barre
            String scanContent = scanningResult.getContents();
            if (scanContent != null) {

                place = place + 1;
                nom = scanContent;
                c = new Coureur(nom, place, temps, nomCourse);
                base.ajoutCoureur(c);
                listcoureur.add(c);
                listCoureurAdapter adapter = new listCoureurAdapter(Course.this, R.layout.liste_coureur, listcoureur);
                listView.setAdapter(adapter);

            } else {
                Toast.makeText(Course.this,
                        "Aucune donnée reçue!", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(Course.this,
                    "Aucune donnée reçu!", Toast.LENGTH_SHORT).show();
        }

    }
*/
   @Override
   public void onResume() {
       barcodeView.resume();
       super.onResume();
   }

    @Override
    public void onPause() {
        barcodeView.pause();
        super.onPause();
    }

}