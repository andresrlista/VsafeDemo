package com.anlisoft.vsafe.models.data;


import android.content.SharedPreferences;
import android.location.LocationManager;

import com.anlisoft.vsafe.interfaces.IServiceVsafe;
import com.anlisoft.vsafe.models.response.DenunciaCategoriaResponse;
import com.anlisoft.vsafe.models.response.DenunciaMotivoResponse;
import com.anlisoft.vsafe.models.response.UserLoginResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;

public class Global {

    public static SharedPreferences gSessionManager = null;
    public static SharedPreferences.Editor gSessionManagerEditor = null;
    public static DenunciaCategoriaResponse gCategoriasDenuncia = null;
    public static DenunciaMotivoResponse gMotivoDenunciaById = null;
    public static Retrofit gRetrofit = null;
    public static IServiceVsafe gVsafeWebServices = null;
    public static List<Municipalidad> gMunicipalidadesListado = new ArrayList<>();
    public static LocationManager gLocationManager;
    public static UserProfileData gUserProfileData = new UserProfileData();
    public static UserLoginResponse gUserSessionData = new UserLoginResponse();
    public final static String BASE_URL_MUNICIPALIDAD = "https://www.visualsatpe.com:94/api/munic/";
    public final static String BASE_URL_VISUALSAT_DEFAULT = "https://www.visualsatpe.com:200/api/";
    public static String BASE_URL_CURRENT = "";
    public static String KEY_MUNICIPALIDAD_NAME = "key_municipalidad_name";
    public static final String BASE_URL_REPORTE = "";
    public static final String KEY_USER_LOGIN = "key_login";
    public static final String KEY_USER_PASSWORD = "key_password";
    public static final String KEY_USERLOGGED = "key_userlogged";
    public static final String KEY_CURRENT_MUNICIPALIDAD = "key_user_municipalidad";
    public static final String KEY_VSAFE = "key_vsafe";
    public final static int USERLOGIN_NOT_VALID = 9990;
    public final static int PASSWORD_NOT_VALID = 9991;
    public final static int USERLOGIN_PASSWORD_VALID = 9992;
    public final static int USERLOGIN_PASSWORD_NOT_VALID = 9993;
    public final static int MobileData = 2;
    public final static int WifiData = 1;
}
