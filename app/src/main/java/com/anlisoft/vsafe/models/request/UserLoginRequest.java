package com.anlisoft.vsafe.models.request;

public class UserLoginRequest {

    private String Login = "N/A";
    private String Password = "N/A";

    public UserLoginRequest() {
    }

    public String getLogin() {
        return Login;
    }

    public void setLogin(String login) {
        Login = login;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
