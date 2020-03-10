package com.anlisoft.vsafe.views;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.anlisoft.vsafe.R;
import com.anlisoft.vsafe.interfaces.ApiServiceVsafe;
import com.anlisoft.vsafe.interfaces.IServiceVsafe;
import com.anlisoft.vsafe.models.data.Contribuyente;
import com.anlisoft.vsafe.models.data.DenunciaCategoria;
import com.anlisoft.vsafe.models.data.Global;
import com.anlisoft.vsafe.models.data.Motivo;
import com.anlisoft.vsafe.models.request.UserDenunciaRequest;
import com.anlisoft.vsafe.models.response.DenunciaCategoriaResponse;
import com.anlisoft.vsafe.models.response.DenunciaMotivoResponse;
import com.anlisoft.vsafe.models.response.UserDenunciaResponse;
import com.anlisoft.vsafe.utils.CustomProgressDialog;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.anlisoft.vsafe.models.data.Global.gUserProfileData;
import static com.anlisoft.vsafe.models.data.Global.gUserSessionData;

public class MenuDenunciaActivity
        extends AppCompatActivity
        implements View.OnClickListener {

    private Button btn_enviar_denuncia;
    private EditText text_denuncia_username,
            text_denuncia_dni,
            text_denuncia_phone,
            text_denuncia_descripcion;
    private ArrayAdapter categoriaDenuncia;
    private DenunciaCategoria categoriaSelected;
    private Motivo motivoSelected;
    private Spinner spinner_categoria,
            spinner_motivo;

    private IServiceVsafe vsafeServices;
    private CustomProgressDialog customProgressDialogLoadCategorias;
    private Map<String, String> requestHeaders = new ArrayMap<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_denuncia);
        String mApiServiceUrl = Global.BASE_URL_VISUALSAT_DEFAULT;
        vsafeServices = ApiServiceVsafe.getAPIService(mApiServiceUrl);

        init_UIComponents();
        enableUI(false);
        customProgressDialogLoadCategorias = new CustomProgressDialog(this);
        requestHeaders.put("Authorization", "Bearer " + gUserSessionData.getToken());
        requestHeaders.put("Content-type", "application/json");
        getDenunciaCategorias(requestHeaders);
        //getDenunciaMotivosById(requestHeaders, "2");

        spinner_categoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                categoriaSelected = (DenunciaCategoria) categoriaDenuncia.getItem(i);
                if (categoriaSelected != null) {
                    Log.d("vsafe-demo", "onItemSelected: " + categoriaSelected.getId());

                    if (categoriaSelected.getId() != null) {
                        getDenunciaMotivosById(requestHeaders, categoriaSelected.getId());
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner_motivo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (Global.gMotivoDenunciaById.getMotivoResult() != null) {
                    motivoSelected = Global.gMotivoDenunciaById.getMotivoResult().get(i);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    public void init_UIComponents() {
        text_denuncia_username = findViewById(R.id.text_denuncia_name);
        text_denuncia_dni = findViewById(R.id.text_denuncia_dni);
        text_denuncia_phone = findViewById(R.id.text_denuncia_phone);
        text_denuncia_descripcion = findViewById(R.id.text_denuncia_descripcion);
        spinner_categoria = findViewById(R.id.spinner_categoria);
        spinner_motivo = findViewById(R.id.spinner_motivo);
        btn_enviar_denuncia = findViewById(R.id.btn_enviar_denuncia);
    }

    public void enableUI(boolean isEnabled) {

        text_denuncia_username.setEnabled(isEnabled);
        text_denuncia_dni.setEnabled(isEnabled);
        text_denuncia_phone.setEnabled(isEnabled);
        spinner_categoria.setEnabled(isEnabled);
        spinner_motivo.setEnabled(isEnabled);
        btn_enviar_denuncia.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    public void sendUserDenuncia(Map<String, String> headerRequest, UserDenunciaRequest userDenunciaRequest) {
        customProgressDialogLoadCategorias.showDialogWithMsg("Enviando denuncia...");
        vsafeServices.sendUserDenunciaRequest(headerRequest, userDenunciaRequest).enqueue(new Callback<UserDenunciaResponse>() {
            @Override
            public void onResponse(@NonNull Call<UserDenunciaResponse> call,
                                   @NonNull Response<UserDenunciaResponse> response) {
                if (response.isSuccessful()) {

                    if (response.body() != null) {
                        //TODO
                        enableUI(true);
                        AlertDialog.Builder buildDiag = new AlertDialog.Builder(MenuDenunciaActivity.this);
                        buildDiag.setTitle("Información:");
                        buildDiag.setCancelable(false);
                        buildDiag.setMessage("La denuncia ha sido registrada exitosamente.");
                        buildDiag.setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(MenuDenunciaActivity.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                startActivity(intent);
                                finish();
                            }
                        });
                        AlertDialog alertDialog = buildDiag.create();
                        alertDialog.show();
                        customProgressDialogLoadCategorias.dismissProgressDialog();
                        Log.d("vsafe-demo", "onResponse: " + response.body().getMessage());
                    } else {
                        enableUI(true);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserDenunciaResponse> call,
                                  @NonNull Throwable t) {
                enableUI(true);
                customProgressDialogLoadCategorias.dismissProgressDialog();
                Log.d("vsafe-demo", "onFailure: " + t.getMessage());

            }
        });
    }

    public void getDenunciaCategorias(Map<String, String> requestHeaders) {

        customProgressDialogLoadCategorias.showDialogWithMsg("Cargando datos...");
        vsafeServices.getDenunciaCategoria(requestHeaders).enqueue(new Callback<DenunciaCategoriaResponse>() {
            @Override
            public void onResponse(@NonNull Call<DenunciaCategoriaResponse> call,
                                   @NonNull Response<DenunciaCategoriaResponse> response) {
                if (response.isSuccessful()) {

                    if (response.body() != null) {
                        enableUI(true);
                        customProgressDialogLoadCategorias.dismissProgressDialog();
                        Global.gCategoriasDenuncia = response.body();
                        updateCategorias(Global.gCategoriasDenuncia);
                    }
                } else {
                    customProgressDialogLoadCategorias.dismissProgressDialog();
                    Snackbar.make(getWindow().getDecorView().getRootView(),
                            "Error al conectar con el servidor.",
                            2000)
                            .show();
                    enableUI(true);
                }
            }

            @Override
            public void onFailure(@NonNull Call<DenunciaCategoriaResponse> call,
                                  @NonNull Throwable t) {
                customProgressDialogLoadCategorias.dismissProgressDialog();
                Snackbar.make(getWindow().getDecorView().getRootView(),
                        "Error al conectar con el servidor.",
                        2000)
                        .show();
                enableUI(true);
            }
        });

    }

    public void getDenunciaMotivosById(Map<String, String> requestHeaders, String motivoId) {

        customProgressDialogLoadCategorias.showDialogWithMsg("Cargando datos...");
        enableUI(false);
        vsafeServices.getDenunciaMotivo(requestHeaders, motivoId).enqueue(new Callback<DenunciaMotivoResponse>() {
            @Override
            public void onResponse(@NonNull Call<DenunciaMotivoResponse> call,
                                   @NonNull Response<DenunciaMotivoResponse> response) {
                if (response.isSuccessful()) {

                    if (response.body() != null) {
                        customProgressDialogLoadCategorias.dismissProgressDialog();
                        enableUI(true);
                        Global.gMotivoDenunciaById = response.body();
                        updateMotivo(Global.gMotivoDenunciaById);
                    }
                } else {
                    customProgressDialogLoadCategorias.dismissProgressDialog();
                    Snackbar.make(getWindow().getDecorView().getRootView(),
                            "Error al conectar con el servidor.",
                            2000)
                            .show();
                    enableUI(true);
                }
            }

            @Override
            public void onFailure(@NonNull Call<DenunciaMotivoResponse> call,
                                  @NonNull Throwable t) {
                customProgressDialogLoadCategorias.dismissProgressDialog();
                Snackbar.make(getWindow().getDecorView().getRootView(),
                        "Error al conectar con el servidor.",
                        2000)
                        .show();
                enableUI(true);
                Log.d("vsafe-demo", "onFailure: " + t.getMessage());
            }
        });

    }

    private void updateMotivo(DenunciaMotivoResponse gMotivoDenunciaById) {

        ArrayAdapter motivoDenuncia = new ArrayAdapter<>(MenuDenunciaActivity.this,
                R.layout.spinner_categoria_text,
                gMotivoDenunciaById.getMotivoResult());
        motivoDenuncia.setDropDownViewResource(R.layout.spinner_categoria_dropdown);
        spinner_motivo.setAdapter(motivoDenuncia);

    }


    public void updateCategorias(DenunciaCategoriaResponse denunciaCategoriaResponse) {

        categoriaDenuncia = new ArrayAdapter<>(MenuDenunciaActivity.this,
                R.layout.spinner_categoria_text,
                denunciaCategoriaResponse.getResult());
        categoriaDenuncia.setDropDownViewResource(R.layout.spinner_categoria_dropdown);
        spinner_categoria.setAdapter(categoriaDenuncia);

    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.btn_enviar_denuncia) {

            if (text_denuncia_username.length() < 2 ||
                    text_denuncia_dni.length() < 6 ||
                    text_denuncia_phone.length() < 7) {

                //TODO
                Snackbar.make(getWindow().getDecorView().getRootView(),
                        "Pot favor, verifique los datos solicitados.",
                        2000)
                        .show();

            } else {

                String userCurrentAddress = getUserCompleteAddress(
                        gUserProfileData.getUserCurrtentLocation().getLatitude(),
                        gUserProfileData.getUserCurrtentLocation().getLongitude());
                enableUI(false);
                Contribuyente denunciaContribuyente = new Contribuyente(
                        text_denuncia_username.getText().toString().trim(),
                        text_denuncia_dni.getText().toString().trim(),
                        text_denuncia_phone.getText().toString().trim(),
                        "N/A"
                );

                UserDenunciaRequest userDenunciaRequest = new UserDenunciaRequest(
                        String.valueOf(gUserSessionData.getId()),
                        String.valueOf(gUserProfileData.getUserCurrtentLocation().getLatitude()),
                        String.valueOf(gUserProfileData.getUserCurrtentLocation().getLongitude()),
                        denunciaContribuyente,
                        text_denuncia_descripcion.getText().toString().trim(),
                        String.valueOf(motivoSelected.getId()),
                        userCurrentAddress
                );

                sendUserDenuncia(requestHeaders, userDenunciaRequest);
            }
        }

    }

    private String getUserCompleteAddress(double lat, double lng) {

        String szUserAddress = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder szReturnedAddress = new StringBuilder();

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    szReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                szUserAddress = szReturnedAddress.toString();

                if (szUserAddress.contains("á")) {
                    szUserAddress = szUserAddress.replace("á", "a");
                }

                if (szUserAddress.contains("é")) {
                    szUserAddress = szUserAddress.replace("é", "e");
                }
                if (szUserAddress.contains("í")) {
                    szUserAddress = szUserAddress.replace("í", "i");
                }

                if (szUserAddress.contains("ó")) {
                    szUserAddress = szUserAddress.replace("ó", "o");
                }

                if (szUserAddress.contains("ú")) {
                    szUserAddress = szUserAddress.replace("ú", "u");
                }
                Log.w("vsafe-demo", "my current loc. address" + szReturnedAddress.toString());
            } else {
                Log.w("vsafe", "my current loc. address no avaible");
            }

        } catch (IOException e) {
            szUserAddress = "No disponible";
            e.printStackTrace();
        }
        return szUserAddress;
    }

}
