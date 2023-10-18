package fr.kunze.crossepsv2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    CoureurDAO basecourse;
    ArrayList<String> liste;
    RecyclerViewAdapter ladapter;
    RecyclerView.LayoutManager layoutManager;

    public static final int PERMISSION_REQUEST = 1000;
    public static final String NOM_COURSE = "fr.kunze.nomCourse";
    public static final String NOM_COURSE2 = "fr.kunze.nomCourse2";

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        if (ActivityCompat.checkSelfPermission(MainActivity.this,Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_MEDIA_AUDIO)   !=PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_MEDIA_IMAGES)   !=PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_MEDIA_VIDEO)   !=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.READ_MEDIA_AUDIO,Manifest.permission.READ_MEDIA_IMAGES,Manifest.permission.READ_MEDIA_VIDEO},
                  PERMISSION_REQUEST );
        }

        basecourse = new CoureurDAO(MainActivity.this);
        basecourse.openDb();

        recyclerView=findViewById(R.id.recyclerView);

        liste = new ArrayList<>();
        liste = basecourse.getlistNomCourse();

        ladapter=new RecyclerViewAdapter(liste,MainActivity.this);
        layoutManager=new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(ladapter);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(MainActivity.this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent i = new Intent(MainActivity.this, Resultats.class);
                i.putExtra(NOM_COURSE2, liste.get(position));
                startActivity(i);
                overridePendingTransition(R.anim.slideright,R.anim.slideoutleft);
            }
            @Override
            public void onLongItemClick(View view, int position) {
            }
        }));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.aide) {

            LayoutInflater factory = LayoutInflater.from(MainActivity.this);
            final View alertDialogView = factory.inflate(R.layout.boite_de_dialogue_aide_main, null);

            //Création de l'AlertDialog
            final AlertDialog.Builder adb = new AlertDialog.Builder(MainActivity.this);

            //On affecte la vue personnalisé que l'on a crée à notre AlertDialog
            adb.setView(alertDialogView);

            //On donne un titre à l'AlertDialog
            adb.setTitle("Aide");

            //On affecte un bouton "OK" à notre AlertDialog et on lui affecte un évènement
            adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {


                }

            });

            adb.setNeutralButton("Créer les dossards", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    Intent i=new Intent(MainActivity.this,CreerDossard.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.slideright,R.anim.slideoutleft);
                }
            });

            adb.show();

            return true;
        }

        if (id == R.id.go) {

            LayoutInflater factory = LayoutInflater.from(MainActivity.this);
            final View alertDialogView = factory.inflate(R.layout.boite_de_dialogue_nomcourse, null);

            //Création de l'AlertDialog
            final AlertDialog.Builder adb = new AlertDialog.Builder(MainActivity.this);

            //On affecte la vue personnalisé que l'on a crée à notre AlertDialog
            adb.setView(alertDialogView);

            //On donne un titre à l'AlertDialog
            adb.setTitle("Créer une course");

            //On affecte un bouton "OK" à notre AlertDialog et on lui affecte un évènement
            adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                    EditText nomCourse = (EditText) alertDialogView.findViewById(R.id.nomCourse);
                    String course = nomCourse.getText().toString();
                    String newcourse="";
                    if(course.contains("/")) {
                       newcourse = course.replace("/", " ");
                    }else{
                        newcourse=course;
                    }
                        Intent i = new Intent(alertDialogView.getContext(), Course.class);
                        i.putExtra(NOM_COURSE, newcourse);
                        startActivity(i);
                        overridePendingTransition(R.anim.slideright, R.anim.slideoutleft);
                }

            });

            //On crée un bouton "Annuler" à notre AlertDialog et on lui affecte un évènement
            adb.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();
                }
            });

            adb.show();

            return true;
        }

        if (id == R.id.creerDossard) {

            Intent i=new Intent(MainActivity.this,CreerDossard.class);
            startActivity(i);
            overridePendingTransition(R.anim.slideright,R.anim.slideoutleft);
            return true;
        }

        if (id == R.id.supprimerCourse) {


            LayoutInflater factory = LayoutInflater.from(MainActivity.this);
            final View alertDialogView = factory.inflate(R.layout.boite_de_dialogue_supprimer_course, null);

            //Création de l'AlertDialog
            final AlertDialog.Builder adb = new AlertDialog.Builder(MainActivity.this);

            //On affecte la vue personnalisé que l'on a crée à notre AlertDialog
            adb.setView(alertDialogView);

            //On donne un titre à l'AlertDialog
            adb.setTitle("Supprimer une course");

            Spinner spinner=alertDialogView.findViewById(R.id.spinnerFichierPDF);
            ArrayAdapter adapter=new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1,liste);
            spinner.setAdapter(adapter);

            //On affecte un bouton "OK" à notre AlertDialog et on lui affecte un évènement
            adb.setPositiveButton("Continuer", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                    if (liste.isEmpty()){
                        Toast.makeText(alertDialogView.getContext(),"Il n'y a pas de course à supprimer.",Toast.LENGTH_LONG).show();
                    }else {
                        basecourse.supprimerCourse(spinner.getSelectedItem().toString());
                        liste = basecourse.getlistNomCourse();
                        ladapter = new RecyclerViewAdapter(liste, MainActivity.this);
                        layoutManager = new LinearLayoutManager(MainActivity.this);
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(ladapter);
                    }

                }

            });

            //On crée un bouton "Annuler" à notre AlertDialog et on lui affecte un évènement
            adb.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();
                }
            });

            adb.show();


            return true;
        }

        if (id == R.id.listeDossard) {

            Intent i=new Intent (MainActivity.this,ListePDF.class);
            startActivity(i);
            overridePendingTransition(R.anim.slideright,R.anim.slideoutleft);
            finish();
            return true;
        }

        if (id == R.id.logo) {

            Intent i=new Intent (MainActivity.this,Logo.class);
            startActivity(i);
            overridePendingTransition(R.anim.slideright,R.anim.slideoutleft);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(this, "Permissions accordées.", Toast.LENGTH_SHORT).show();
            } else {

                Toast.makeText(this, "Permission refusée !", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        basecourse.closeDb();
    }
}