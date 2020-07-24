package com.example.sosocar.MyUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;

public class HttpUtil {
    public static  MediaType Json=MediaType.parse("application/json;charset=utf-8");
    public static  MediaType File=MediaType.parse("image/*");
    public static  String url="http://3r2x705117.zicp.vip";
    public static OkHttpClient client = new OkHttpClient.Builder().connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5,TimeUnit.SECONDS).build();//设置连接超时时间

}
