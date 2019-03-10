package com.demo;

import com.demo.entity.UserInfo;
import com.demo.util.ExcelUtil;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
        Map<String, Object> map1 = new HashMap<>();
        map1.put("id", 12);
        map1.put("name", "靖敏");
        Map<String, Object> map2 = new HashMap<>();
        map2.put("id", "23");
        map2.put("name", new Date());
        List list = new ArrayList();
        list.add(map1);
        list.add(map2);

        String[] fields = {"id", "name"};
        String[] excelFields = {"主键", "名字"};
        ExcelUtil.toXlsx(new FileInputStream("D:/Temp/1.xlsx"),new FileOutputStream("D:/Temp/map2.xlsx"),fields,list);
    }

    @Test
    public void toXlsx2() throws IOException {
        Map<String, Object> map1 = new HashMap<>();
        map1.put("id", 12);
        map1.put("name", "靖敏");
        Map<String, Object> map2 = new HashMap<>();
        map2.put("id", "23");
        map2.put("name", new Date());
        List list = new ArrayList();
        list.add(map1);
        list.add(map2);

        String[] fields = {"id", "name"};
        String[] excelFields = {"主键", "名字"};
        ExcelUtil.toXlsx(new FileOutputStream("D:/Temp/toXlsx2.xlsx"),list);
    }

    @Test
    public void change() throws ClassNotFoundException, SQLException {
        final String url = "jdbc:mysql://localhost:3306/demo?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai";
        final String name = "com.mysql.jdbc.Driver";
        final String user = "root";
        final String password = "19930913";
        Class.forName(name); // 指定连接类型
        Connection conn = DriverManager.getConnection(url, user, password);// 获取连接
        conn.setAutoCommit(false);
        PreparedStatement ps = conn.prepareStatement("");//准备执行语句
        String sql = "INSERT INTO `user_info` VALUES (2, '000001', '123456', '761878367@qq.com', '13530361335', '深圳', '靖敏', NULL)";
        ps.addBatch(sql);
        ps.executeBatch();
        conn.commit();
    }

}
