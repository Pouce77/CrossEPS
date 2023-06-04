package fr.kunze.crossepsv2;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.io.File;
import java.util.ArrayList;

public class ListeCSV extends AppCompatActivity {

    RecyclerView listView;
    ArrayList<String> arrayList;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_c_s_v);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView=findViewById(R.id.listViewCSV);
        arrayList=new ArrayList<>();

        File folder= new File(getApplicationContext().getFilesDir() +
                File.separator + "app_cross");

        File[]file=folder.listFiles();

        if (file!=null){
            for (int i=0; i<file.length;i++){
                if (file[i].getName().endsWith("csv")){

                    arrayList.add(file[i].getName());
                }

            }
        }

        RecyclerViewAdapterFichier ladapter=new RecyclerViewAdapterFichier(arrayList,ListeCSV.this);
        layoutManager=new LinearLayoutManager(ListeCSV.this);
        listView.setLayoutManager(layoutManager);
        listView.setItemAnimator(new DefaultItemAnimator());
        listView.setAdapter(ladapter);

        FloatingActionButton fab = findViewById(R.id.fabPrint);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i=new Intent(ListeCSV.this,MainActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slideleft,R.anim.slideoutright);
                finish();

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

            Intent i=new Intent(ListeCSV.this,MainActivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.slideleft,R.anim.slideoutright);
            finish();

            return true;
        }



        return super.onOptionsItemSelected(item);
    }
}