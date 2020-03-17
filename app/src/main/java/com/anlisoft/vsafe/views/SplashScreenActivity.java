package com.anlisoft.vsafe.views;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.anlisoft.vsafe.R;
import com.anlisoft.vsafe.interfaces.IServiceVsafe;
import com.anlisoft.vsafe.models.data.ConnectionModel;
import com.anlisoft.vsafe.models.data.Global;
import com.anlisoft.vsafe.models.data.Municipalidad;
import com.anlisoft.vsafe.models.response.MunicipalidadResponse;
import com.anlisoft.vsafe.models.response.UserLoginResponse;
import com.anlisoft.vsafe.repositories.SplashScreenRepository;
import com.anlisoft.vsafe.utils.ConnectionLiveData;
import com.anlisoft.vsafe.viewmodels.SplashScreenViewModel;
import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.anlisoft.vsafe.models.data.Global.KEY_USER_LOGIN;
import static com.anlisoft.vsafe.models.data.Global.KEY_USER_PASSWORD;
import static com.anlisoft.vsafe.models.data.Global.MobileData;
import static com.anlisoft.vsafe.models.data.Global.WifiData;
import static com.anlisoft.vsafe.models.data.Global.gMunicipalidadesListado;
import static com.anlisoft.vsafe.models.data.Global.gSessionManager;
import static com.anlisoft.vsafe.models.data.Global.gUserProfileData;
import static com.anlisoft.vsafe.models.data.Global.gUserSessionData;
import static com.anlisoft.vsafe.models.data.Global.gVsafeWebServices;


public class SplashScreenActivity extends AppCompatActivity {

    private ProgressBar pbLoad;
    private AlertDialog alertDialog;
    private SplashScreenViewModel mSplashScreenViewModel;
    private SplashScreenRepository mSplashScreenRepository;
    private static final String TAG = "vsafe-demo";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        ConnectionLiveData connectionLiveData = new ConnectionLiveData(getApplicationContext());

        // Initialize view components layout for SplashScreenActivity:
        initContentViewComponents();

        // Create or get an existing instance of the repository for SplashScreenActivity:
        mSplashScreenRepository = SplashScreenRepository.getInstance();

        // Set the api source webservice:
        mSplashScreenRepository.setWebServiceApiSource(Global.BASE_URL_MUNICIPALIDAD, IServiceVsafe.class);
        mSplashScreenRepository.initSessionManager(this);

        // Set the viewmodel for SplashScreen:
        mSplashScreenViewModel = ViewModelProviders.of(SplashScreenActivity.this).get(SplashScreenViewModel.class);

        // Initialize the viewmodel for SplashScreenActivity:
        mSplashScreenViewModel.initialize();

        // Observe changes in the network:
        connectionLiveData.observe(this, new Observer<ConnectionModel>() {
            @Override
            public void onChanged(@Nullable ConnectionModel connection) {
                //every time connection state changes, we'll be notified and can perform action accordingly
                if (connection != null) {
                    if (connection.getIsConnected()) {
                        switch (connection.getType()) {

                            case WifiData:
                            case MobileData:

                                Log.d(TAG, "onChanged:  data network ON");
                                // Set the initial load progress bar:
                                pbLoad.setVisibility(View.VISIBLE);

                                // If there is an alert dialog? dismiss:
                                if (alertDialog != null) {
                                    alertDialog.dismiss();
                                }


                                // Observe changes if listado municipalidades is not empty:
                                mSplashScreenViewModel.getMunicipalidades().observe(
                                        SplashScreenActivity.this,
                                        new Observer<MunicipalidadResponse>() {
                                            @Override
                                            public void onChanged(MunicipalidadResponse municipalidadResponse) {

                                                if (municipalidadResponse == null) {
                                                    gMunicipalidadesListado.clear();
                                                    Municipalidad municipalidadListadoDefault = new Municipalidad();
                                                    municipalidadListadoDefault.setDescription("Error al descargar datos");
                                                    gMunicipalidadesListado.add(0, municipalidadListadoDefault);
                                                } else {
                                                    gMunicipalidadesListado.clear();
                                                    Municipalidad municipalidadListadoDefault = new Municipalidad();
                                                    municipalidadListadoDefault.setDescription(getString(R.string.sp_text_municipalidad));
                                                    gMunicipalidadesListado.add(0, municipalidadListadoDefault);
                                                    gMunicipalidadesListado.addAll(municipalidadResponse.getMunicipalidad());
                                                }


                                                // Check if user selected autologin session:
                                                if (mSplashScreenRepository.isUserLogged()) {

                                                    String login = gSessionManager.getString(KEY_USER_LOGIN, "");
                                                    String password = gSessionManager.getString(KEY_USER_PASSWORD, "");
                                                    String lastSavedUrlSession = mSplashScreenRepository.getCurrentUrlBase(getApplicationContext());
                                                    mSplashScreenRepository.setWebServiceApiSource(lastSavedUrlSession, IServiceVsafe.class);
                                                    initializeUserAutoLogin(login, password);
                                                    Log.d(TAG, "onChanged: " + login +
                                                            " : " + password + " : " +
                                                            " : " + lastSavedUrlSession +
                                                            " : " + mSplashScreenRepository.isUserLogged());

                                                } else {
                                                    Log.d(TAG, "onChanged: " + mSplashScreenRepository.isUserLogged());
                                                    Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                    pbLoad.setVisibility(View.GONE);

                                                }
                                            }
                                        });
                                break;
                        }
                    } else {
                        pbLoad.setVisibility(View.GONE);
                        AlertDialog.Builder buildDiag = new AlertDialog.Builder(SplashScreenActivity.this);
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
                        Log.d(TAG, "onChanged:  Data Network OFF...");
                    }
                }
            }
        });
    }

    /**
     * Initialize user login in the server vsafe:
     *
     * @param login:    username
     * @param password: password
     */
    private void initializeUserAutoLogin(String login, String password) {
        gVsafeWebServices.initUserLogin(login, password).enqueue(new Callback<UserLoginResponse>() {
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

                        gUserProfileData.setUserId(gUserSessionData.getId().toString());
                        gUserProfileData.setUserFirstName(gUserSessionData.getFirstName());
                        gUserProfileData.setUserLastName(gUserSessionData.getLastName());
                    }
                    Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    pbLoad.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserLoginResponse> call,
                                  @NonNull Throwable t) {
                Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                pbLoad.setVisibility(View.GONE);
                Snackbar.make(getWindow().getDecorView().getRootView(),
                        getString(R.string.diag_msgbody_internet_not_available),
                        Snackbar.LENGTH_LONG).setAction(getString(R.string.diag_btn_close), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                })
                        .setActionTextColor(ContextCompat.getColor(
                                SplashScreenActivity.this, R.color.colorAccent))
                        .show();
            }
        });
    }

    public void initContentViewComponents() {
        pbLoad = findViewById(R.id.pb_load);
    }


}
