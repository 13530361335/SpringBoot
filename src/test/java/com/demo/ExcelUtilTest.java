package com.demo;

import com.demo.entity.UserInfo;
import com.demo.util.ExcelUtil;
import org.junit.Test;

import java.io.*;
import java.util.*;

public class ExcelUtilTest {


    @Test
    public void toList() throws IOException {
        InputStream in = new FileInputStream("D:/Temp/1.xlsx");
        String[] fields = {"account", "password"};
        List<UserInfo> data = ExcelUtil.toList(in, 0, 0, fields, UserInfo.class);
        data.forEach(e -> {
            System.out.println(e.getAccount());
            System.out.println(e.getPassword());
            System.out.println(e.getAddress());
        });
    }

    @Test
    public void toXlsx() throws IOException {
        UserInfo userInfo = new UserInfo();
        userInfo.setName("靖敏");
        userInfo.setId(1);
        UserInfo userInfo1 = new UserInfo();
        userInfo1.setName("许");
        userInfo1.setId(2);

        List<UserInfo> userInfos = new ArrayList();
        userInfos.add(userInfo);
        userInfos.add(userInfo1);

        Map<String,Object>  map = new HashMap<>();
        map.put("id",12);
        map.put("name","靖敏");

        Map<String,Object>  map1 = new HashMap<>();
        map1.put("id","23");
        map1.put("name",new Date());
        List list = new ArrayList();
        list.add(map);
        list.add(map1);

        String[] fields = {"id", "name"};
        String[] excelFields = {"主键", "名字"};

        OutputStream out = new FileOutputStream("D:/Temp/map2.xlsx");

        ExcelUtil.toXlsx(list, fields, excelFields, out);
    }

    @Test
    public void change(){
        Object a = 1;
        if(a instanceof Double){
            System.out.println(1);
        }
        if(a instanceof Integer){
            System.out.println(1);
        }
    }

}
