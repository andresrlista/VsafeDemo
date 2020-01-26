package com.anlisoft.vsafe.views;

import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.anlisoft.vsafe.R;

import static com.anlisoft.vsafe.animations.BounceViewAnimation.startBounceAnimation;

public class ReporteLoadScreenActivity extends AppCompatActivity {

    private ImageButton imgReporteLoading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reporte_load_screen);

        imgReporteLoading = findViewById(R.id.img_reporte);
        startBounceAnimation(this,
                imgReporteLoading,
                0.15, 25);

        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            public void run() {
                finish();
            }
        }, 1500);
    }

    @Override
    public void onBackPressed() {
        // Do nothing
    }
}
