package com.example.sosocar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sosocar.Entity.UserBean;
import com.example.sosocar.MyUtils.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class RegisterPage extends AppCompatActivity {
    //public String url="http://3a27001y01.zicp.vip:80/registered";
    public String url= HttpUtil.url+ "/registered";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);
        Button registerBtn=findViewById(R.id.bt_register);
        final EditText et_register_username=findViewById(R.id.et_register_username);
        final EditText et_register_pwd_input=findViewById(R.id.et_register_pwd_input);
        final EditText et_register_city=findViewById(R.id.et_register_city);

        setOnFocusChangeErrMsg(et_register_username, "phone", "手机号格式不正确");//当输入账号FocusChange时，校验账号是否是手机号
        setOnFocusChangeErrMsg(et_register_pwd_input, "password", "密码必须不少于6位");//当输入密码FocusChange时，校验密码是否不少于6位

        //注册按钮的点击监听器
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 获取用户输入的账号和密码以进行注册
                String account = et_register_username.getText().toString();
                String password = et_register_pwd_input.getText().toString();
                String city= et_register_city.getText().toString();

                if (!(isTelphoneValid(account) && isPasswordValid(password) && !(et_register_username==null))) {
                    Toast.makeText(RegisterPage.this, "账户或密码不符合要求", Toast.LENGTH_SHORT).show();
                    return;
                }
                //异步注册
                asyncRegister(account,password,city);

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



    //注册功能
    public void asyncRegister(final String account, final String password, final String city){
        //创建新的进程
        new Thread(new Runnable() {
            @Override
            public void run() {
                UserBean userBean=new UserBean();//建立一个用户实体类
                userBean.setTelephone(account);//设置电话号码
                userBean.setPassword(password);//设置密码
                userBean.setCity(city);//设置城市
                //创建okHttp客户端
                OkHttpClient okHttpClient=new OkHttpClient
                        .Builder()
                        .connectTimeout(10000, TimeUnit.MILLISECONDS)
                        .build();
               //创建Gson,用来解析Json文件


                Gson gson=new Gson();
                String jsonStr=gson.toJson(userBean);//把userBean实体类转换成字符串形式
                Log.i("json",jsonStr);
                MediaType mediaType=MediaType.parse("application/json");
                final RequestBody requestBody=RequestBody.create(jsonStr,mediaType);//requestBody是要传输的内容
                //创建请求内容,传输requestBody的内容
                Request request=new Request
                        .Builder()
                        .post(requestBody)
                        .url(url)
                        .build();

                //用client创建请求任务
                Call task=okHttpClient.newCall(request);
                //发起异步请求
                task.enqueue(new Callback() {
                    @Override
                    //如果失败显示日志信息
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Log.d("Activity ","onFailure->"+e.toString());
                    }

                    @Override
                    //如果成功返回
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        int code=response.code();
                        Log.d("Testing ","code-->"+code);
                        if(code== HttpURLConnection.HTTP_OK){//如果状态码为200，表示返回数据成功
                            ResponseBody body=response.body();
                            if(body!=null){//如果返回数据不为空
//                                Log.d("Testing","result ==>"+body.string());//日志显示数据内容
                                String responseBodyStr=body.string();//把内容转成字符串类型
                                JsonObject responseBodyJsonObject=(JsonObject) new JsonParser().parse(responseBodyStr);
                                String content=responseBodyJsonObject.get("status").getAsString();//获取content字段的值
                                System.out.println(content);
                                if(content.equals("1")) {//如果字段的字符串为1
                                  System.out.println("注册成功");
                                    showToastInThread(RegisterPage.this, "注册成功");
                                    Intent intent = new Intent(RegisterPage.this, LoginPage.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else{
                                 System.out.println("注册失败");
                                 showToastInThread(RegisterPage.this, "注册失败");
                                    return;
                                }

                            }
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
                Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
            }
        });
    }
}
