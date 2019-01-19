package com.demo.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

/**
 * @author: Joker Jing
 * @date: 2018/6/10
 */
public class HttpUtil {

    /**
     * 文件下载时设置响应头
     * @param response
     * @param request
     * @param fileName
     * @return
     */
    public static boolean setDownHeader(HttpServletRequest request, HttpServletResponse response, String fileName) {
        response.setHeader("Content-Type", "application/octet-stream");
        boolean isIE = isIEBrowser(request);
        try {
            if (isIE) {
                //解决IE浏览器下载时文件名乱码，以及空格和加号问题
                fileName = URLEncoder.encode(fileName, "utf-8").replaceAll("\\+", "%20");
            } else {
                //解决其它浏览器下载时文件名乱码
                fileName = new String(fileName.getBytes("utf-8"), "ISO-8859-1");
            }
        } catch (UnsupportedEncodingException e) {
            return false;
        }
        response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
        response.setHeader("Connection", "close");
        return true;
    }


    public static void setImgHeader(HttpServletResponse response) {
        response.setHeader("Content-Type", "image/png");
        response.setHeader("Connection", "close");
    }

    /**
     * 判断客户端是否为ie浏览器
     * @param request
     * @return
     */
    private static boolean isIEBrowser(HttpServletRequest request) {
        String[] IEs = {"MSIE", "Trident", "Edge"};
        String userAgent = request.getHeader("User-Agent");
        for (String ie : IEs) {
            if (userAgent.contains(ie)) {
                return true;
            }
        }
        return false;
    }


}