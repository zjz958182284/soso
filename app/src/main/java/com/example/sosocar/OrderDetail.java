package com.example.sosocar;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSON;
import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.example.sosocar.ChatPage.Chat;
import com.example.sosocar.Entity.DriverBean;
import com.example.sosocar.Entity.OrderListBean;
import com.example.sosocar.MyUtils.HttpUtil;
import com.example.sosocar.MyUtils.ToastUtil;
import com.example.sosocar.View.MyWallet;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OrderDetail extends AppCompatActivity {


    private MapView mMapView;//地图控件
    private AMap aMap;//地图对象
    private UiSettings mUiSettings;//定义一个UiSettings对象
    private OrderListBean bean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_detail);
        Intent intent=getIntent();
        //获取地图控件引用/
        mMapView = findViewById(R.id.order_map);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图，此方法须覆写，虚拟机需要在很多情况下保存地图绘制的当前状态。/
        mMapView.onCreate(savedInstanceState);
        //定义了一个地图view
        aMap = mMapView.getMap();
        aMap.setTrafficEnabled(true);// 显示实时交通状况
        //地图模式可选类型：MAP_TYPE_NORMAL,MAP_TYPE_SATELLITE,MAP_TYPE_NIGHT
        aMap.setMapType(AMap.MAP_TYPE_NORMAL);// 普通地图模式
        mUiSettings = aMap.getUiSettings();//实例化UiSettings类对象
        mUiSettings.setMyLocationButtonEnabled(true); //显示默认的定位按钮


        final String id = intent.getStringExtra("index");
        System.out.println(id);
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                getOrder(id);
            }
        });
        thread.start();


    }
    private void  getOrder(String id){
        OkHttpClient client= HttpUtil.client;
        String url=HttpUtil.url+ "/order/getInfo?id="+id;
        Request request=new Request.Builder().url(url).get().build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                    String json=response.body().string();

                    bean= JSON.parseObject(json,OrderListBean.class);

                    System.out.println(json);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            switch (bean.getStatus()){
                                case 1:initSuccess();break;
                                case 2:initDuringTrip();break;
                                case 3:initFinish();break;
                            }
                        }
                    });
                }
            });


    }

    private void initSuccess(){
        View view=findViewById(R.id.success);
        view.setVisibility(View.VISIBLE);
        DriverBean driver=bean.getDriver();
        ((TextView)view.findViewById(R.id.car_number)).setText(driver.getCar_plate());
        ((TextView)view.findViewById(R.id.car_name)).setText(driver.getCar_name());
        ((TextView)view.findViewById(R.id.car_type)).setText(driver.getCar_model());
        ((TextView)view.findViewById(R.id.driver_name)).setText(driver.getName().substring(0,1)+"师傅");
        //((TextView)view.findViewById(R.id.driver_order_num)).setText(driver.getOrder_num()+"单");
        findViewById(R.id.success_cancel_order).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(OrderDetail.this,CancelOrder.class);
                intent.putExtra("id",bean.getId());
                startActivity(intent);

            }
        });

        findViewById(R.id.calling).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(OrderDetail.this, Chat.class);
                intent.putExtra("driver",bean.getDriver());
                startActivity(intent);
            }
        });

    }

    private void initMatchFail(){

        View view=findViewById(R.id.fail);
        view.setVisibility(View.VISIBLE);
        findViewById(R.id.re_match).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    private  void initDuringTrip(){
        View view=findViewById(R.id.during);
        view.setVisibility(View.VISIBLE);
        findViewById(R.id.call_police).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                SmsManager smsManager=SmsManager.getDefault();
                PendingIntent pi=PendingIntent.getActivity(OrderDetail.this,0,new Intent(),0);

                smsManager.sendTextMessage("19945102192",null,"我是xxx，乘坐鄂F0563顺风车遭遇危险，请迅速报警解救我"
                ,pi,null);
                ToastUtil.show(OrderDetail.this,"短信发送成功");

            }
        });

    }

    private void initFinish(){
        View view=findViewById(R.id.finish);
        view.setVisibility(View.VISIBLE);
        findViewById(R.id.pay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(OrderDetail.this,Pay.class);
                startActivity(intent);

            }
        });

        findViewById(R.id.choose_token).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(OrderDetail.this, MyWallet.class);
                startActivityForResult(intent,1);
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==0x123&&grantResults[0]== PackageManager.PERMISSION_GRANTED){
            SmsManager smsManager=SmsManager.getDefault();


        }
    }


    private void sendTextMessage(String phone){
        this.requestPermissions(new String[]{Manifest.permission.SEND_SMS},0x123);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1&&resultCode==RESULT_OK){
            ((TextView)findViewById(R.id.fee)).setText("18");
            ((TextView)findViewById(R.id.discount_fee)).setText("10");
        }
    }
}
