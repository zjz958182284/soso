package com.example.sosocar.Entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DriverBean implements Serializable {


    private static final long serialVersionUID = 2009835005316898442L;
    private String telephone;
    //密码
    private String password;
    //身份证号
    private String ID_num;
    //车名
    private String car_name;
    //车型
    private String car_model;

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getID_num() {
        return ID_num;
    }

    public void setID_num(String ID_num) {
        this.ID_num = ID_num;
    }

    public String getCar_name() {
        return car_name;
    }

    public void setCar_name(String car_name) {
        this.car_name = car_name;
    }

    public String getCar_model() {
        return car_model;
    }

    public void setCar_model(String car_model) {
        this.car_model = car_model;
    }

    public String getCar_plate() {
        return car_plate;
    }

    public void setCar_plate(String car_plate) {
        this.car_plate = car_plate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPerson_pic() {
        return person_pic;
    }

    public void setPerson_pic(String person_pic) {
        this.person_pic = person_pic;
    }

    public String getCar_pic() {
        return car_pic;
    }

    public void setCar_pic(String car_pic) {
        this.car_pic = car_pic;
    }

    public int getOrder_num() {
        return order_num;
    }

    public void setOrder_num(int order_num) {
        this.order_num = order_num;
    }

    public int getZero_star() {
        return zero_star;
    }

    public void setZero_star(int zero_star) {
        this.zero_star = zero_star;
    }

    public int getOne_star() {
        return one_star;
    }

    public void setOne_star(int one_star) {
        this.one_star = one_star;
    }

    public int getTwo_star() {
        return two_star;
    }

    public void setTwo_star(int two_star) {
        this.two_star = two_star;
    }

    public int getThree_star() {
        return three_star;
    }

    public void setThree_star(int three_star) {
        this.three_star = three_star;
    }

    public int getFour_star() {
        return four_star;
    }

    public void setFour_star(int four_star) {
        this.four_star = four_star;
    }

    public int getFive_star() {
        return five_star;
    }

    public void setFive_star(int five_star) {
        this.five_star = five_star;
    }

    public int getRegistration_day() {
        return registration_day;
    }

    public void setRegistration_day(int registration_day) {
        this.registration_day = registration_day;
    }

    public int getBalance() {
        return Balance;
    }

    public void setBalance(int balance) {
        Balance = balance;
    }

    public String getIP_address() {
        return IP_address;
    }

    public void setIP_address(String IP_address) {
        this.IP_address = IP_address;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public List<OrderListBean> getOrderLists() {
        return orderLists;
    }

    public void setOrderLists(List<OrderListBean> orderLists) {
        this.orderLists = orderLists;
    }

    //车牌号
    private String car_plate;
    //姓名
    private String name;
    //年龄
    private String age;
    //性别
    private String sex;
    //个人照片
    private String person_pic;
    //车辆照片
    private String car_pic;
    //接单量
    private int order_num;
    //0星数
    private int zero_star;
    //1星数
    private int one_star;
    //2星数
    private int two_star;
    //3星数
    private int three_star;
    //4星数
    private int four_star;
    //5星数
    private int five_star;
    //注册天数
    private int registration_day;
    //账户余额
    private int Balance;
    //手机外网IP地址
    private String IP_address;
    //工作状态
    private int status;
    //经度
    private double longitude;
    //纬度
    private double latitude;
    //城市
    private String city;


    private List<OrderListBean> orderLists=new ArrayList<>();
}
