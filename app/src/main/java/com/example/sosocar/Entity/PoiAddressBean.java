package com.example.sosocar.Entity;

import java.io.Serializable;

public class PoiAddressBean implements Serializable {

    private String longitude;//经度
    private String latitude;//纬度
    private String text;//信息内容
    public  String detailAddress;//详细地址
    public  String province;//省
    public  String city;//城市
    public  String district;//区域

    public PoiAddressBean(String lon, String lat, String detailAddress, String text, String province, String city, String district){
        this.longitude = lon;
        this.latitude = lat;
        this.text = text;
        this.detailAddress = detailAddress;
        this.province = province;
        this.city = city;
        this.district = district;


    }

    public String getLongitude() {
        return longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getText() {
        return text;
    }

    public String getDetailAddress() {
        return detailAddress;
    }

    public String getProvince() {
        return province;
    }

    public String getCity() {
        return city;
    }

    public String getDistrict() {
        return district;
    }

}