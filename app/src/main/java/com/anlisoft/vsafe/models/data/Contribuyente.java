package com.anlisoft.vsafe.models.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Contribuyente {
    @SerializedName("Name")
    @Expose
    private String Name = "empty";
    @SerializedName("DNI")
    @Expose
    private String DNI = "empty";
    @SerializedName("Phone")
    @Expose
    private String Phone = "empty";
    @SerializedName("Email")
    @Expose
    private String Email = "empty";

    public Contribuyente() {
    }

    public Contribuyente(String name , String DNI, String phone, String email) {
        Name = name;
        this.DNI = DNI;
        Phone = phone;
        Email = email;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDNI() {
        return DNI;
    }

    public void setDNI(String DNI) {
        this.DNI = DNI;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }
}
