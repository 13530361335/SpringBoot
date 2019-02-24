package com.demo;

import com.alibaba.druid.filter.config.ConfigTools;

public class DruidTest {

    public static void main(String[] args) throws Exception {
        String password = "19930913";//密码
        String cipherText = ConfigTools.encrypt(password);
        String plainText = ConfigTools.decrypt(cipherText);
        System.out.println("公钥:" + ConfigTools.DEFAULT_PUBLIC_KEY_STRING);
        System.out.println("密文:" + cipherText);   //用私钥加密
        System.out.println("明文:" + plainText);    //用公钥解密
    }

}