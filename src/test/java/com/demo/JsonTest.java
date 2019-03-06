package com.demo;

import com.demo.entity.UserInfo;
import com.demo.util.JsonUtil;
import org.junit.Test;


public class JsonTest {

    @Test
    public void toJSONString() {
        UserInfo userInfo = new UserInfo();
        userInfo.setAccount("jing");
        userInfo.setPassword("123456");
        String jsonString = JsonUtil.toString(userInfo);
        System.out.println(jsonString);
    }
}
