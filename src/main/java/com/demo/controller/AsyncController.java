package com.demo.controller;

import com.demo.service.AsyncService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Api(description = "单向推送")
@RequestMapping("async")
@RestController
public class AsyncController {

    @Autowired
    private AsyncService asyncService;

    @GetMapping("register")
    public void register(HttpServletRequest request, HttpServletResponse response, String key){
        asyncService.register(request,response,key);
    }

    @GetMapping("pushMessage")
    public void pushMessage(String key,String message){
        asyncService.pushMessage(key,message);
    }

}
