package com.example.sosocar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class MyOrder extends AppCompatActivity {

    private RecyclerView recyclerView;
    private  RecyclerView.Adapter adapter;
    private List<String> datas=new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_order);

        for(int i=0;i<20;++i)
            datas.add((String.valueOf(i)));
        recyclerView=findViewById(R.id.order_lists);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter=new RecycleViewAdapter(this,datas));
        adapter.notifyDataSetChanged();
    }
}
