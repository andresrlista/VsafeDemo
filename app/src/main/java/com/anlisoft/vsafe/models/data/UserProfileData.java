package com.anlisoft.vsafe.models.data;

import android.location.Location;

public class UserProfileData {

    private String userId;
    private String userFirstName;
    private String userLastName;
    private String userEnviroment;
    private String userServerLocation;
    private Location userCurrtentLocation;


    public String getUserEnviroment() {
        return userEnviroment;
    }

    public String getUserServerLocation() {
        return userServerLocation;
    }

    public void setUserServerLocation(String userServerLocation) {
        this.userServerLocation = userServerLocation;
    }

    public void setUserEnviroment(String userEnviroment) {
        this.userEnviroment = userEnviroment;
    }

    public UserProfileData() {
    }

    public UserProfileData(String userId, String userFirstName, String userLastName, Location userCurrtentLocation) {
        this.userId = userId;
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        this.userCurrtentLocation = userCurrtentLocation;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    public Location getUserCurrtentLocation() {
        return userCurrtentLocation;
    }

    public void setUserCurrtentLocation(Location userCurrtentLocation) {
        this.userCurrtentLocation = userCurrtentLocation;
    }
}
