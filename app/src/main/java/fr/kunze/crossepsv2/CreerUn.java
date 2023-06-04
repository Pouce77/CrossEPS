package fr.kunze.crossepsv2;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.zxing.WriterException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class CreerUn extends AppCompatActivity {

    EditText editnom;
    EditText editPrenom;
    EditText editClasse;
    EditText editCategorie;
    EditText editAutre1;
    EditText editAutre2;

    String chaine;
    String nomFichier;
    String newnom;

    Button generer;
    PdfDocument pdfDocument;
    PdfDocument.PageInfo pageInfo;
    Rect contentRect;

    MyView myView;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creer_un);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        editnom=findViewById(R.id.editNom);
        editPrenom=findViewById(R.id.editPrenom);
        editClasse=findViewById(R.id.editClasse);
        editCategorie=findViewById(R.id.editCategorie);
        editAutre1=findViewById(R.id.editAutre1);
        editAutre2=findViewById(R.id.editAutre2);

        generer=findViewById(R.id.buttonGenerer);


        generer.setOnClickListener(v -> {

            LayoutInflater factory = LayoutInflater.from(CreerUn.this);
            final View alertDialogView = factory.inflate(R.layout.boite_de_dialogue_creer_un, null);

            //Création de l'AlertDialog
            final AlertDialog.Builder adb = new AlertDialog.Builder(CreerUn.this);

            //On affecte la vue personnalisé que l'on a crée à notre AlertDialog
            adb.setView(alertDialogView);
            nomFichier=editnom.getText().toString()+"_"+editPrenom.getText().toString()+".pdf";
            if (nomFichier.contains("/")){
                newnom=nomFichier.replace("/"," ");
            }else{
                newnom=nomFichier;
            }
            //On donne un titre à l'AlertDialog
            adb.setTitle("Vous avez créer le dossard "+ newnom);

            chaine=editnom.getText().toString()+" "+editPrenom.getText().toString()+" "+editClasse.getText().toString()+" "+editCategorie.getText().toString()+" "+editAutre1.getText().toString()+" "+editAutre2.getText().toString();
            String newChaine="";
            if (chaine.contains("/")){
               newChaine=chaine.replace("/","-");
            }else{
                newChaine=chaine;
            }
            FrameLayout frameLayout=alertDialogView.findViewById(R.id.frame);
            try {
                myView = new MyView(alertDialogView.getContext(),newChaine);
            } catch (WriterException e) {
                e.printStackTrace();
            }
            frameLayout.addView(myView);

            //On affecte un bouton "OK" à notre AlertDialog et on lui affecte un évènement
            adb.setPositiveButton("Continuer", (dialog, which) -> {

            contentRect=new Rect(20,20,585,822);
            pdfDocument=new PdfDocument();
            pageInfo=new PdfDocument.PageInfo.Builder(595,842,1).setContentRect(contentRect).create();

            PdfDocument.Page page=pdfDocument.startPage(pageInfo);
            myView.draw(page.getCanvas());
            pdfDocument.finishPage(page);
                File folder= new File(getApplicationContext().getFilesDir()+
                        File.separator + "app_cross");
                if(!folder.exists()){

                    folder.mkdir();
                }

                File file=new File(folder,newnom);
                int n=0;
                try{
                    FileOutputStream out=new FileOutputStream(file);
                   pdfDocument.writeTo(out);

                    if (out != null)
                        out.close();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            pdfDocument.close();
                Toast.makeText(alertDialogView.getContext(),"Vous avez créer le fichier : "+newnom+" dans le dossier app_cross !",Toast.LENGTH_LONG).show();
            });

            //On crée un bouton "Annuler" à notre AlertDialog et on lui affecte un évènement
            adb.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();
                }
            });

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

            LayoutInflater factory = LayoutInflater.from(CreerUn.this);
            final View alertDialogView = factory.inflate(R.layout.boite_de_dialogue_aide_creerun, null);

            //Création de l'AlertDialog
            final AlertDialog.Builder adb = new AlertDialog.Builder(CreerUn.this);

            //On affecte la vue personnalisé que l'on a crée à notre AlertDialog
            adb.setView(alertDialogView);

            //On donne un titre à l'AlertDialog
            adb.setTitle("Aide");

            //On affecte un bouton "OK" à notre AlertDialog et on lui affecte un évènement
            adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {


                }

            });

            adb.show();
            return true;

        }

        if (id == R.id.home) {

            Intent i=new Intent(CreerUn.this,MainActivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.slideleft,R.anim.slideoutright);
            finish();

            return true;
        }

        if (id == R.id.listePDF) {

            Intent i=new Intent(CreerUn.this,ListePDF.class);
            overridePendingTransition(R.anim.slideright,R.anim.slideoutleft);
            startActivity(i);
            finish();
            return true;

        }

        return super.onOptionsItemSelected(item);
    }
}