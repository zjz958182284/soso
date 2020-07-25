package com.example.sosocar;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.sosocar.Entity.UserBean;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        List<UserBean> beans=new ArrayList<>();
        for(int i=0;i<5;++i) {
           UserBean bean= new UserBean();
           bean.setNickname("66");
           bean.setTelephone("dw");
            beans.add( bean);
        }
        UserBean bean= new UserBean();
        bean.setNickname("66");
        bean.setTelephone("dw");
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("telephone","13");
        jsonObject.put("nickname",null);
       bean= JSON.toJavaObject(jsonObject,UserBean.class);
      Object a=23.5;
       System.out.println(jsonObject.toJSONString());

    }
}