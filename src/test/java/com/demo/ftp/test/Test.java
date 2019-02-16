package com.demo.ftp.test;


public class Test {
    public static void main(String[] args) {
        System.out.println(getMap());
    }

    public static String getMap() {
        try {
            Integer.parseInt("a");
            return "try";
        }
        catch (Exception e) {
        }
        finally {
        }
        return "return";
    }
}
