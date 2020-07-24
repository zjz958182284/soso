package com.example.sosocar;

import android.app.Application;

public class MyApplication extends Application {

    private String telephone;

    @Override
    public void onCreate() {
        super.onCreate();
        setTelephone("未登入");
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
}
