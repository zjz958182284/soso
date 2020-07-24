package com.example.sosocar.IpUtil;

import android.util.Log;

import com.example.sosocar.MyUtils.IpUtils;

import org.junit.Test;

public class IpUtilsTest {

    @Test
    public void getNetIp() {
        String ip= IpUtils.GetNetIp();
        Log.i("ip",ip);
    }
}