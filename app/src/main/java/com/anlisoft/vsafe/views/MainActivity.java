package com.anlisoft.vsafe.views;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;

import com.anlisoft.vsafe.R;
import com.anlisoft.vsafe.models.data.ConnectionModel;
import com.anlisoft.vsafe.models.data.Global;
import com.anlisoft.vsafe.models.response.UserLoginResponse;
import com.anlisoft.vsafe.utils.CircleTransform;
import com.anlisoft.vsafe.utils.ConnectionLiveData;
import com.anlisoft.vsafe.utils.UserLocation;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.squareup.picasso.Picasso;

import static com.anlisoft.vsafe.animations.BounceViewAnimation.startBounceAnimation;
import static com.anlisoft.vsafe.models.data.Global.KEY_CURRENT_MUNICIPALIDAD;
import static com.anlisoft.vsafe.models.data.Global.KEY_MUNICIPALIDAD_NAME;
import static com.anlisoft.vsafe.models.data.Global.KEY_USERLOGGED;
import static com.anlisoft.vsafe.models.data.Global.KEY_VSAFE;
import static com.anlisoft.vsafe.models.data.Global.gSessionManager;
import static com.anlisoft.vsafe.models.data.Global.gSessionManagerEditor;
import static com.anlisoft.vsafe.models.data.Global.gUserSessionData;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener {

    private ImageView img_userprofile;
    private TextView text_username_profile,
            text_location;
    private CardView cardViewEncuesta,
            cardViewRelevamiento,
            cardViewDenuncia,
            cardViewReporte;
    private TextView txtEncuesta,
            txtRelevamiento,
            txtDenuncia,
            txtReporte;
    private ImageButton btnEncuesta,
            btnRelevamiento,
            btnDenuncia,
            btnReporte;
    private AlertDialog alertDialog;

    private FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        setContentView(R.layout.activity_main);
        gSessionManager = getSharedPreferences(KEY_VSAFE, Context.MODE_PRIVATE);
        ConnectionLiveData connectionLiveData = new ConnectionLiveData(getApplicationContext());
        connectionLiveData.observe(this, new Observer<ConnectionModel>() {
            @Override
            public void onChanged(ConnectionModel connection) {

                if (connection != null) {

                    if (!connection.getIsConnected()) {

                            AlertDialog.Builder buildDiag = new AlertDialog.Builder(MainActivity.this);
                            buildDiag.setTitle(getString(R.string.diag_title_info));
                            buildDiag.setCancelable(false);
                            buildDiag.setMessage(getString(R.string.diag_msgbody_internet_not_available));
                            buildDiag.setPositiveButton(getString(R.string.diag_btn_close), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    finish();
                                }
                            });
                            alertDialog = buildDiag.create();
                            alertDialog.show();

                    } else {
                        if (alertDialog != null) {
                            alertDialog.dismiss();
                        }
                    }
                }
            }
        });

        // Initialize UI:
        initializeUIComponents();
        UserLocation.updateUserCurrentLocation(this, fusedLocationProviderClient);
        updateUI(gUserSessionData);

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder buildDiag = new AlertDialog.Builder(MainActivity.this);
        buildDiag.setTitle(getString(R.string.diag_title_info));
        buildDiag.setCancelable(false);
        buildDiag.setMessage(getString(R.string.diag_msgbody_quit_session));
        buildDiag.setPositiveButton(getString(R.string.diag_btn_yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Global.gRetrofit = null;
                gSessionManagerEditor = gSessionManager.edit();
                gSessionManagerEditor.putBoolean(KEY_USERLOGGED, false);
                gSessionManagerEditor.putString(KEY_CURRENT_MUNICIPALIDAD, "-");
                gSessionManagerEditor.apply();
                dialog.dismiss();
                finish();
            }
        }).setNegativeButton(getString(R.string.diag_btn_no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        AlertDialog alertDialog = buildDiag.create();
        alertDialog.show();
    }

    public void updateUI(UserLoginResponse userData) {

        loadImageProfile("https://www.google.com", img_userprofile);
        text_username_profile.setText(userData.getFirstName().
                concat(" ")
                .concat(userData.getLastName()));
        StringBuilder sb = new StringBuilder();
        sb.append(getString(R.string.user_municipalidad)).append(gSessionManager.getString(KEY_MUNICIPALIDAD_NAME, "..."));
        text_location.setText(sb);
    }

    public void loadImageProfile(String url, ImageView imageViewProfile) {
        Picasso.get()
                .load(url)
                .transform(new CircleTransform())
                .placeholder(R.drawable.img_profile_default)
                .error(R.drawable.img_profile_default)
                .into(img_userprofile);
    }

    @Override
    public void onClick(View view) {

        Intent intent;
        switch (view.getId()) {
            case R.id.btn_encuesta:
                intent = new Intent(MainActivity.this, EncuestaLoadScreenActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Pair[] pairsEncuesta = new Pair[3];
                pairsEncuesta[0] = new Pair<View, String>(cardViewEncuesta, "cardTransitionEncuesta");
                pairsEncuesta[1] = new Pair<View, String>(btnEncuesta, "imageTransitionEncuesta");
                pairsEncuesta[2] = new Pair<View, String>(txtEncuesta, "textTransitionEncuesta");
                ActivityOptions activityOptionsEncuesta = ActivityOptions
                        .makeSceneTransitionAnimation(this, pairsEncuesta);
                startActivity(intent, activityOptionsEncuesta.toBundle());
                break;

            case R.id.btn_relevamiento:
                intent = new Intent(MainActivity.this, RelevamientoLoadScreenActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Pair[] pairsRelevamiento = new Pair[3];
                pairsRelevamiento[0] = new Pair<View, String>(cardViewEncuesta, "cardTransitionRelevamiento");
                pairsRelevamiento[1] = new Pair<View, String>(btnRelevamiento, "imageTransitionRelevamiento");
                pairsRelevamiento[2] = new Pair<View, String>(txtEncuesta, "textTransitionRelevamiento");
                ActivityOptions activityOptionsRelevamiento = ActivityOptions
                        .makeSceneTransitionAnimation(this, pairsRelevamiento);
                startActivity(intent, activityOptionsRelevamiento.toBundle());
                break;

            case R.id.btn_denuncia:
                intent = new Intent(MainActivity.this, DenunciaLoadScreenActivity.class);
                Pair[] pairsDenuncia = new Pair[3];
                pairsDenuncia[0] = new Pair<View, String>(cardViewEncuesta, "cardTransitionDenuncia");
                pairsDenuncia[1] = new Pair<View, String>(btnDenuncia, "imageTransitionDenuncia");
                pairsDenuncia[2] = new Pair<View, String>(txtEncuesta, "textTransitionDenuncia");

                ActivityOptions activityOptionsDenuncia = ActivityOptions
                        .makeSceneTransitionAnimation(this, pairsDenuncia);
                startActivity(intent, activityOptionsDenuncia.toBundle());
                break;

            case R.id.btn_reporte:
                intent = new Intent(MainActivity.this, ReporteLoadScreenActivity.class);
                Pair[] pairsReporte = new Pair[3];
                pairsReporte[0] = new Pair<View, String>(cardViewEncuesta, "cardTransitionReporte");
                pairsReporte[1] = new Pair<View, String>(btnReporte, "imageTransitionReporte");
                pairsReporte[2] = new Pair<View, String>(txtEncuesta, "textTransitionReporte");

                ActivityOptions activityOptionsReporte = ActivityOptions
                        .makeSceneTransitionAnimation(this, pairsReporte);
                startActivity(intent, activityOptionsReporte.toBundle());
                break;
        }
    }

    public void initializeUIComponents() {

        img_userprofile = findViewById(R.id.img_userprofile);
        text_username_profile = findViewById(R.id.text_username);
        text_location = findViewById(R.id.text_location);
        cardViewEncuesta = findViewById(R.id.cardEncuesta);
        cardViewRelevamiento = findViewById(R.id.cardRelevamiento);
        cardViewDenuncia = findViewById(R.id.cardDenuncia);
        cardViewReporte = findViewById(R.id.cardReporte);
        txtEncuesta = findViewById(R.id.txt_encuesta);
        txtRelevamiento = findViewById(R.id.txt_relevamiento);
        txtDenuncia = findViewById(R.id.txt_denuncia);
        txtReporte = findViewById(R.id.txt_reporte);
        btnEncuesta = findViewById(R.id.btn_encuesta);
        btnRelevamiento = findViewById(R.id.btn_relevamiento);
        btnDenuncia = findViewById(R.id.btn_denuncia);
        btnReporte = findViewById(R.id.btn_reporte);

        btnEncuesta.setOnClickListener(this);
        btnRelevamiento.setOnClickListener(this);
        btnDenuncia.setOnClickListener(this);
        btnReporte.setOnClickListener(this);

        startBounceAnimation(this, btnEncuesta, 0.2, 20);
        startBounceAnimation(this, btnRelevamiento, 0.2, 20);
        startBounceAnimation(this, btnDenuncia, 0.2, 20);
        startBounceAnimation(this, btnReporte, 0.2, 20);

    }
}
