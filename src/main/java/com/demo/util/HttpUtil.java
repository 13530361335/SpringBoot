package com.demo.util;

import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;


public class HttpUtil {

    private final static Logger logger = LoggerFactory.getLogger(HttpUtil.class);

    public static Document getHtml(String url) {
        try {
            return Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.119 Safari/537.36")
                    .referrer(getReferer(url))
                    .timeout(5000)
                    .get();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * 下载文件
     *
     * @param url
     * @param savePath
     */
    public static void downLoad(String url, String savePath) {
        InputStream in = null;
        OutputStream out = null;
        try {
            URLConnection conn = new URL(url).openConnection();
            conn.setRequestProperty("referer", getReferer(url));
            conn.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.119 Safari/537.36");
            conn.connect();

            String contentDisposition = conn.getHeaderField("Content-Disposition");
            String fileName = contentDisposition == null ? url.substring(url.lastIndexOf("/") + 1) : contentDisposition.substring(contentDisposition.lastIndexOf(";") + 1);
            String contentType = conn.getContentType();
            long contentLength = conn.getContentLengthLong();
            logger.info("文件名:[" + fileName + "], 类型:[" + contentType + "], 大小:[" + contentLength + "]");

            in = conn.getInputStream();
            out = new FileOutputStream(new File((savePath + fileName)));
            byte[] bytes = new byte[1024];
            int len;
            while ((len = in.read(bytes)) != -1) {
                out.write(bytes, 0, len);
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    private static String getReferer(String url) {
        String[] urls = url.split("://");
        String protocol = urls[0];
        String uri = urls[1];
        uri = uri.substring(0, uri.indexOf("/"));
        String referer = protocol + "://" + uri;
        return referer;
    }


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
