package com.anlisoft.vsafe.views;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.anlisoft.vsafe.R;
import com.anlisoft.vsafe.interfaces.ApiServiceVsafe;
import com.anlisoft.vsafe.interfaces.IServiceVsafe;
import com.anlisoft.vsafe.models.data.Contribuyente;
import com.anlisoft.vsafe.models.data.Global;
import com.anlisoft.vsafe.models.data.Relevamiento;
import com.anlisoft.vsafe.models.request.UserEncuestaRequest;
import com.anlisoft.vsafe.models.response.UserEncuestaResponse;
import com.anlisoft.vsafe.utils.CustomProgressDialog;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.anlisoft.vsafe.models.data.Global.gUserProfileData;
import static com.anlisoft.vsafe.models.data.Global.gUserSessionData;

public class MenuEncuestaActivity
        extends AppCompatActivity
        implements View.OnClickListener {

    private String mApiServiceUrl;
    private IServiceVsafe vsafeServices;

    private EditText edit_text_name,
            edit_text_dni,
            edit_text_phone;

    private RatingBar rating_bar1,
            rating_bar2,
            rating_bar3;

    private Button btn_enviar_encuesta;
    private CustomProgressDialog customProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        setContentView(R.layout.activity_menu_encuesta);
        mApiServiceUrl = Global.BASE_URL_VISUALSAT_DEFAULT;
        vsafeServices = ApiServiceVsafe.getAPIService(mApiServiceUrl);
        customProgressDialog = new CustomProgressDialog(this);
        init_UIComponents();


    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.btn_enviar_encuesta) {

            enableUI(false);

            if (edit_text_name.length() < 3 ||
                    edit_text_dni.length() < 6 ||
                    rating_bar1.getRating() < 0 ||
                    rating_bar2.getRating() < 0 ||
                    rating_bar3.getRating() < 0) {

                enableUI(true);
                customProgressDialog.dismissProgressDialog();
                Snackbar.make(getWindow().getDecorView().getRootView(),
                        "Por favor, complete los campos solicitados.",
                        2000)
                        .show();

            } else {

                enableUI(false);
                customProgressDialog.showDialogWithMsg("Enviando encuesta...");

                Contribuyente mContribuyente = new Contribuyente(
                        edit_text_name.getText().toString().trim(),
                        edit_text_dni.getText().toString().trim(),
                        edit_text_phone.getText().toString().trim(),
                        "empty");

                Relevamiento mRelevamiento1 = new Relevamiento(
                        "5",
                        String.valueOf(rating_bar1.getRating()));

                Relevamiento mRelevamiento2 = new Relevamiento(
                        "6",
                        String.valueOf(rating_bar2.getRating()));

                Relevamiento mRelevamiento3 = new Relevamiento(
                        "7",
                        String.valueOf(rating_bar3.getRating()));

                Map<String, String> requestHeaders = new HashMap<>();
                requestHeaders.put("Content-type", "application/json");
                requestHeaders.put("Authorization", "Bearer " + gUserSessionData.getToken());

                List<Relevamiento> relevamientoList = new ArrayList<>();
                relevamientoList.add(mRelevamiento1);
                relevamientoList.add(mRelevamiento2);
                relevamientoList.add(mRelevamiento3);

                UserEncuestaRequest userEncuesta = new UserEncuestaRequest(
                        gUserProfileData.getUserId(),
                        String.valueOf(gUserProfileData.getUserCurrtentLocation().getLatitude()),
                        String.valueOf(gUserProfileData.getUserCurrtentLocation().getLongitude()),
                        mContribuyente,
                        relevamientoList
                );

                sendEncuesta(requestHeaders, userEncuesta);

            }
        }
    }

    private void init_UIComponents() {

        edit_text_name = findViewById(R.id.text_contribuyente_name);
        edit_text_dni = findViewById(R.id.text_dni);
        edit_text_phone = findViewById(R.id.text_phone);

        rating_bar1 = findViewById(R.id.rating_bar1);
        rating_bar2 = findViewById(R.id.rating_bar2);
        rating_bar3 = findViewById(R.id.rating_bar3);

        btn_enviar_encuesta = findViewById(R.id.btn_enviar_encuesta);
        btn_enviar_encuesta.setOnClickListener(this);

    }

    private void enableUI(boolean isEnabled) {

        edit_text_name.setEnabled(isEnabled);
        edit_text_dni.setEnabled(isEnabled);
        edit_text_phone.setEnabled(isEnabled);
        rating_bar1.setEnabled(isEnabled);
        rating_bar2.setEnabled(isEnabled);
        rating_bar3.setEnabled(isEnabled);

    }

    public void sendEncuesta(Map<String, String> header,
                             UserEncuestaRequest userEncuestaRequest) {

        vsafeServices.sendUserEncuesta(header, userEncuestaRequest).enqueue(new Callback<UserEncuestaResponse>() {
            @Override
            public void onResponse(@NonNull Call<UserEncuestaResponse> call, @NonNull Response<UserEncuestaResponse> response) {

                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        customProgressDialog.dismissProgressDialog();
                        AlertDialog.Builder buildDiag = new AlertDialog.Builder(MenuEncuestaActivity.this);
                        buildDiag.setTitle("Información:");
                        buildDiag.setCancelable(false);
                        buildDiag.setMessage("La encuesta ha sido registrada exitosamente");
                        buildDiag.setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(MenuEncuestaActivity.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                startActivity(intent);
                                finish();
                            }
                        });
                        AlertDialog alertDialog = buildDiag.create();
                        alertDialog.show();
                        Log.d("vsafe-demo", "onResponse: " + response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserEncuestaResponse> call, @NonNull Throwable t) {
                customProgressDialog.dismissProgressDialog();
                Snackbar.make(getWindow().getDecorView().getRootView(),
                        "Ocurrio un error al intentar conectar con el servidor.",
                        2000)
                        .show();
            }
        });


    }
}
