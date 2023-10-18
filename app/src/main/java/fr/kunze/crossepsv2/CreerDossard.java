package fr.kunze.crossepsv2;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class CreerDossard extends AppCompatActivity {
    Button creerUn;
    Button creerListe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creer_dossard);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        creerUn=findViewById(R.id.buttoncreerUn);
        creerListe=findViewById(R.id.buttoncreerListe);

        creerUn.setOnClickListener(v -> {

            Intent i=new Intent (CreerDossard.this,CreerUn.class);
            startActivity(i);
            overridePendingTransition(R.anim.slideright,R.anim.slideoutleft);
            finish();

        });

        creerListe.setOnClickListener(v -> {

            Intent i=new Intent (CreerDossard.this,CreerListe.class);
            startActivity(i);
            overridePendingTransition(R.anim.slideright,R.anim.slideoutleft);
            finish();

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_creerdossard, menu);
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
            
            Intent i=new Intent(CreerDossard.this,MainActivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.slideleft,R.anim.slideoutright);
            finish();
            
            return true;
        }

        if (id == R.id.listeDossardscrees) {

            Intent i=new Intent(CreerDossard.this,ListePDF.class);
            startActivity(i);
            overridePendingTransition(R.anim.slideright,R.anim.slideoutleft);
            finish();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}