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

public class RelevamientoLoadScreenActivity extends AppCompatActivity {

    private ImageButton imgRelevamientoLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relevamiento_load_screen);

        imgRelevamientoLoading = findViewById(R.id.img_relevamiento);
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            public void run() {

                Intent intent = new Intent(RelevamientoLoadScreenActivity.this, MenuRelevamientoActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Pair[] pairs = new Pair[1];
                pairs[0] = new Pair<View, String>(imgRelevamientoLoading, "imageTransitionRelevamiento");
                ActivityOptions activityOptionsRelevamiento = ActivityOptions
                        .makeSceneTransitionAnimation(RelevamientoLoadScreenActivity.this, pairs);
                startActivity(intent, activityOptionsRelevamiento.toBundle());
            }
        }, 500);
    }

    @Override
    public void onBackPressed() {
        // Do nothing
    }
}
