package com.demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.UUID;

import com.mysql.jdbc.PreparedStatement;

public class InsertTest {

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        final String url = "jdbc:mysql://localhost:3306/demo?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai";
        final String name = "com.mysql.jdbc.Driver";
        final String user = "root";
        final String password = "19930913";
        Connection conn;
        Class.forName(name); // 指定连接类型
        conn = DriverManager.getConnection(url, user, password);
        if (conn == null) {
            System.out.println("获取连接失败");
        } else {
            System.out.println("获取连接成功");
            insert(conn);
        }

    }

    public static void insert(Connection conn) {
        Long begin = System.currentTimeMillis();
        String prefix = "INSERT INTO `user_info` VALUES ";
        try {
            conn.setAutoCommit(false);
            PreparedStatement ps = (PreparedStatement) conn.prepareStatement("");
            for (int i = 1; i <= 100; i++) {
                String suffix = "";
                for (int j = 1; j <= 100000; j++) {
                    suffix += "('" + UUID.randomUUID() + "', '000001', '123456', '761878367@qq.com', '13530361335', '深圳', '靖敏', NULL),";
                }
                String sql = prefix + suffix.substring(0, suffix.length() - 1);
                ps.addBatch(sql);
                ps.executeBatch();
                conn.commit();
                Long temp = System.currentTimeMillis();
                System.out.println("10万条数据插入花费时间 : " + (temp - begin) / 1000 + " s");
            }
            ps.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // 结束时间
        Long end = System.currentTimeMillis();
        System.out.println("1000万条数据插入花费时间 : " + (end - begin) / 1000 + " s");
        System.out.println("插入完成");
    }
}