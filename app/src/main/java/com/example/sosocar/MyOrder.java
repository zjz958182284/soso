package com.example.sosocar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.alibaba.fastjson.JSON;
import com.example.sosocar.Entity.OrderListBean;
import com.example.sosocar.MyUtils.HttpUtil;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MyOrder extends AppCompatActivity {

    private RecyclerView recyclerView;
    private  RecycleViewAdapter adapter;
    private List<OrderListBean> datas;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_order);
        Thread thread =new Thread(new Runnable() {
            @Override
            public void run() {
                getOrders();
            }
        });
        thread.start();

        recyclerView=findViewById(R.id.order_lists);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL));

    }

    private void  getOrders(){

        String phone =MyApplication.telephone;
        OkHttpClient client= HttpUtil.client;
         Request request=new Request.Builder().url(HttpUtil.url+"/getUserOrders?telephone="+phone)
                .get().build();

          client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String json=response.body().string();
                    Log.d("s",json);
                    datas=JSON.parseArray(json,OrderListBean.class);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.setAdapter(adapter=new RecycleViewAdapter(MyOrder.this,datas));
                            adapter.setListener(new RecycleViewAdapter.onItemClickListener() {
                                @Override
                                public void itemClick(int position) {
                                    Intent intent=new Intent(MyOrder.this,OrderDetail.class);
                                    intent.putExtra("id",datas.get(position).getId());
                                    startActivity(intent);
                                }
                            });
                            adapter.notifyDataSetChanged();
                        }
                    });


                }
            });




    }
}
