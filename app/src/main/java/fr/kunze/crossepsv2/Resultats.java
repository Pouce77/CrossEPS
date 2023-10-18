package fr.kunze.crossepsv2;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class Resultats extends AppCompatActivity {

    CoureurDAO basecourse;
    ListView listeResultats;
    String nomCourse;
    private static final int MY_PERMISSIONS_REQUEST_WRITE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultats);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setHomeButtonEnabled(true);
        ab.setDisplayUseLogoEnabled(true);

        Intent intent=getIntent();
        nomCourse = intent.getStringExtra(MainActivity.NOM_COURSE2);
        ab.setTitle(nomCourse);

        basecourse=new CoureurDAO(this);
        basecourse.openDb();
        listeResultats=findViewById(R.id.listResultats);

        ArrayList<HashMap<String, String>> e = basecourse.getListeCoureur(nomCourse);

        SimpleAdapter eadapter = new SimpleAdapter(Resultats.this, e, R.layout.liste_item_perso, new String[]{"place", "nom"}, new int[]{R.id.textView3, R.id.textView4});
        listeResultats.setAdapter(eadapter);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_resultats, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == R.id.csvfile) {

            if(android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {

                if (ContextCompat.checkSelfPermission(Resultats.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    Log.e("PErmissison", "pas accordée");

                    if (ActivityCompat.shouldShowRequestPermissionRationale(Resultats.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        //Cela signifie que la permission à déjà était
                        //demandé et l'utilisateur l'a refusé
                        //Vous pouvez aussi expliquer à l'utilisateur pourquoi
                        //cette permission est nécessaire et la redemander
                        LayoutInflater factory = LayoutInflater.from(Resultats.this);
                        final View alertDialogView = factory.inflate(R.layout.boite_de_dialogue_permission_ecrire, null);
                        //Création de l'AlertDialog
                        final android.app.AlertDialog.Builder adb = new android.app.AlertDialog.Builder(Resultats.this);
                        //On affecte la vue personnalisé que l'on a crée à notre AlertDialog
                        adb.setView(alertDialogView);
                        //On donne un titre à l'AlertDialog
                        adb.setTitle("Permission");
                        //On affecte un bouton "OK" à notre AlertDialog et on lui affecte un évènement
                        adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                ActivityCompat.requestPermissions(Resultats.this,
                                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        MY_PERMISSIONS_REQUEST_WRITE);

                            }
                        });
                        //On crée un bouton "Annuler" à notre AlertDialog et on lui affecte un évènement
                        adb.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //Lorsque l'on cliquera sur annuler on quittera l'application
                                dialog.dismiss();
                            }
                        });
                        adb.show();

                    } else {
                        //Sinon demander la permission
                        ActivityCompat.requestPermissions(Resultats.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                MY_PERMISSIONS_REQUEST_WRITE);
                    }
                } else {
                    Log.e("Permission déjà accordé","Déjà accordée");
                    openDialogBox();
                }
            }else{
                Log.e("Build.version","Tiramisu");
                openDialogBox();
            }
            return true;
        }

        if (id == R.id.aide) {

            LayoutInflater factory = LayoutInflater.from(Resultats.this);
            final View alertDialogView = factory.inflate(R.layout.boite_de_dialogue_aide_resultats, null);
            //Création de l'AlertDialog
            final AlertDialog.Builder adb = new AlertDialog.Builder(Resultats.this);
            //On affecte la vue personnalisé que l'on a crée à notre AlertDialog
            adb.setView(alertDialogView);
            //On donne un titre à l'AlertDialog
            adb.setTitle("Aide");
            //On affecte un bouton "OK" à notre AlertDialog et on lui affecte un évènement
            adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            adb.show();

            return true;
        }
        if (id == R.id.listefichiercsv) {

            Intent i=new Intent(Resultats.this,ListeCSV.class);
            startActivity(i);
            overridePendingTransition(R.anim.slideleft,R.anim.slideoutright);
            return true;
        }

        if (id == R.id.home) {

            Intent i=new Intent(Resultats.this,MainActivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.slideright,R.anim.slideoutleft);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        basecourse.closeDb();
    }

    public void ecrireDansCSV(String data, String filename) {

        File folder= new File(getApplicationContext().getFilesDir() +
                File.separator + "app_cross");
        if(!folder.exists()){

            folder.mkdir();
        }

        File file=new File(folder,filename);
        int n=0;
        try{
            FileOutputStream out=new FileOutputStream(file);
            out.write(data.getBytes());

            if (out != null)
                out.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[],
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // La permission est garantie
                    Log.e("Permisssion", "accordée");
                    openDialogBox();
                } else {
                    // La permission est refusée
                    Toast.makeText(Resultats.this, "Vous avez refusé la permission d'écrire sur l'appareil.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public void openDialogBox (){

        LayoutInflater factory = LayoutInflater.from(Resultats.this);
        final View alertDialogView = factory.inflate(R.layout.boite_de_dialogue_ecrire_csv, null);
        //Création de l'AlertDialog
        final AlertDialog.Builder adb = new AlertDialog.Builder(Resultats.this);
        //On affecte la vue personnalisé que l'on a crée à notre AlertDialog
        adb.setView(alertDialogView);
        //On donne un titre à l'AlertDialog
        adb.setTitle("Créer un fichier au format CSV");
        //On affecte un bouton "OK" à notre AlertDialog et on lui affecte un évènement
        adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                Cursor c = basecourse.getCursorResultatCourse(nomCourse);
                String data = "";
                while (c.moveToNext()) {
                    String chaine = c.getString(1).replace(" ", " ");
                    data = data + c.getString(0) + ";" + chaine + ";" + (c.getString(2)) + "\n";
                }
                String timeStamp = new SimpleDateFormat("yyyyMMddhhmmss", Locale.getDefault()).format(new Date());
                String filname = nomCourse + "_" + timeStamp + ".csv";
                Log.e("data", data);
                ecrireDansCSV(data, filname);

                Toast.makeText(Resultats.this, "Course enregistrée dans le fichier " + filname, Toast.LENGTH_LONG).show();
            }
        });
        //On crée un bouton "Annuler" à notre AlertDialog et on lui affecte un évènement
        adb.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });
        adb.show();
    }

}