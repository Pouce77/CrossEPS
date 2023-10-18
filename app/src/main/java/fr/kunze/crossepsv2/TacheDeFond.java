package fr.kunze.crossepsv2;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.graphics.Rect;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class TacheDeFond extends AsyncTask <ArrayList<String>,Integer,Void>{

    ArrayList arrayList;
    Rect contentRect;
    Context context;
    PdfDocument document;
    int nbrePage;
    String nomFichier;
    PdfDocument.PageInfo pageInfo;
    ProgressDialog progressBar;
    int tour;
    Bitmap logo;

    public TacheDeFond(ArrayList<String> arrayList,Bitmap logo,Context context , String nomFichier) {

        this.arrayList = arrayList;
        this.context = context;
        this.nomFichier = nomFichier;
        this.contentRect = new Rect(20, 20, 575, 822);
        this.document = new PdfDocument();
        pageInfo=new PdfDocument.PageInfo.Builder(595,842,1).setContentRect(contentRect).create();
        this.tour = 0;
        this.nbrePage = arrayList.size() / 2;
        this.logo=logo;
    }


    @SafeVarargs
    @Override
    protected final Void doInBackground(ArrayList<String>... arrayLists) {


        ArrayList<String> arrayList1=arrayLists[0];
        for (tour=0;tour<arrayList1.size();tour=tour+2){
            PdfDocument.Page page=document.startPage(pageInfo);
            String nom1=arrayList1.get(tour);
            String nom2="";
            if (tour+1<arrayList1.size()){
            nom2=arrayList1.get(tour+1);
            }
            MyViewListe myViewListe=new MyViewListe(context,nom1,nom2,logo);
            myViewListe.draw(page.getCanvas());
            document.finishPage(page);
            publishProgress(tour);

        }

        File folder= new File(context.getApplicationContext().getFilesDir()+ File.separator +"app_cross");
        if(!folder.exists()){

            folder.mkdir();
        }

        File file=new File(folder,this.nomFichier+".pdf");
        int n=0;
        try{
            FileOutputStream out=new FileOutputStream(file);
            document.writeTo(out);

            if (out != null)
                out.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        document.close();



        return null;
    }


    @Override
    protected void onPreExecute() {

        progressBar = new ProgressDialog(this.context);
        progressBar.setCancelable(false);
        progressBar.setButton("Annuler", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                progressBar.dismiss();
                Toast.makeText(context,"Création du fichier annulée...",Toast.LENGTH_LONG).show();


            }
        });
        
        progressBar.setMessage("Le fichier est en cours de création ...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

        progressBar.setMax(nbrePage*2);
        progressBar.show();

       super.onPreExecute();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        progressBar.setProgress(values[0]);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        Toast.makeText(context,"Vous avez créer le fichier : "+this.nomFichier+".pdf !",Toast.LENGTH_LONG).show();
        progressBar.dismiss();
    }

    @Override
    protected void onCancelled() {

        Toast.makeText(context,"Création du fichier annulée...",Toast.LENGTH_LONG).show();
        super.onCancelled();

    }
}
