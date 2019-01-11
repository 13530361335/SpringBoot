package com.demo.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface AsyncService {

    void register(HttpServletRequest request, HttpServletResponse response, String key);

    void pushMessage(String key, String message);

}
