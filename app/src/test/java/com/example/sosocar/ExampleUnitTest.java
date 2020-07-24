package com.example.sosocar;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.sosocar.Entity.UserBean;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

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
        JSONArray jsonArray= (JSONArray) JSONArray.toJSON(beans);
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("as",beans);

        System.out.println(jsonArray.toJSONString());
        System.out.println(jsonObject.toJSONString());
        System.out.println("dwdw");
        assertEquals(4, 2 + 2);

    }
}