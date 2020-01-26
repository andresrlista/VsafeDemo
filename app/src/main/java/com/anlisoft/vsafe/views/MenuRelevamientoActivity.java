package com.anlisoft.vsafe.views;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.anlisoft.vsafe.R;
import com.anlisoft.vsafe.interfaces.ApiServiceVsafe;
import com.anlisoft.vsafe.interfaces.IServiceVsafe;
import com.anlisoft.vsafe.models.data.Global;
import com.anlisoft.vsafe.models.data.Relevamiento;
import com.anlisoft.vsafe.models.request.UserRelevamientoRequest;
import com.anlisoft.vsafe.models.response.UserRelevamientoResponse;
import com.anlisoft.vsafe.utils.CustomProgressDialog;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.anlisoft.vsafe.models.data.Global.gUserProfileData;
import static com.anlisoft.vsafe.models.data.Global.gUserSessionData;

public class MenuRelevamientoActivity
        extends AppCompatActivity
        implements View.OnClickListener {

    private String mApiServiceUrl;
    private IServiceVsafe vsafeServices;

    private RatingBar rb_releva_1,
            rb_releva_2,
            rb_releva_3,
            rb_releva_4;

    private CustomProgressDialog customProgressDialog;
    private Button btn_enviar_relevamiento;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        setContentView(R.layout.activity_menu_relevamiento);

        mApiServiceUrl = Global.BASE_URL_VISUALSAT_DEFAULT;
        vsafeServices = ApiServiceVsafe.getAPIService(mApiServiceUrl);

        init_UIComponents();
        customProgressDialog = new CustomProgressDialog(this);

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    public void init_UIComponents() {

        rb_releva_1 = findViewById(R.id.rb_releva_1);
        rb_releva_2 = findViewById(R.id.rb_releva_2);
        rb_releva_3 = findViewById(R.id.rb_releva_3);
        rb_releva_4 = findViewById(R.id.rb_releva_4);

        btn_enviar_relevamiento = findViewById(R.id.btn_enviar_relevamiento);
        btn_enviar_relevamiento.setOnClickListener(this);


    }

    public void enableUI(boolean isEnable) {

        rb_releva_1.setEnabled(isEnable);
        rb_releva_2.setEnabled(isEnable);
        rb_releva_3.setEnabled(isEnable);
        rb_releva_4.setEnabled(isEnable);
        btn_enviar_relevamiento.setEnabled(isEnable);
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.btn_enviar_relevamiento) {

            enableUI(false);
            if (rb_releva_1.getRating() < 0 ||
                    rb_releva_2.getRating() < 0 ||
                    rb_releva_3.getRating() < 0 ||
                    rb_releva_4.getRating() < 0) {

                Snackbar.make(getWindow().getDecorView().getRootView(),
                        "Por favor, verifique los datos solicitados.",
                        2000)
                        .show();

                enableUI(true);

            } else {

                enableUI(false);
                customProgressDialog.showDialogWithMsg("Enviando relevamiento");
                int rb1 = (int) (rb_releva_1.getRating());
                int rb2 = (int) (rb_releva_2.getRating());
                int rb3 = (int) (rb_releva_3.getRating());
                int rb4 = (int) (rb_releva_4.getRating());
                Relevamiento r1 = new Relevamiento("1", String.valueOf(rb1));
                Relevamiento r2 = new Relevamiento("2", String.valueOf(rb2));
                Relevamiento r3 = new Relevamiento("3", String.valueOf(rb3));
                Relevamiento r4 = new Relevamiento("4", String.valueOf(rb4));

                List<Relevamiento> userRelevamientoList = new ArrayList<>();
                userRelevamientoList.add(r1);
                userRelevamientoList.add(r2);
                userRelevamientoList.add(r3);
                userRelevamientoList.add(r4);

                UserRelevamientoRequest userRelevamientoRequest = new UserRelevamientoRequest(
                        String.valueOf(gUserSessionData.getId()),
                        String.valueOf(gUserProfileData.getUserCurrtentLocation().getLatitude()),
                        String.valueOf(gUserProfileData.getUserCurrtentLocation().getLongitude()),
                        userRelevamientoList
                );

                Map<String, String> mapHeaders = new HashMap<>();
                mapHeaders.put("Content-type", "application/json");
                mapHeaders.put("Authorization", "Bearer " + gUserSessionData.getToken());

                Gson gson = new Gson();
                String json = gson.toJson(userRelevamientoRequest);
                Log.e("vsafe-demo","Gson-Data: " + json);
                sendRelevamiento(mapHeaders, userRelevamientoRequest);

            }
        }
    }

    public void sendRelevamiento(Map<String, String> requestHeaders,
                                 UserRelevamientoRequest userRelevamientoRequest) {
        vsafeServices.sendUserRelevamiento(requestHeaders, userRelevamientoRequest)
                .enqueue(new Callback<UserRelevamientoResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<UserRelevamientoResponse> call,
                                           @NonNull Response<UserRelevamientoResponse> response) {

                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                customProgressDialog.dismissProgressDialog();
                                AlertDialog.Builder buildDiag = new AlertDialog.Builder(MenuRelevamientoActivity.this);
                                buildDiag.setTitle("Información:");
                                buildDiag.setCancelable(false);
                                buildDiag.setMessage("El relevamiento ha sido enviado exitosamente.");
                                buildDiag.setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(MenuRelevamientoActivity.this, MainActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                                AlertDialog alertDialog = buildDiag.create();
                                alertDialog.show();
                            }
                        } else {
                            customProgressDialog.dismissProgressDialog();
                            AlertDialog.Builder buildDiag = new AlertDialog.Builder(MenuRelevamientoActivity.this);
                            buildDiag.setTitle("Información:");
                            buildDiag.setCancelable(false);
                            buildDiag.setMessage("Por favor, verifique su conexión a internet.");
                            buildDiag.setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(MenuRelevamientoActivity.this, MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                            AlertDialog alertDialog = buildDiag.create();
                            alertDialog.show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<UserRelevamientoResponse> call,
                                          @NonNull Throwable t) {

                        Snackbar.make(getWindow().getDecorView().getRootView(),
                                "Error de conexión al servidor.",
                                2000)
                                .show();
                        enableUI(true);
                    }
                });


    }
}
