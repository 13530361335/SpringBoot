package com.demo;

import com.demo.util.HttpUtil;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Test;

public class HttpUtilTest {

    @Test
    public void download() {
        HttpUtil.downLoad("http://www.pptok.com/wp-content/uploads/2012/08/xunguang-9.jpg", "D:\\Temp\\");
    }

    @Test
    public void parseHtml() {
        Document document = HttpUtil.getHtml("https://me.csdn.net/mashuai720");
        Elements elements = document.select("dt h3 a");

        elements.forEach(e -> {
            String href =  e.attr("href");
            String title = e.text();
            System.out.println(href);
            System.out.println(title);
        });
    }


}
