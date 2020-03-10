package com.anlisoft.vsafe.views;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.anlisoft.vsafe.R;
import com.anlisoft.vsafe.interfaces.IServiceVsafe;
import com.anlisoft.vsafe.models.data.Global;
import com.anlisoft.vsafe.models.data.Municipalidad;
import com.anlisoft.vsafe.models.request.UserLoginRequest;
import com.anlisoft.vsafe.models.response.UserLoginResponse;
import com.anlisoft.vsafe.repositories.SplashScreenRepository;
import com.anlisoft.vsafe.utils.CustomProgressDialog;
import com.anlisoft.vsafe.utils.UserDataValidator;
import com.google.android.material.snackbar.Snackbar;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.functions.Consumer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.anlisoft.vsafe.models.data.Global.BASE_URL_CURRENT;
import static com.anlisoft.vsafe.models.data.Global.KEY_CURRENT_MUNICIPALIDAD;
import static com.anlisoft.vsafe.models.data.Global.KEY_MUNICIPALIDAD_NAME;
import static com.anlisoft.vsafe.models.data.Global.KEY_USERLOGGED;
import static com.anlisoft.vsafe.models.data.Global.KEY_USER_LOGIN;
import static com.anlisoft.vsafe.models.data.Global.KEY_USER_PASSWORD;
import static com.anlisoft.vsafe.models.data.Global.KEY_VSAFE;
import static com.anlisoft.vsafe.models.data.Global.gLocationManager;
import static com.anlisoft.vsafe.models.data.Global.gMunicipalidadesListado;
import static com.anlisoft.vsafe.models.data.Global.gSessionManager;
import static com.anlisoft.vsafe.models.data.Global.gSessionManagerEditor;
import static com.anlisoft.vsafe.models.data.Global.gUserProfileData;
import static com.anlisoft.vsafe.models.data.Global.gUserSessionData;
import static com.anlisoft.vsafe.models.data.Global.gVsafeWebServices;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText userLogin,
            userPassword;
    private Spinner spinner_country_list;
    private Button btnLogin;
    private SplashScreenRepository mRepository = null;
    private CustomProgressDialog customProgressDialog;
    private ArrayAdapter<Municipalidad> countryListAdapter;
    private UserLoginRequest userLoginRequest = new UserLoginRequest();
    private String serverLocation;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppThemeMain);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mRepository = SplashScreenRepository.getInstance();

        gLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        gSessionManager = getSharedPreferences(KEY_VSAFE, Context.MODE_PRIVATE);

        initializeContentViewComponents(this);

        spinner_country_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                serverLocation = gMunicipalidadesListado.get(position).getDescription();

                Log.d("vsafe-demo", "onItemSelected: " + serverLocation);
                if (!gMunicipalidadesListado.get(position).getDescription().equals("Seleccione Municipalidad:")) {
                    Global.BASE_URL_CURRENT = gMunicipalidadesListado.get(position).getBaseurl() + ":" +
                            gMunicipalidadesListado.get(position).getPort().toString() +
                            "/" + gMunicipalidadesListado.get(position).getApi() + "/";
                    mRepository.setWebServiceApiSource(BASE_URL_CURRENT, IServiceVsafe.class);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions
                .request(Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION) // ask single or multiple permission once
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean granted) throws Exception {
                        if (granted) {
                            // All requested permissions are granted
                            if (!gLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                                // Call your Alert message
                                isEnabledUILogin(false);
                                AlertDialog.Builder buildDiag = new AlertDialog.Builder(LoginActivity.this);
                                buildDiag.setTitle("Información:");
                                buildDiag.setCancelable(false);
                                buildDiag.setMessage("La ubicación de su dispositivo(GPS) está desactivada.");
                                buildDiag.setPositiveButton("Ir a la configuración", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                        dialog.dismiss();
                                        finish();
                                    }
                                }).setNegativeButton("salir", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        finish();

                                    }
                                });

                                AlertDialog alertDialog = buildDiag.create();
                                alertDialog.show();
                            }

                        } else {
                            finish();
                        }
                    }
                });
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.btn_login) {

            isEnabledUILogin(false);
            hideKeyboard(LoginActivity.this);
            userLoginRequest.setLogin(userLogin.getText().toString());
            userLoginRequest.setPassword(userPassword.getText().toString());
            customProgressDialog.showDialogWithMsg(
                    "Conectando con el servidor ..."
            );

            UserDataValidator userDataValidator = new UserDataValidator(userLoginRequest);

            switch (userDataValidator.isValidUser()) {

                case Global.USERLOGIN_NOT_VALID:
                    customProgressDialog.dismissProgressDialog();
                    isEnabledUILogin(true);
                    Snackbar.make(getWindow().getDecorView().getRootView(),
                            "Error al iniciar sesión: usuario no válido.",
                            Snackbar.LENGTH_LONG).setAction("¿Registrarse?", null)
                            .setActionTextColor(ContextCompat.getColor(
                                    this, R.color.colorAccent))
                            .show();
                    userLogin.setError("Ingrese un usuario válido");
                    break;

                case Global.PASSWORD_NOT_VALID:
                    isEnabledUILogin(true);
                    customProgressDialog.dismissProgressDialog();
                    Snackbar.make(getWindow().getDecorView().getRootView(),
                            "Error al iniciar sesión: contraseña no válida.",
                            Snackbar.LENGTH_LONG).setAction("¿Registrarse?",
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            })
                            .setActionTextColor(ContextCompat.getColor(
                                    this, R.color.colorAccent))
                            .show();
                    userPassword.setError("Ingrese una contraseña válida.");
                    break;

                case Global.USERLOGIN_PASSWORD_NOT_VALID:
                    isEnabledUILogin(true);
                    customProgressDialog.dismissProgressDialog();
                    Snackbar.make(getWindow().getDecorView().getRootView(),
                            "Error al iniciar sesión: el usuario y contraseña no son válidos.",
                            Snackbar.LENGTH_LONG).setAction("¿Registrarse?", null)
                            .setActionTextColor(ContextCompat.getColor(
                                    this, R.color.colorAccent))
                            .show();
                    userLogin.setError("Ingrese un usuario válido.");
                    userPassword.setError("Ingrese una contraseña válida.");
                    break;

                case Global.USERLOGIN_PASSWORD_VALID:

                    if (!gLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        // Call your Alert message
                        isEnabledUILogin(false);
                        AlertDialog.Builder buildDiag = new AlertDialog.Builder(LoginActivity.this);
                        buildDiag.setTitle("Información:");
                        buildDiag.setCancelable(false);
                        buildDiag.setMessage("La ubicación de su dispositivo está desactivada.\n" +
                                "Por favor, active la ubicación para continuar ejecutando Vsafe. ");
                        buildDiag.setPositiveButton("Ir a configuración ahora", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                dialog.dismiss();
                                finish();
                            }
                        }).setNegativeButton("salir", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();

                            }
                        });

                        AlertDialog alertDialog = buildDiag.create();
                        alertDialog.show();

                    } else {
                        userLogin(userLoginRequest.getLogin(), userLoginRequest.getPassword());
                    }
                    break;

            }
        } else {
            Snackbar.make(getWindow().getDecorView().getRootView(),
                    "Por favor, seleccione una municipalidad.",
                    Snackbar.LENGTH_LONG)
                    .show();
        }
    }

    public void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity
                .getSystemService(Activity.INPUT_METHOD_SERVICE);
        // Find the currently focused view,
        // so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        // If no view currently has focus, create a new one,
        // just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void isEnabledUILogin(boolean isEnabled) {

        if (!isEnabled) {
            userLogin.setEnabled(false);
            userPassword.setEnabled(false);
            btnLogin.setEnabled(false);

        } else {
            userLogin.setEnabled(true);
            userPassword.setEnabled(true);
            btnLogin.setEnabled(true);
        }
    }

    public void initializeContentViewComponents(Context context) {
        userLogin = findViewById(R.id.text_userlogin);
        userPassword = findViewById(R.id.text_userpassword);
        btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener((View.OnClickListener) context);
        spinner_country_list = findViewById(R.id.spinner_country_list);
        customProgressDialog = new CustomProgressDialog(this);

        countryListAdapter = new ArrayAdapter<>(this,
                R.layout.spinner_categoria_text_center,
                gMunicipalidadesListado);
        countryListAdapter.setDropDownViewResource(R.layout.spinner_categoria_dropdown_center);
        spinner_country_list.setAdapter(countryListAdapter);
        spinner_country_list.setSelection(0);

    }

    public void userLogin(final String uLogin, final String uPwd) {

        gVsafeWebServices.initUserLogin(uLogin, uPwd).enqueue(new Callback<UserLoginResponse>() {
            @Override
            public void onResponse(@NonNull Call<UserLoginResponse> call,
                                   @NonNull Response<UserLoginResponse> response) {

                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        gUserSessionData.setId(response.body().getId());
                        gUserSessionData.setFirstName(response.body().getFirstName());
                        gUserSessionData.setLastName(response.body().getLastName());
                        gUserSessionData.setExpireIn(response.body().getExpireIn());
                        gUserSessionData.setToken(response.body().getToken());
                        gUserSessionData.setUserServer(serverLocation);

                        gUserProfileData.setUserId(gUserSessionData.getId().toString());
                        gUserProfileData.setUserFirstName(gUserSessionData.getFirstName());
                        gUserProfileData.setUserLastName(gUserSessionData.getLastName());
                    }

                    if (gSessionManager != null) {
                        gSessionManagerEditor = gSessionManager.edit();
                        gSessionManagerEditor.putBoolean(KEY_USERLOGGED, true);
                        gSessionManagerEditor.putString(KEY_USER_LOGIN, String.valueOf(uLogin));
                        gSessionManagerEditor.putString(KEY_USER_PASSWORD, String.valueOf(uPwd));
                        gSessionManagerEditor.putString(KEY_MUNICIPALIDAD_NAME, String.valueOf(gUserSessionData.getUserServer()));
                        gSessionManagerEditor.putString(KEY_CURRENT_MUNICIPALIDAD, BASE_URL_CURRENT);
                        gSessionManagerEditor.apply();
                    }

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    customProgressDialog.dismissProgressDialog();
                    finish();

                } else {
                    isEnabledUILogin(true);
                    customProgressDialog.dismissProgressDialog();
                    Log.d("vsafe-demo", "onFailure: " + response.message() + " - " + BASE_URL_CURRENT);
                    if (gSessionManager != null) {
                        gSessionManagerEditor = gSessionManager.edit();
                        gSessionManagerEditor.putBoolean(KEY_USERLOGGED, false).apply();
                    }
                    Snackbar.make(getWindow().getDecorView().getRootView(),
                            "Error al iniciar sesión: el usuario y/o contraseña no son válidos.",
                            Snackbar.LENGTH_LONG).setAction("¿Registrarse?", null)
                            .setActionTextColor(ContextCompat.getColor(
                                    LoginActivity.this, R.color.colorAccent))
                            .show();
                    userLogin.setError("Ingrese un usuario válido.");
                    userPassword.setError("Ingrese una contraseña válida.");
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserLoginResponse> call,
                                  @NonNull Throwable t) {

                isEnabledUILogin(true);
                customProgressDialog.dismissProgressDialog();
                Log.d("vsafe-demo", "onFailure: " + t.getMessage());
                if (gSessionManager != null) {
                    gSessionManagerEditor = gSessionManager.edit();
                    gSessionManagerEditor.putBoolean(KEY_USERLOGGED, false).apply();
                }
                Snackbar.make(getWindow().getDecorView().getRootView(),
                        "Error al iniciar sesión: compruebe su conexión a internet.",
                        Snackbar.LENGTH_LONG).setAction("¿Registrarse?", null)
                        .setActionTextColor(ContextCompat.getColor(
                                LoginActivity.this, R.color.colorAccent))
                        .show();
                userLogin.setError("Ingrese un usuario válido.");
                userPassword.setError("Ingrese una contraseña válida.");

            }
        });

    }
}
