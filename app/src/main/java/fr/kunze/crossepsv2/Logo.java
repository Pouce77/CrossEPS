package fr.kunze.crossepsv2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class Logo extends AppCompatActivity {

    Button select;
    Button useImage;
    ImageView imageLogo;
    FloatingActionButton fab;

    Bitmap bitmap;
    Uri uri;

    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);

        select=findViewById(R.id.selectFile);
        useImage=findViewById(R.id.useFile);
        fab=findViewById(R.id.fabLogo);
        imageLogo=findViewById(R.id.imageLogo);

        File folder= new File(getApplicationContext().getFilesDir() +
                File.separator + "app_cross");
        String[] files=folder.list();
        boolean fileExist=false;
        if(files!=null) {
            for (String file : files) {
                if (file.matches("logo.png")) {
                    fileExist = true;
                }
            }
        }

        if(!fileExist) {
            AssetManager assetManager = Logo.this.getAssets();
            InputStream istr;
            try {
                istr = assetManager.open("logo.png");
                bitmap = BitmapFactory.decodeStream(istr);
            } catch (IOException e) {
                // handle exception
            }
            try {
                bitmap.compress(Bitmap.CompressFormat.PNG,100,new FileOutputStream(getApplicationContext().getFilesDir()+"/app_cross/logo.png"));
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }else {
            Log.e("File exist","Le fichier existe !");
            bitmap=BitmapFactory.decodeFile(getApplicationContext().getFilesDir()+"/app_cross/logo.png");
            imageLogo.setImageBitmap(bitmap);
        }
        useImage.setEnabled(false);

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            openDocument(Uri.parse(Environment.DIRECTORY_DOCUMENTS));
            useImage.setEnabled(true);

            }
        });

        useImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    bitmap.compress(Bitmap.CompressFormat.PNG,100,new FileOutputStream(getApplicationContext().getFilesDir()+"/app_cross/logo.png"));
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }

                Toast.makeText(Logo.this,"Le logo est maintenant disponible sur vos dossards",Toast.LENGTH_LONG).show();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Logo.this,CreerListe.class);
                startActivity(i);
                overridePendingTransition(R.anim.slideleft,R.anim.slideoutright);
                finish();
            }
        });
    }

    private static final int PICK_FILE = 2;
    private void openDocument(Uri pickerInitialUri) {

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");

        // Optionally, specify a URI for the file that should appear in the
        // system file picker when it loads.
        intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri);

        startActivityForResult(intent, PICK_FILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FILE) {
            if(resultCode == Activity.RESULT_OK){
                uri=data.getData();

                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    imageLogo.setImageBitmap(bitmap);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(Logo.this,"Aucune fichier reçu",Toast.LENGTH_LONG).show();
            }
        }
    } //onActivityResult
}