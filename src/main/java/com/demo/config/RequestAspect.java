package com.demo.config;

import com.alibaba.fastjson.JSONObject;
import com.mongodb.BasicDBObject;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;


@Aspect
@Configuration
public class RequestAspect {

    private Logger log = LoggerFactory.getLogger(RequestAspect.class);

    private Map<String, String> json = new HashMap<>();

    @Autowired
    private MongoTemplate mongoTemplate;

    //申明一个切点 里面是 execution表达式
    @Pointcut("execution(public * com.demo.controller..*.*(..))")
    private void controllerAspect() {
    }

    //请求method前打印内容
    @Before(value = "controllerAspect()")
    public void before(JoinPoint joinPoint) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        BasicDBObject logInfo = getBasicDBObject(request, joinPoint);
        log.info("请求数据:" + JSONObject.toJSONString(logInfo));
        mongoTemplate.insert(logInfo, "request");
    }

    //在方法执行完结后打印返回内容
    @AfterReturning(returning = "result", pointcut = "controllerAspect()")
    public void after(Object result) {
        log.info("返回数据:" + JSONObject.toJSONString(result));
    }

    /**
     * 获取请求的详细参数
     *
     * @param request
     * @param joinPoint
     * @return
     */
    private BasicDBObject getBasicDBObject(HttpServletRequest request, JoinPoint joinPoint) {
        BasicDBObject r = new BasicDBObject();
        // 快速复现
        r.append("requestURL", request.getRequestURL().toString());
        r.append("requestMethod", request.getMethod());
        r.append("queryString", request.getQueryString());
        r.append("parameters", joinPoint.getArgs());

        // 权限控制
        r.append("requestURI", request.getRequestURI());
        r.append("clientAddr", request.getRemoteAddr());
        r.append("clientHost", request.getRemoteHost());
        r.append("clientPort", request.getRemotePort());
        r.append("serverAddr", request.getLocalAddr());
        r.append("serverName", request.getLocalName());
        r.append("headers", getHeaders(request));

        // 快速定位
        r.append("method", joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        r.append("args", Arrays.toString(joinPoint.getArgs()));
        return r;
    }

    /**
     * 获取头信息
     *
     * @param request
     * @return
     */
    private Map<String, String> getHeaders(HttpServletRequest request) {
        Map<String, String> map = new HashMap<>();
        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }
        return map;
    }


}