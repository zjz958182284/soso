package com.example.sosocar;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.alibaba.fastjson.JSONObject;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.example.sosocar.MyUtils.HttpUtil;
import com.example.sosocar.driveroute.DrivingRouteOverlay;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity  {
    //布局控件
    private DrawerLayout mDrawerLayout;
    private LinearLayout mTrip,mAccount,mInfo, ll_select_start_location, ll_select_end_location,mReserveTime;
    private ImageButton my;
    private RadioGroup mSelectType;
    private TextView tv_start_location;
    private TextView tv_end_location;
    private PopupWindow matchingPopupWindow;
    private PopupWindow estimatedMoneyPopupWindow;
    private PopupWindow matchSuccessPopupWindow;
    private TextView tv_user_phone_number;
    private View rootView;
    private Button bt_hail_car;
    private TextView tv_waiting_time;

    private int currentOrderType=R.id.now_go;
    private static final int WRITE_COARSE_LOCATION_REQUEST_CODE = 255;//这号码没有什么意义
    private final String TAG = this.getClass().getName();

    //显示地图需要的变量
    private MapView mMapView;//地图控件
    private AMap aMap;//地图对象
    private UiSettings mUiSettings;//定义一个UiSettings对象
    //定位需要的声明
    private AMapLocationClient aMapLocationClient = null;//定位发起端
    private AMapLocationClientOption mLocationOption = null;//定位参数

    //标识，用于判断是否只显示一次定位信息和用户重新定位
    private boolean isFirstLoc = true;
    private LatLonPoint startPoint=null;//开始的点
    private LatLonPoint endPoint=null;//结束的点
    private Marker selfMarker=null;
    boolean isAddSelfMarker=false;
    private String currentCity="北京";//当前所在城市
    //请求Activity回转参数
    private final int REQUEST_END=0;
    boolean flag=true;
    MyApplication myApp;

    private  String origin_longitude;
    private  String origin_latitude;
    private String city;
    private String origin_address;
    private String destination_address;
    private String destination_longitude;
    private String destination_latitude;
    private String createTime;
    private String appointment;
    private String order_type;

    String createOrderUrl="http://3a27001y01.zicp.vip/order/create";




    @Override
   protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //初始化控件
        initWidget();
        //初始全局变量
        myApp = (MyApplication) getApplication();
        tv_user_phone_number.setText(myApp.getTelephone());
        //获取地图控件引用/
        mMapView = findViewById(R.id.map);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图，此方法须覆写，虚拟机需要在很多情况下保存地图绘制的当前状态。/
        mMapView.onCreate(savedInstanceState);
        //定义了一个地图view
        aMap = mMapView.getMap();
        aMap.setTrafficEnabled(true);// 显示实时交通状况
        //地图模式可选类型：MAP_TYPE_NORMAL,MAP_TYPE_SATELLITE,MAP_TYPE_NIGHT
        aMap.setMapType(AMap.MAP_TYPE_NORMAL);// 普通地图模式
        mUiSettings = aMap.getUiSettings();//实例化UiSettings类对象
        mUiSettings.setMyLocationButtonEnabled(true); //显示默认的定位按钮
        //aMap.setMyLocationEnabled(true);// 可触发定位并显示当前位置
        changeLocationStyle();//更改定位的小圆圈
        //申请权限
        requestPermissions();
        //开始定位
        initLoc();
        doPOISearch();
    }

    //初始化控件
    private void initWidget(){
        mDrawerLayout=findViewById(R.id.main_drawer);
        mAccount=findViewById(R.id.mywallet);
        mInfo=findViewById(R.id.myinfo);
        my=findViewById(R.id.my);
        ll_select_end_location =findViewById(R.id.ll_select_end_location);
        ll_select_start_location =findViewById(R.id.ll_select_start_location);
        mReserveTime=findViewById(R.id.select_time);
        mSelectType=findViewById(R.id.radio_group);
        mTrip=findViewById(R.id.mytrip);
        tv_end_location=findViewById(R.id.tv_end_location);
        tv_start_location=findViewById(R.id.tv_start_location);

        tv_user_phone_number=findViewById(R.id.tv_user_phone_number);


        my.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    mDrawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        // 跳向订单页
        mTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, MyOrder.class);
                    startActivity(intent);
                    //重置 是防止重复点击生效
            }
        });

        //跳向个人信息页
        mInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, MyInfo.class);
                    startActivity(intent);
            }
        });



    }

    //定位
    public void initLoc(){
        //初始化定位
        aMapLocationClient = new AMapLocationClient(getApplicationContext());
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(true);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        //给定位客户端对象设置定位参数
        aMapLocationClient.setLocationOption(mLocationOption);
        //给客户端设置一个listener来处理服务器返回定位数据
        //onLocationChanged 就是如果服务器客户端返回数据，调用的回调函数
        //aMapLocation 是服务器给客户端返回的定位数据
        aMapLocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {

                if (aMapLocation!=null){  //服务器是有响应的

                    if (aMapLocation.getErrorCode()==0){//定位成功，获取数据
                        Log.e("Amap","location succ address="+aMapLocation.getAddress());
                        Log.e("Amap","city="+aMapLocation.getCity());
                        Log.e("Amap","longtitude="+aMapLocation.getLongitude());
                        Log.e("Amap","latitude="+aMapLocation.getLatitude());

                        origin_address=aMapLocation.getAddress();
                        city=aMapLocation.getCity();
                        origin_latitude=Double.toString(aMapLocation.getLatitude());
                        origin_longitude=Double.toString(aMapLocation.getLongitude());

                        if(isAddSelfMarker==false) {
                            aMapLocation.setLatitude(39.9042);//北京的经度
                           aMapLocation.setLongitude(116.4074);//北京的纬度
                            //在此位置添加一个标记
                            addMarkerToMap(aMapLocation.getLatitude(), aMapLocation.getLongitude());
                            isAddSelfMarker=true;
                            if(isFirstLoc) {
                                //以自我为中心去展示地图
                                moveMap(aMapLocation.getLatitude(), aMapLocation.getLongitude());
                                isFirstLoc=false;
                            }
                            tv_start_location.setText(aMapLocation.getAddress());//设置text
                            currentCity=aMapLocation.getCity();//获取目前定位的城市
                        }
                        if(startPoint==null){
                            //得到起始坐标
                            startPoint=new LatLonPoint(aMapLocation.getLatitude(),aMapLocation.getLongitude());
                        }
                    }
                    else{//定位失败
                        Log.e("Amap","location error,code="+aMapLocation.getErrorCode()+",info="+aMapLocation.getErrorInfo());
                    }
                }
            }
        });
        //启动定位
        aMapLocationClient.startLocation();
    }


    //更改定位的小圆圈
    private void changeLocationStyle(){
        //定位的小图标 默认是蓝点
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        //myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.mipmap.map_pin));
        myLocationStyle.radiusFillColor(android.R.color.transparent);
        myLocationStyle.strokeColor(android.R.color.transparent);
        myLocationStyle.strokeWidth(0);
        aMap.setMyLocationStyle(myLocationStyle);
    }



    //自定义一个图钉，并且设置图标，当我们点击图钉时，显示设置的信息
    private MarkerOptions getMarkerOptions(AMapLocation amapLocation) {
        //设置图钉选项
        MarkerOptions options = new MarkerOptions();
        //图标
       // options.icon(BitmapDescriptorFactory.fromResource(R.mipmap.map_pin2));
        //位置
        options.position(new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude()));
        StringBuffer buffer = new StringBuffer();
        buffer.append(amapLocation.getCountry() + "" + amapLocation.getProvince() + "" + amapLocation.getCity() +  "" + amapLocation.getDistrict() + "" + amapLocation.getStreet() + "" + amapLocation.getStreetNum());
        //标题
        options.title(buffer.toString());
        //子标题
        options.snippet("这是测试，还未知道要写什么");
        //设置多少帧刷新一次图片资源
        options.period(60);
        return options;
    }


    //申请权限
    public void requestPermissions(){
        //在运行定位之前需要对定位权限进行检查和申请
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    WRITE_COARSE_LOCATION_REQUEST_CODE);//自定义的code
        }
    }

    //向固定的经纬度添加一个标记
    protected  void addMarkerToMap(double latitude,double longtitude) {
        //若需要设置marker的图标可以使用这行代码，把后面的icon换了就行了
//      selfMarker = amap.addMarker(new MarkerOptions().position(new LatLng(latitude, longtitude)).icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.map_pin))));

        //这里使用默认marker
        selfMarker = aMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longtitude)));

    }

    //以某个经纬度为中心来展示地图
    protected  void moveMap(double latitude,double longitude){
        LatLng lagLng=new LatLng(latitude,longitude);
        //移动amap地图，以之前的缩放比例展示
        aMap.animateCamera(new CameraUpdateFactory().newLatLngZoom(lagLng,aMap.getCameraPosition().zoom));
    }


    //绘制驾驶交通路径
    protected  void drawRouteLine(){
        //创建路径绘制 routeSearch
        RouteSearch routeSearch=new RouteSearch(getApplicationContext());
        RouteSearch.FromAndTo ft=new RouteSearch.FromAndTo(startPoint,endPoint);
        //设置路径搜索的query
        RouteSearch.DriveRouteQuery query=new RouteSearch.DriveRouteQuery(ft,RouteSearch.DRIVEING_PLAN_DEFAULT,null,null,"");

        //绘制路径设置一个callback函数
        routeSearch.setRouteSearchListener(new RouteSearch.OnRouteSearchListener() {
            @Override
            public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

            }

            @Override
            public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {
                //判断是否请求成功
                if (i !=1000) {
                    Log.e("Amap","搜索驾驶路径失败而");
                    return;
                }
                //得到路径(默认选第一个)
                DrivePath path=driveRouteResult.getPaths().get(0);

                //驾驶路径一个覆盖物
                DrivingRouteOverlay drivingRouteOverlay=new DrivingRouteOverlay(
                        getApplicationContext(),
                        aMap,
                        path,
                        startPoint,
                        endPoint,null
                );

                //先把之前的路径删除掉
                aMap.clear();
                //以适当的缩放显示路径
                drivingRouteOverlay.zoomToSpan();
                //去掉转弯的图标显示
                drivingRouteOverlay.setNodeIconVisibility(false);
                drivingRouteOverlay.setThroughPointIconVisibility(false);
                //将路径添加到地图
                drivingRouteOverlay.addToMap();
            }


            @Override
            public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {

            }

            @Override
            public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

            }
        });
        //启动路径所有 将query代进去
        routeSearch.calculateDriveRouteAsyn(query);
    }


    //POI搜索
    protected void doPOISearch(){

        ll_select_start_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag=true;
                Intent poiSearchIntent1 =new Intent(MainActivity.this,PoiSearchPage.class);
                startActivityForResult(poiSearchIntent1,1);
            }
        });


        ll_select_end_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag=false;
                Intent poiSearchIntent =new Intent(MainActivity.this,PoiSearchPage.class);
                startActivityForResult(poiSearchIntent,1);
            }
        });

//                //开始搜索POI兴趣点
//                Log.e("Amap","click");
//                //拿到用户搜索地址关键词
//                String startLocation=tv_start_location.getText().toString();
//                //开始搜索POI
//                //创建一个搜索条件对象query
//                PoiSearch.Query query=new PoiSearch.Query("北京","",currentCity);
//                //创建一个POISearch和query关联
//                PoiSearch poiSearch=new PoiSearch(getApplicationContext(),query);
//                //给search绑定一个回调函数
//                poiSearch.setOnPoiSearchListener(new PoiSearch.OnPoiSearchListener() {
//                    @Override
//                    public void onPoiSearched(PoiResult poiResult, int i) {
//                        //处理得到返回的POI兴趣点集合 poiResult
//                        if(i!=1000){//1000是请求正常
//                            Log.e("Amap","poi Search error code ="+i);
//                        }
//                        else{
//                            //搜索成功
//                            List<PoiItem> poiItemList = poiResult.getPois();
//
//                            for (int index=0;index<poiItemList.size();index++){
//                                //处理每个已经搜索到的兴趣点
//                                Log.e("Amap","搜索到的兴趣点有");
//                                PoiItem item=poiItemList.get(index);
//                                Log.e("Amap","poi title ="+item.getTitle()
//                                        +"latitude = "+item.getLatLonPoint().getLatitude()
//                                        +"longitude = "+item.getLatLonPoint().getLongitude());
//                                //给每个搜索到的定位点画一个标记
//                                addMarkerToMap(item.getLatLonPoint().getLatitude(),item.getLatLonPoint().getLongitude());
//
//                                //默认以第一个兴趣点为我们坐标点
//                                endPoint=new LatLonPoint(item.getLatLonPoint().getLatitude(),item.getLatLonPoint().getLongitude());
//
//                                //画出规划路径
//                                drawRouteLine();
//                                if (index==0){
//                                    break;
//                                }
//                            }
//                        }
//                    }
//                    @Override
//                    public void onPoiItemSearched(PoiItem poiItem, int i) {
//
//                    }
//                });
//                //启动异步搜索
//                poiSearch.searchPOIAsyn();

    }



    //用户选择允许或拒绝后，会回调onRequestPermissionsResult方法, 该方法类似于onActivityResult方法
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //可在此继续其他操作。
    }


    //获取poi界面的返回数据
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_END&&resultCode==RESULT_OK){


        }
        if (resultCode==1) {
            if (flag) {
                Bundle bundle = data.getExtras();
                setStartPoint(bundle);
            } else {
                Bundle bundle = data.getExtras();
                setEndPoint(bundle);
            }
        }
    }

    public void setStartPoint(Bundle bundle){
        tv_start_location.setText(bundle.getString("addressDetail"));
        double latitude = Double.valueOf(bundle.getString("latitude"));
        // endPoint.setLatitude(latitude);
        double longitude = Double.valueOf(bundle.getString("longitude"));
        //endPoint.setLatitude(longitude);
        origin_longitude=bundle.getString("longitude");
        origin_latitude=bundle.getString("latitude");
        origin_address=bundle.getString("addressDetail");
        city=bundle.getString("city");
        startPoint = new LatLonPoint(latitude, longitude);
        moveMap(latitude,longitude);
        addMarkerToMap(latitude,longitude);
        if(endPoint!=null) {
            //画出规划路径
            drawRouteLine();
        }
    }



    public void setEndPoint(Bundle bundle){
        tv_end_location.setText(bundle.getString("addressDetail"));
        double latitude = Double.valueOf(bundle.getString("latitude"));
        // endPoint.setLatitude(latitude);
        double longitude = Double.valueOf(bundle.getString("longitude"));
        //endPoint.setLatitude(longitude);
        destination_longitude=bundle.getString("longitude");
        destination_latitude=bundle.getString("latitude");
        destination_address=bundle.getString("addressDetail");
        endPoint = new LatLonPoint(latitude, longitude);
        if(startPoint!=null) {
            //画出规划路径
            drawRouteLine();
            showEstimatedMoneyWindow();
        }
    }

    public  void showMatchingWindow() {
        View view = LayoutInflater.from(this).inflate(R.layout.match_ing, null);
        matchingPopupWindow = new PopupWindow(this);
        matchingPopupWindow.setContentView(view);//设置PopupWindow布局文件
        matchingPopupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);//设置PopupWindow宽
        matchingPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);//设置PopupWindow高
        rootView = LayoutInflater.from(this).inflate(R.layout.activity_main, null);//父布局
        matchingPopupWindow.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
        matchingPopupWindow.setOutsideTouchable(true);
        matchDriver();
    }


    public void showEstimatedMoneyWindow(){
        View view= LayoutInflater.from(this).inflate(R.layout.call_for_car,null);
        estimatedMoneyPopupWindow=new PopupWindow(this);
        estimatedMoneyPopupWindow.setContentView(view);//设置PopupWindow布局文件
        estimatedMoneyPopupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);//设置PopupWindow宽
        estimatedMoneyPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);//设置PopupWindow高
        rootView =LayoutInflater.from(this).inflate(R.layout.activity_main, null);//父布局
        estimatedMoneyPopupWindow.showAtLocation(rootView, Gravity.BOTTOM,0,0);
        estimatedMoneyPopupWindow.setOutsideTouchable(false);//点击外部区域消失
        bt_hail_car=view.findViewById(R.id.bt_hail_car);

        bt_hail_car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMatchingWindow();
                estimatedMoneyPopupWindow.dismiss();
            }
        });
    }

    public void showMatchSuccessWindow(){
        View view= LayoutInflater.from(this).inflate(R.layout.match_success,null);
        matchSuccessPopupWindow=new PopupWindow(this);
        matchSuccessPopupWindow.setContentView(view);//设置PopupWindow布局文件
        matchSuccessPopupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);//设置PopupWindow宽
        matchSuccessPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);//设置PopupWindow高
        rootView =LayoutInflater.from(this).inflate(R.layout.activity_main, null);//父布局
        matchSuccessPopupWindow.showAtLocation(rootView, Gravity.BOTTOM,0,0);
        matchSuccessPopupWindow.setOutsideTouchable(true);//点击外部区域消失

    }

    public void matchDriver(){
        SimpleDateFormat sdf = new SimpleDateFormat();// 格式化时间
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss");//
        Date date = new Date();// 获取当前时间
         createTime=date.toString();
         appointment=date.toString();

        order_type="1";
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient okHttpClient=HttpUtil.client; //采用单例模式，因为这个OkhttpClient不需要每次都实例化
//                        .Builder()
//                        .connectTimeout(10000, TimeUnit.MILLISECONDS)
//                        .build();
                JSONObject jsonObject=new JSONObject();
                           jsonObject.put("telephone",tv_user_phone_number.getText().toString().trim());
                           jsonObject.put("city",city);
                           jsonObject.put("origin_address",origin_address);
                           jsonObject.put("origin_longitude",origin_longitude);
                           jsonObject.put("origin_latitude",origin_latitude);
                           jsonObject.put("destination_address",destination_address);
                           jsonObject.put("destination_longitude",destination_longitude);
                           jsonObject.put("destination_address",destination_address);
                           jsonObject.put("destination_latitude",destination_latitude);
                           jsonObject.put("createTime",createTime);
                           jsonObject.put("appointment",appointment);
                RequestBody requestBody=RequestBody.create(HttpUtil.Json,jsonObject.toJSONString());
                         Request  request=new Request
                        .Builder()
                        .post(requestBody)
                        .url(createOrderUrl)
                        .build();

                Call task=okHttpClient.newCall(request);
                task.enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Log.d("Activity ","onFailure->"+e.toString());
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        int code=response.code();
                        Log.d("Testing ","code-->"+code);
                        if(code== HttpURLConnection.HTTP_OK){
                            ResponseBody body=response.body();
                            String responseBodyStr=body.string();//把内容转成字符串类型
                            JsonObject responseBodyJsonObject=(JsonObject) new JsonParser().parse(responseBodyStr);
                            String id=responseBodyJsonObject.get("id").getAsString();//获取id字段的值
                            System.out.println(id);
                            showToastInThread(MainActivity.this, "订单号为："+id);
                            showMatchSuccessWindow();
                            matchingPopupWindow.dismiss();
                        }
                    }
                });
            }
        }).start();

    }


    // 实现在子线程中显示Toast
    private void showToastInThread(final Context context, final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
