package com.demo.util;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class FastIOUtil {

    private final static Logger logger = LoggerFactory.getLogger(FastIOUtil.class);

    /**
     * 文件下载时设置响应头
     *
     * @param response
     * @param request
     * @param fileName
     * @return
     */
    public static boolean setDownHeader(HttpServletRequest request, HttpServletResponse response, String fileName) {
        response.setHeader("Content-Type", "application/octet-stream");
        try {
            if (isIEBrowser(request)) {
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
     *
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

    /**
     * @param in
     * @return
     */
    public static String parseImg(InputStream in) {
        byte[] data = null;
        try {
            data = new byte[in.available()];
            in.read(data);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } finally {
            IOUtils.closeQuietly(in);
        }
        return new BASE64Encoder().encode(data);
    }

    /**
     * @param imgStr
     * @param out
     * @return
     */
    public static boolean generateImg(String imgStr, OutputStream out) {
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            byte[] b = decoder.decodeBuffer(imgStr);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }
            out.write(b);
            out.flush();
            return true;
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return false;
        } finally {
            IOUtils.closeQuietly(out);
        }
    }


}
