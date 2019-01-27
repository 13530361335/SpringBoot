package com.demo.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import org.apache.catalina.connector.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@Api(description = "单向推送")
@RequestMapping("async")
@RestController
public class AsyncController {

    private final static int DEFAULT_TIME_OUT = 60 * 60 * 1000;

//    private static HashMap<String, ArrayList<AsyncContext>> links = new HashMap<>();

    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("connect")
    public void connect(HttpServletRequest request, HttpServletResponse response, String key) {
        ArrayList<AsyncContext> asyncContexts = (ArrayList<AsyncContext>) redisTemplate.opsForHash().get("AsyncContext",key);
//        ArrayList<AsyncContext> asyncContexts = links.get(key);
        if (CollectionUtils.isEmpty(asyncContexts)) {
            asyncContexts = new ArrayList<>();
//            links.put(key, asyncContexts);
        }
        request.getAsyncContext();
        response.setContentType("text/event");//持续响应
        AsyncContext asyncContext = request.startAsync();//异步请求
        asyncContexts.add(asyncContext);

        redisTemplate.opsForHash().put("AsyncContext",key,new HttpServletRequestWrapper(request));
    }

    @GetMapping("push")
    public String push(String key, String message){
//        List<AsyncContext> asyncContexts = links.get(key);
        List<AsyncContext> asyncContexts =  (ArrayList<AsyncContext>) redisTemplate.opsForHash().get("AsyncContext",key);
        int total = asyncContexts == null ? 0 : asyncContexts.size();
        int success = 0;
        if (total == 0) {
            return "连接总数:0";
        }
        if ("close".equalsIgnoreCase(message)) { //关闭连接
            for (AsyncContext asyncContext : asyncContexts) {
                asyncContext.complete();
            }
            redisTemplate.opsForHash().delete("AsyncContext",key);
//            links.remove(key);
            return "关闭连接:" + total;
        }
        for (AsyncContext asyncContext : asyncContexts) {
            try {
                PrintWriter pw = asyncContext.getResponse().getWriter();
                pw.println(message);
                pw.flush();
                success++;
            } catch (IOException e) {
                asyncContext.complete();
                asyncContexts.remove(asyncContext);
            }
        }
        redisTemplate.opsForHash().put("AsyncContext",key,asyncContexts);
        return "连接总数:" + total + ";推送成功:" + success;
    }


}

