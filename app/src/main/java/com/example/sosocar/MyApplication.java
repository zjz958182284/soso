package com.example.sosocar;

import android.app.Application;

public class MyApplication extends Application {

    public static  String telephone;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

   public static  String ip;
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
