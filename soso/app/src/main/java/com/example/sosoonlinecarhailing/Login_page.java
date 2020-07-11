package com.example.sosoonlinecarhailing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Login_page extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        Button loginBtn=findViewById(R.id.bt_login_submit);
        //设置点击监听器
        loginBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                //跳转界面，从login_page界面跳转去MainActivity界面
                Intent intent=new Intent(Login_page.this,MainActivity.class);
                startActivity(intent);
            }
        });

    }
}
