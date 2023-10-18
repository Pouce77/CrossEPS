package fr.kunze.crossepsv2;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.BarcodeView;

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
    Button addCoureur;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        chrono = findViewById(R.id.chrono);
        start = findViewById(R.id.start);
        listView = findViewById(R.id.listview);
        nomCours = findViewById(R.id.nomCoursego);
        barcodeView = findViewById(R.id.barcode_scanner);
        addCoureur=findViewById(R.id.buttonAdd);
        addCoureur.setEnabled(false);

        Intent i = getIntent();
        nomCourse = i.getStringExtra(MainActivity.NOM_COURSE);
        nomCours.setText(nomCourse);

        base = new CoureurDAO(this);
        base.openDb();

        listcoureur = new ArrayList<>();
        li = new ArrayList<>();

        listCoureurAdapter adapter = new listCoureurAdapter(Course.this, android.R.layout.simple_list_item_1, listcoureur);
        listView.setAdapter(adapter);

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

                addCoureur.setEnabled(false);
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
                addCoureur.setEnabled(true);
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

        addCoureur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LayoutInflater factory = LayoutInflater.from(Course.this);
                final View alertDialogView = factory.inflate(R.layout.boite_de_dialogue_ajouter_coureur, null);

                //Création de l'AlertDialog
                final AlertDialog.Builder adb = new AlertDialog.Builder(Course.this);

                //On affecte la vue personnalisé que l'on a crée à notre AlertDialog
                adb.setView(alertDialogView);

                //On donne un titre à l'AlertDialog
                adb.setTitle("Insérer un coureur manuellement");

                EditText editNom=alertDialogView.findViewById(R.id.editNomCoureur);
                EditText editPrenom=alertDialogView.findViewById(R.id.editPrenomCoureur);

                //On affecte un bouton "OK" à notre AlertDialog et on lui affecte un évènement
                adb.setPositiveButton("Ok", (dialog, which) -> {

                    String name=editNom.getText().toString();
                    String prenom=editPrenom.getText().toString();

                    if(name.matches("") && prenom.matches("")){
                        dialog.dismiss();
                    }else {
                        temps = chrono.getText().toString();
                        nom = name + " " + prenom;
                        place = place + 1;
                        c = new Coureur(nom, place, temps, nomCourse);
                        base.addCoureur(c);
                        listcoureur.add(0, c);
                        listCoureurAdapter adapter = new listCoureurAdapter(Course.this, R.layout.liste_coureur, listcoureur);
                        listView.setAdapter(adapter);
                    }

                });

                //On crée un bouton "Annuler" à notre AlertDialog et on lui affecte un évènement
                adb.setNegativeButton("Annuler", (dialog, which) -> dialog.dismiss());

                adb.show();
            }
        });

     /*   listView.setOnItemLongClickListener((parent, view, position, id) -> {

            Coureur coureur =(Coureur)listView.getItemAtPosition(position);
            listcoureur.remove(coureur);
            base.deleteCoureur(coureur);
            listCoureurAdapter listcoureuradapter = new listCoureurAdapter(Course.this, android.R.layout.simple_list_item_1, listcoureur);
            listView.setAdapter(listcoureuradapter);

            return false;
        });
    */
    }

    private final BarcodeCallback callback = new BarcodeCallback() {
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
                    base.addCoureur(c);
                    listcoureur.add(0,c);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        base.closeDb();
    }

    @Override
    public void onBackPressed() {

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

        }else{
            super.onBackPressed();
        }
    }
}