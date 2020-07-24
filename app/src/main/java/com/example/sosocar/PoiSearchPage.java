package com.example.sosocar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.example.sosocar.Entity.PoiAddressBean;
import com.example.sosocar.driveroute.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class PoiSearchPage extends AppCompatActivity implements PoiSearch.OnPoiSearchListener {

    private RecyclerView recyclerView;
    private EditText et_poi_search;
    private String keyWord = "";// 要输入的poi搜索关键字
    private PoiResult poiResult; // poi返回的结果
    private int currentPage = 0;// 当前页面，从0开始计数
    private PoiSearch.Query query;// Poi查询条件类
    private PoiSearch poiSearch;// POI搜索
    private ImageButton img_bt_back;
    private ImageButton img_bt_clear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poi_search_page);

        initView();
        onClick();
        initListener();
        initData();
    }


    private void initView() {
        recyclerView = findViewById(R.id.recyclerView);
        et_poi_search = findViewById(R.id.et_poi_search);
        img_bt_back=findViewById(R.id.img_bt_back);
        img_bt_clear=findViewById(R.id.img_bt_clear);
    }

    private  void onClick(){
        img_bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        img_bt_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_poi_search.setText("");
            }
        });
    }


    private void initListener() {
        et_poi_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                keyWord = String.valueOf(charSequence);
                if ("".equals(keyWord)) {
                    ToastUtil.show(PoiSearchPage.this,"请输入搜索关键字");
                    return;
                } else {
                    doSearchQuery();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    /**
     * 开始进行poi搜索
     */
    protected void doSearchQuery() {
        currentPage = 0;
        //不输入城市名称有些地方搜索不到
        query = new PoiSearch.Query(keyWord, "", "深圳");// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        //这里没有做分页加载了,默认给50条数据
        query.setPageSize(50);// 设置每页最多返回多少条poiitem
        query.setPageNum(currentPage);// 设置查第一页

        poiSearch = new PoiSearch(this, query);
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.searchPOIAsyn();
    }

    private void initData() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }


    /**
     * POI信息查询回调方法
     */
    @Override
    public void onPoiSearched(PoiResult result, int rCode) {
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getQuery() != null) {  // 搜索poi的结果
                if (result.getQuery().equals(query)) {  // 是否是同一条
                    poiResult = result;
                    ArrayList<PoiAddressBean> data = new ArrayList<PoiAddressBean>();//自己创建的数据集合
                    // 取得搜索到的poiitems有多少页
                    List<PoiItem> poiItems = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始
                    List<SuggestionCity> suggestionCities = poiResult
                            .getSearchSuggestionCitys();// 当搜索不到poiitem数据时，会返回含有搜索关键字的城市信息
                    for(PoiItem item : poiItems){
                        //获取经纬度对象
                        LatLonPoint llp = item.getLatLonPoint();
                        double lon = llp.getLongitude();
                        double lat = llp.getLatitude();

                        String title = item.getTitle();
                        String text = item.getSnippet();
                        String provinceName = item.getProvinceName();
                        String cityName = item.getCityName();
                        String adName = item.getAdName();
                        data.add(new PoiAddressBean(String.valueOf(lon), String.valueOf(lat), title, text,provinceName,
                                cityName,adName));
                    }
                    PoiKeywordSearchAdapter adapter = new PoiKeywordSearchAdapter(PoiSearchPage.this,data);
                    recyclerView.setAdapter(adapter);
                }
            } else {
                ToastUtil.show(PoiSearchPage.this, "没效果");
            }
        } else {
            ToastUtil.showerror(this, rCode);
        }

    }

    /**
     * POI信息查询回调方法
     */
    @Override
    public void onPoiItemSearched(PoiItem item, int rCode) {
        // TODO Auto-generated method stub

    }


    /**
     * 设置详情地址
     * @param detailAddress
     */
    public void setDetailAddress(String detailAddress) {
        et_poi_search.setText(detailAddress);
    }


    public void getAddress(PoiAddressBean poiAddressBean){
        Intent intent=new Intent();
        Bundle bundle=new Bundle();
        bundle.putString("addressDetail",poiAddressBean.getDetailAddress());
        bundle.putString("latitude",poiAddressBean.getLatitude());
        bundle.putString("longitude",poiAddressBean.getLongitude());
        intent.putExtras(bundle);

        setResult(1,intent);

        finish();
    }

}

