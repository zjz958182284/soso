package com.example.sosocar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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
    public String url="http://3a27001y01.zicp.vip:80//login?";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        TextView tv_register=findViewById(R.id.tv_register);//注册字体
        Button bt_login_submit=findViewById(R.id.bt_login_submit);//登入按钮
        final EditText et_login_username=findViewById(R.id.et_login_username);//用户名输入框
        final EditText et_login_pwd=findViewById(R.id.et_login_pwd);//密码输入框

        setOnFocusChangeErrMsg(et_login_username, "phone", "手机号格式不正确");//当输入账号FocusChange时，校验账号是否是手机号
        setOnFocusChangeErrMsg(et_login_pwd, "password", "密码必须不少于6位");//当输入密码FocusChange时，校验密码是否不少于6位


        //设置登入按钮的监听器
        bt_login_submit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // 获取用户输入的账号和密码以进行验证
                String account = et_login_username.getText().toString();
                String password = et_login_pwd.getText().toString();
                Intent intent =new Intent(LoginPage.this,MainActivity.class);
                startActivity(intent);

                et_login_pwd.clearFocus();
                // 发送URL请求之前,先进行校验

                if (!(isTelphoneValid(account) && isPasswordValid(password))) {
                    Toast.makeText(LoginPage.this, "账户或密码不符合要求", Toast.LENGTH_SHORT).show();
                    return;
                }
                asyncValidate(account,password);
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
        if (account == null && account.length()<=11) {
            return false;
        }
        // 利用正则表达式
        String pattern = "^[1][3,4,5,7,8][0-9]{9}$";
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


    //验证账户与密码
    public void asyncValidate(final String account, final String password){
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient okHttpClient=new OkHttpClient();
//                        .Builder()
//                        .connectTimeout(10000, TimeUnit.MILLISECONDS)
//                        .build();
                //建立表单
                FormBody formBody =new FormBody
                        .Builder()
                        .add("telephone",account)
                        .add("password",password)
                        .build();

                Request request=new Request
                        .Builder()
                .post(formBody)
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
                            String responseBodyStr=body.string();//把内容转成字符串类型
                            JsonObject responseBodyJsonObject=(JsonObject) new JsonParser().parse(responseBodyStr);
                            String content=responseBodyJsonObject.get("content").getAsString();//获取content字段的值
                            System.out.println(content);
                            if(content.equals("1")){//如果数据为1则为true
                                showToastInThread(LoginPage.this, "登入成功");
                                Intent intent =new Intent(LoginPage.this,Main2Activity.class);
                                startActivity(intent);
                            }
                            else{
                                showToastInThread(LoginPage.this, "登入失败");
                                return;
                            }
                        }
                        else{
                            showToastInThread(LoginPage.this, "登入失败");
                            return;
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
