package com.example.sosocar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sosocar.MyUtils.ToastUtil;

public class CancelOrder extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cancel_reason);
        String id =getIntent().getStringExtra("id");

        findViewById(R.id.cancel_order_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.show(CancelOrder.this,"提交成功,您已取消改订单");
                Intent intent=new Intent(CancelOrder.this,MainActivity.class);
                startActivity(intent);
            }
        });

    }
}
