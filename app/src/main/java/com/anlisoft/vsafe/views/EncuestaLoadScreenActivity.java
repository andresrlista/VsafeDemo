package com.anlisoft.vsafe.views;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.anlisoft.vsafe.R;

public class EncuestaLoadScreenActivity extends AppCompatActivity {

    private ImageButton imgButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        setContentView(R.layout.activity_encuesta_load_screen);


        imgButton = findViewById(R.id.img_encuesta);

        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            public void run() {

                Intent intent = new Intent(EncuestaLoadScreenActivity.this, MenuEncuestaActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Pair[] pairsRelevamiento = new Pair[1];
                pairsRelevamiento[0] = new Pair<View, String>(imgButton, "imageTransitionEncuesta");
                ActivityOptions activityOptionsRelevamiento = ActivityOptions
                        .makeSceneTransitionAnimation(EncuestaLoadScreenActivity.this, pairsRelevamiento);
                startActivity(intent, activityOptionsRelevamiento.toBundle());
            }
        }, 500);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}

