package fr.kunze.crossepsv2;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

public class CreerListe extends AppCompatActivity {

    Button importer;
    Button ajouter;
    Button generer;
    ListView listView;
    ArrayList<String> arrayList;
    String nomFichier;
    TacheDeFond tacheDeFond;
    String nom;
    String newnom;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creer_liste);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        importer=findViewById(R.id.buttonImporter);
        ajouter=findViewById(R.id.buttonAjouter);
        generer=findViewById(R.id.buttonGenererListeDossards);
        listView=findViewById(R.id.listViewdossards);

        arrayList=new ArrayList<>();
        arrayList=getIntent().getStringArrayListExtra("listeNoms");

        if (arrayList==null) {

        }else{
            ArrayAdapter adapter = new ArrayAdapter(CreerListe.this, android.R.layout.simple_list_item_1, arrayList);
            listView.setAdapter(adapter);
        }

        listView.setOnItemLongClickListener((parent, view, position, id) -> {

            arrayList.remove(listView.getItemAtPosition(position));
            ArrayAdapter adapter = new ArrayAdapter(CreerListe.this, android.R.layout.simple_list_item_1, arrayList);
            listView.setAdapter(adapter);

            return false;
        });


        importer.setOnClickListener(v -> {

            Intent i=new Intent(CreerListe.this,Importer.class);
            overridePendingTransition(R.anim.slideleft,R.anim.slideoutright);
            startActivity(i);
            finish();

        });

        ajouter.setOnClickListener(v -> {

            LayoutInflater factory = LayoutInflater.from(CreerListe.this);
            final View alertDialogView = factory.inflate(R.layout.boite_de_dialogue_ajouter_manuellement, null);

            //Création de l'AlertDialog
            final AlertDialog.Builder adb = new AlertDialog.Builder(CreerListe.this);

            //On affecte la vue personnalisé que l'on a crée à notre AlertDialog
            adb.setView(alertDialogView);

            //On donne un titre à l'AlertDialog
            adb.setTitle("Ajouter un participant dans la liste");
            EditText editTextNom=alertDialogView.findViewById(R.id.editNomlist);
            EditText editTextPrenom=alertDialogView.findViewById(R.id.editPrenomList);
            EditText editTextClasse=alertDialogView.findViewById(R.id.editClasselist);
            EditText editTextCategorie=alertDialogView.findViewById(R.id.editcategorielist);
            EditText editTextAutre1=alertDialogView.findViewById(R.id.editAutre1list);
            EditText editTextAutre2=alertDialogView.findViewById(R.id.editAutre2list);

            //On affecte un bouton "OK" à notre AlertDialog et on lui affecte un évènement
            adb.setPositiveButton("Ok", (dialog, which) -> {

                nom=editTextNom.getText().toString()+" "+editTextPrenom.getText().toString()+" "+editTextClasse.getText().toString()+" "+editTextCategorie.getText().toString()+" "+editTextAutre1.getText().toString()+" "+editTextAutre2.getText().toString();
                if(nom.matches("     ")){
                   Toast.makeText(alertDialogView.getContext(),"Vous n'avez entré aucune donnée !",Toast.LENGTH_LONG).show();
               }else {

                   if(nom.contains("/")) {
                       newnom=nom.replace("/","-");
                   }else {
                       newnom=nom;
                   }
                       if (arrayList==null) {
                           arrayList = new ArrayList<>();
                       }
                       arrayList.add(newnom);
                       ArrayAdapter adapter = new ArrayAdapter(alertDialogView.getContext(), android.R.layout.simple_list_item_1, arrayList);
                       listView.setAdapter(adapter);
               }

            });

            //On crée un bouton "Annuler" à notre AlertDialog et on lui affecte un évènement
            adb.setNegativeButton("Annuler", (dialog, which) -> dialog.dismiss());

            adb.show();

        });

        generer.setOnClickListener(v -> {

            LayoutInflater factory = LayoutInflater.from(CreerListe.this);
            final View alertDialogView = factory.inflate(R.layout.boite_de_dialogue_nomfichier, null);

            //Création de l'AlertDialog
            final AlertDialog.Builder adb = new AlertDialog.Builder(CreerListe.this);

            //On affecte la vue personnalisé que l'on a crée à notre AlertDialog
            adb.setView(alertDialogView);

            //On donne un titre à l'AlertDialog
            adb.setTitle("Donnez un nom à votre fichier");

            EditText editText=alertDialogView.findViewById(R.id.editTextNomfichier);

            //On affecte un bouton "OK" à notre AlertDialog et on lui affecte un évènement
            adb.setPositiveButton("Ok", (dialog, which) -> {

                nomFichier = editText.getText().toString();
                if (nomFichier.matches("")){
                    Toast.makeText(alertDialogView.getContext(),"Vous devez entrer un nom de fichier !",Toast.LENGTH_LONG).show();
                }else {
                    if (nomFichier.contains("/")) {
                        Toast.makeText(alertDialogView.getContext(), "Vous ne pouvez pas utiliser de caractère spécial pour votre nom de fichier", Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    } else {
                        tacheDeFond = new TacheDeFond(arrayList, CreerListe.this, nomFichier);
                        tacheDeFond.execute(arrayList);

                    }
                }
            });

            //On crée un bouton "Annuler" à notre AlertDialog et on lui affecte un évènement
            adb.setNegativeButton("Annuler", (dialog, which) -> dialog.dismiss());

            adb.show();


        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_un_dossard, menu);
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

            LayoutInflater factory = LayoutInflater.from(CreerListe.this);
            final View alertDialogView = factory.inflate(R.layout.boite_de_dialogue_aide_creerliste, null);

            //Création de l'AlertDialog
            final AlertDialog.Builder adb = new AlertDialog.Builder(CreerListe.this);

            //On affecte la vue personnalisé que l'on a crée à notre AlertDialog
            adb.setView(alertDialogView);

            //On donne un titre à l'AlertDialog
            adb.setTitle("Aide");

            //On affecte un bouton "OK" à notre AlertDialog et on lui affecte un évènement
            adb.setPositiveButton("OK", (dialog, which) -> {


            });

            adb.show();

            return true;

        }

        if (id == R.id.home) {

            Intent i=new Intent(CreerListe.this,MainActivity.class);
            overridePendingTransition(R.anim.slideright,R.anim.slideoutleft);
            startActivity(i);
            finish();

            return true;
        }

        if (id == R.id.listePDF) {

            Intent i=new Intent(CreerListe.this,ListePDF.class);
            overridePendingTransition(R.anim.slideright,R.anim.slideoutleft);
            startActivity(i);
            finish();
            return true;

        }

        return super.onOptionsItemSelected(item);
    }

}