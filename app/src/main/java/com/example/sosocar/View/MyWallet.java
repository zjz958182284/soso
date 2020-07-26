package com.example.sosocar.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sosocar.R;

public class MyWallet extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mywallet);
        findViewById(R.id.t10).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyWallet.this.setResult(RESULT_OK,new Intent().putExtra("10",10));
                MyWallet.this.finish();
            }
        });
    }
}
