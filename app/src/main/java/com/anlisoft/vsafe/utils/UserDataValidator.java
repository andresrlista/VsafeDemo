package com.anlisoft.vsafe.utils;

import com.anlisoft.vsafe.models.data.Global;
import com.anlisoft.vsafe.models.request.UserLoginRequest;

public class UserDataValidator {

    // Set the default value:
    private String userLogin = "";
    private String userPassword = "";
    private int authResult = Global.USERLOGIN_PASSWORD_NOT_VALID;

    public UserDataValidator() {
    }

    public UserDataValidator(UserLoginRequest userLoginRequest) {
        this.userLogin = userLoginRequest.getLogin();
        this.userPassword = userLoginRequest.getPassword();
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public int getAuthResult() {
        return authResult;
    }

    public void setAuthResult(int authResult) {
        this.authResult = authResult;
    }

    public int isValidUser() {

        if (getUserLogin().isEmpty()) {
            authResult = Global.USERLOGIN_NOT_VALID;
        }
        if (getUserPassword().length() < 3 || getUserPassword().isEmpty()) {
            authResult = Global.PASSWORD_NOT_VALID;
        }

        if (getUserLogin().isEmpty() && getUserPassword().isEmpty()) {
            authResult = Global.USERLOGIN_PASSWORD_NOT_VALID;
        }

        if (!getUserLogin().isEmpty() && !getUserPassword().isEmpty()) {
            authResult = Global.USERLOGIN_PASSWORD_VALID;
        }
        return authResult;
    }
}
