package com.demo.service.impl;

import com.demo.service.ORCService;
import com.demo.util.ORCUtil;
import com.demo.com.Result;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class ORCServiceImpl implements ORCService {


    @Override
    public Result simpleAnalysis(MultipartFile file) {
        String data;
        long start = System.currentTimeMillis();
        try {
            data = ORCUtil.simpleAnalysis(file.getInputStream());
        } catch (IOException e) {
            return new Result("Image Error");
        } catch (TesseractException e) {
            return new Result("ORC Error");
        }
        long end = System.currentTimeMillis();
        long time = end - start;
        Map map = new HashMap();
        map.put("time",time);
        map.put("text",data);
        return new Result(map);
    }

}
