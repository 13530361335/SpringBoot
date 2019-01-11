package com.demo.service.impl;

import com.demo.service.AsyncService;
import org.springframework.stereotype.Service;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class AsyncServiceImpl implements AsyncService {

    private static HashMap<String, ArrayList<AsyncContext>> links = new HashMap<>();

    @Override
    public void register(HttpServletRequest request, HttpServletResponse response, String key) {
         response.setContentType("text/event");
        ArrayList<AsyncContext> asyncContexts =  links.get(key);
        if(asyncContexts==null){
            asyncContexts = new ArrayList<>();
            links.put(key,asyncContexts);
        }
         AsyncContext ctx = request.startAsync();
         ctx.setTimeout(60*60*1000);
         asyncContexts.add(ctx);
    }

    @Override
    public void pushMessage(String key, String message) {
        List<AsyncContext> asyncContexts =  links.get(key);
        for (AsyncContext asyncContext: asyncContexts) {
            try {
                PrintWriter pw = asyncContext.getResponse().getWriter();
                pw.println(message);
                pw.flush();
            } catch (IOException e) {
                asyncContexts.remove(asyncContext);
            }
        }
    }
}
