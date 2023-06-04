package fr.kunze.crossepsv2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Importer extends AppCompatActivity {

    private boolean mCountdown = false;
    ListView list;
    File mCurrentFile;
    FileAdapter madapter = null;
    public static final String LISTE = "listeNoms";
    ArrayList<String> arrayList;

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_importer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        list = (ListView) findViewById(R.id.listFichier);
        arrayList=new ArrayList<>();

        //mCurrentFile = Environment.getExternalStorageDirectory();

        mCurrentFile=getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);

        File[] fichiers = mCurrentFile.listFiles();

        ArrayList<File> liste = new ArrayList<File>();
        for (File f : fichiers)
            {
            if(f.isDirectory()){
                liste.add(f);
            }
           else if(f.getName().endsWith("csv"))
            {
                liste.add(f);
            }
            else
            {}
        }

        madapter = new FileAdapter(Importer.this, android.R.layout.simple_list_item_1, liste);
        list.setAdapter(madapter);
        madapter.sort();

        // On ajoute un Listener sur les items de la liste
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            // Que se passe-t-il en cas de clic sur un élément de la liste ?
            public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
                final File fichier = madapter.getItem(position);
                // Si le fichier est un répertoire…
                if (fichier.isDirectory()) {
                    // On change de répertoire courant
                    updateDirectory(fichier);

                } else {

                    if (fichier.getName().endsWith("csv")) {

                        LayoutInflater factory = LayoutInflater.from(Importer.this);
                        final View alertDialogView = factory.inflate(R.layout.boite_de_dialogue_ouvrir_fichier, null);

                        TextView textview = (TextView) alertDialogView.findViewById(R.id.textView2);
                        textview.setText("Voulez-vous utiliser le fichier : " + fichier + " pour créer une liste ?");
                        //Création de l'AlertDialog
                        final AlertDialog.Builder adb = new AlertDialog.Builder(Importer.this);

                        //On affecte la vue personnalisé que l'on a crée à notre AlertDialog
                        adb.setView(alertDialogView);

                        //On donne un titre à l'AlertDialog
                        adb.setTitle("Choix du fichier");


                        //On affecte un bouton "OK" à notre AlertDialog et on lui affecte un évènement
                        adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                BufferedReader bufferedReader = null;
                                InputStreamReader reader = null;
                                try {
                                    FileInputStream is = new FileInputStream(new File(fichier.getAbsolutePath()));
                                    reader = new InputStreamReader(is);
                                    bufferedReader = new BufferedReader(reader);
                                    String line = null;
                                    while ((line = bufferedReader.readLine()) != null) {
                                        String[] data = line.split(";");
                                        String nom = "";
                                        try {
                                            if (data != null) {

                                                for (int i = 0; i < data.length; i++) {

                                                    nom = nom + " " + data[i];
                                                }
                                                arrayList.add(nom);
                                            }
                                        }catch (ArrayIndexOutOfBoundsException e){
                                            Toast.makeText(Importer.this,"Le fichier n'est pas formaté correctement !", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    }

                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } finally {
                                    if (bufferedReader != null) {
                                        try {
                                            bufferedReader.close();
                                            reader.close();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }


                                Intent i = new Intent(Importer.this, CreerListe.class);
                                i.putStringArrayListExtra(LISTE, arrayList);
                                startActivity(i);
                                finish();
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
                        Toast.makeText(Importer.this, "Vous devez selectionner un fichier au format CSV.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabPrint);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File parent = mCurrentFile.getParentFile();
                // S'il y a effectivement un parent
                if (parent != null)
                    updateDirectory(parent);
                else {
                    // Sinon, si c'est la première fois qu'on fait un retour arrière
                    if (mCountdown != true) {
                        // On indique à l'utilisateur qu'appuyer dessus une seconde fois le fera sortir
                        Toast.makeText(Importer.this, "Nous sommes déjà à la racine ! Cliquez une seconde fois pour quitter", Toast.LENGTH_SHORT).show();
                        mCountdown = true;
                    } else
                        // Si c'est la seconde fois, on sort effectivement
                        finish();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_explorateur, menu);
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
            LayoutInflater factory = LayoutInflater.from(Importer.this);
            final View alertDialogView = factory.inflate(R.layout.boit_de_dialogue_aide_csv, null);

            //Création de l'AlertDialog
            final AlertDialog.Builder adb = new AlertDialog.Builder(Importer.this);

            //On affecte la vue personnalisé que l'on a crée à notre AlertDialog
            adb.setView(alertDialogView);

            //On donne un titre à l'AlertDialog
            adb.setTitle("Aide sur le fichier csv");


            //On affecte un bouton "OK" à notre AlertDialog et on lui affecte un évènement
            adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();
                }

            });


            adb.show();

            return true;
        }
        if (id == R.id.home) {

            Intent i = new Intent(Importer.this, CreerListe.class);
            overridePendingTransition(R.anim.slideright,R.anim.slideoutleft);
            startActivity(i);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * On enlève tous les éléments de la liste
     */

    public void setEmpty() {
        // Si l'adaptateur n'est pas vide…
        if (!madapter.isEmpty())
            // Alors on le vide !
            madapter.clear();
    }

    /**
     * Utilisé pour naviguer entre les répertoires
     *
     * @param pFile le nouveau répertoire dans lequel aller
     */

    public void updateDirectory(File pFile) {
        // On change le titre de l'activité
        setTitle(pFile.getAbsolutePath());

        // On change le répertoire actuel
        mCurrentFile = pFile;
        // On vide les répertoires actuels
        setEmpty();

        // On récupère la liste des fichiers du nouveau répertoire
        File[] fichiers = mCurrentFile.listFiles();

        // Si le répertoire n'est pas vide…
        if (fichiers != null) {
            // On les ajoute à  l'adaptateur
            for (File f : fichiers){
                if(f.isDirectory()||f.getName().endsWith("csv")) {
                    madapter.add(f);
                }else{}
            madapter.sort();
            }
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Si on a appuyé sur le retour arrière
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // On prend le parent du répertoire courant
            File parent = mCurrentFile.getParentFile();
            // S'il y a effectivement un parent
            if (parent != null)
                updateDirectory(parent);
            else {
                // Sinon, si c'est la première fois qu'on fait un retour arrière
                if (mCountdown != true) {
                    // On indique à l'utilisateur qu'appuyer dessus une seconde fois le fera sortir
                    Toast.makeText(this, "Nous sommes déjà à la racine ! Cliquez une seconde fois pour quitter", Toast.LENGTH_SHORT).show();
                    mCountdown = true;
                } else
                    // Si c'est la seconde fois, on sort effectivement
                    finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}

