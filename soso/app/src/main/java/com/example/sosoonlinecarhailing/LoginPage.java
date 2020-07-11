package com.example.sosoonlinecarhailing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class LoginPage extends AppCompatActivity {
    public String url="http://3a27001y01.zicp.vip:80";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        TextView tv_register=findViewById(R.id.tv_register);//注册字体
        Button bt_login_submit=findViewById(R.id.bt_login_submit);//登入按钮
        EditText et_login_username=findViewById(R.id.et_login_username);//用户名输入框
        final EditText et_login_pwd=findViewById(R.id.et_login_pwd);//密码输入框

        setOnFocusChangeErrMsg(et_login_username, "phone", "手机号格式不正确");//当输入账号FocusChange时，校验账号是否是手机号
        setOnFocusChangeErrMsg(et_login_pwd, "password", "密码必须不少于6位");//当输入密码FocusChange时，校验密码是否不少于6位


        //设置登入按钮的监听器
        bt_login_submit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // 获取用户输入的账号和密码以进行验证
                String account = et_login_pwd.getText().toString();
                String password = et_login_pwd.getText().toString();

                et_login_pwd.clearFocus();
                // 发送URL请求之前,先进行校验
                if (!(isTelphoneValid(account) && isPasswordValid(password))) {
                   Toast.makeText(LoginPage.this, "账号或密码错误", Toast.LENGTH_SHORT).show();
                    return;
                }
                //进行验证
                asyncValidate(account, password);
            }
        });

        //设置注册字体监听器
        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //从登入界面跳转注册界面
                Intent registerPage=new Intent(LoginPage.this,RegisterPage.class);
                startActivity(registerPage);
            }
        });

    }

    // 校验账号不能为空且必须是中国大陆手机号
    private boolean isTelphoneValid(String account) {
        if (account == null) {
            return false;
        }
        // 利用正则表达式, 首位为1, 第二位为3-9, 剩下九位为 0-9, 共11位数字
        String pattern = "^[1]([3-9])[0-9]{9}$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(account);
        return m.matches();
    }

    // 校验密码不少于6位
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }


    /*
    当输入账号FocusChange时，校验账号是否是中国大陆手机号
    当输入密码FocusChange时，校验密码是否不少于6位
     */
    private void setOnFocusChangeErrMsg(final EditText editText, final String inputType, final String errMsg){
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener(){

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String inputStr=editText.getText().toString();
                if(!hasFocus){
                    if(inputType=="phone"){
                        if (isTelphoneValid(inputStr)) {
                            editText.setError(null);
                        } else {
                            editText.setError(errMsg);
                        }
                    }
                    if (inputType == "password") {
                        if (isPasswordValid(inputStr)) {
                            editText.setError(null);
                        } else {
                            editText.setError(errMsg);
                        }
                    }
                }
            }
        });
    }


    //无法连接数据库
    //等不到想要的结果，一直返回405
    public void asyncValidate(String account,String password){
        new Thread(new Runnable() {
            @Override
            public void run() {

                OkHttpClient okHttpClient=new OkHttpClient();
//                        .Builder()
//                        .connectTimeout(10000, TimeUnit.MILLISECONDS)
//                        .build();
                FormBody formBody =new FormBody
                        .Builder()
                        .add("tellphone","123456")
                        .add("password","123456")
                        .build();
                UserBean userBean=new UserBean();
                userBean.setTelephone("123456");
                userBean.setPassword("123456");
                Gson gson=new Gson();
                String jsonStr=gson.toJson(userBean);
                MediaType mediaType=MediaType.parse("application/json");
                RequestBody requestBody=RequestBody.create(jsonStr,mediaType);

                Request request=new Request
                        .Builder()
                .post(formBody)
//                        .get()
                        .url(url)
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
                            if(body!=null){
                                Log.d("Testing","result ==>"+body.string());
                                Intent intent =new Intent(LoginPage.this,MainActivity.class);
                                startActivity(intent);

                            }
                        }
                    }
                });
            }
        }).start();
    }


}
