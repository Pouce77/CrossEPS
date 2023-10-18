package fr.kunze.crossepsv2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class ListePDF extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton fabPrint;
    FloatingActionButton fabDelete;
    ArrayList<String> arrayList;
    RecyclerView.LayoutManager layoutManager;
    RecyclerViewAdapterFichier ladapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_p_d_f);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView=findViewById(R.id.listViewPDF);
        fabPrint=findViewById(R.id.fabPrint2);
        fabDelete=findViewById(R.id.fabDelete);

        arrayList=new ArrayList<>();

        File folder= new File(getApplicationContext().getFilesDir() +
                File.separator + "app_cross");

        File[]file=folder.listFiles();

        if (file!=null){
            for (int i=0; i<file.length;i++){
                if (file[i].getName().endsWith("pdf")){

                    arrayList.add(file[i].getName());
                }
            }
        }

        ladapter=new RecyclerViewAdapterFichier(arrayList,ListePDF.this);
        layoutManager=new LinearLayoutManager(ListePDF.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(ladapter);

        fabPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater factory = LayoutInflater.from(ListePDF.this);
                final View alertDialogView = factory.inflate(R.layout.boite_de_dialogue_imprimerpdf, null);

                //Création de l'AlertDialog
                final AlertDialog.Builder adb = new AlertDialog.Builder(ListePDF.this);

                //On affecte la vue personnalisé que l'on a crée à notre AlertDialog
                adb.setView(alertDialogView);

                //On donne un titre à l'AlertDialog
                adb.setTitle("Imprimer un fichier PDF");

                Spinner spinner=alertDialogView.findViewById(R.id.spinnerFichierPDFprint);
                ArrayAdapter adapter=new ArrayAdapter(ListePDF.this, android.R.layout.simple_list_item_1,arrayList);
                spinner.setAdapter(adapter);

                //On affecte un bouton "OK" à notre AlertDialog et on lui affecte un évènement
                adb.setPositiveButton("Continuer", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    public void onClick(DialogInterface dialog, int which) {

                        PrintManager printManager=(PrintManager) ListePDF.this.getSystemService(Context.PRINT_SERVICE);
                        try
                        {
                            File folder= new File(getApplicationContext().getFilesDir() +
                                    File.separator + "app_cross"+File.separator+spinner.getSelectedItem().toString());
                            PrintDocumentAdapter printAdapter = new PdfDocumentAdapter(ListePDF.this,folder.getAbsolutePath());

                            assert printManager != null;
                            printManager.print("Document", printAdapter,new PrintAttributes.Builder().build());
                        }
                        catch (Exception e)
                        {

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

            }

        });

        fabDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                LayoutInflater factory = LayoutInflater.from(ListePDF.this);
                final View alertDialogView = factory.inflate(R.layout.boite_de_dialogue_supp_fichierpdf, null);

                //Création de l'AlertDialog
                final AlertDialog.Builder adb = new AlertDialog.Builder(ListePDF.this);

                //On affecte la vue personnalisé que l'on a crée à notre AlertDialog
                adb.setView(alertDialogView);

                //On donne un titre à l'AlertDialog
                adb.setTitle("Supprimer un fichier PDF");

                Spinner spinner=alertDialogView.findViewById(R.id.spinnerFichierPDF);
                ArrayAdapter adapter=new ArrayAdapter(ListePDF.this, android.R.layout.simple_list_item_1,arrayList);
                spinner.setAdapter(adapter);

                //On affecte un bouton "OK" à notre AlertDialog et on lui affecte un évènement
                adb.setPositiveButton("Continuer", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (arrayList.isEmpty()){
                            Toast.makeText(alertDialogView.getContext(),"Il n'y a pas de fichier à supprimer.",Toast.LENGTH_LONG).show();
                        }else {
                            File folder = new File(getApplicationContext().getFilesDir() +
                                    File.separator + "app_cross" + File.separator + spinner.getSelectedItem().toString());
                            folder.delete();
                            arrayList.remove(spinner.getSelectedItem().toString());

                            ladapter = new RecyclerViewAdapterFichier(arrayList, ListePDF.this);
                            layoutManager = new LinearLayoutManager(ListePDF.this);
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

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_fichier, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.home) {

            Intent i=new Intent(ListePDF.this,MainActivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.slideleft,R.anim.slideoutright);
            finish();

            return true;
        }


        return super.onOptionsItemSelected(item);
    }
}