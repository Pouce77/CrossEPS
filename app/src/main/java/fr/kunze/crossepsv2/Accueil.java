package fr.kunze.crossepsv2;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class Accueil extends AppCompatActivity {

    ImageButton image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);

        image=findViewById(R.id.imageAnime);
        Animation anim= AnimationUtils.loadAnimation(Accueil.this,R.anim.rotate);
        anim.setRepeatCount(4);
        image.setAnimation(anim);

      new Thread(() -> {

         try {
              Thread.sleep(2000);

          Intent intent = new Intent(Accueil.this, MainActivity.class);
          startActivity(intent);
          overridePendingTransition(R.anim.slideright, R.anim.slideoutleft);
              finish();
          }catch (InterruptedException ignored){}

      }).start();

            }
        }
