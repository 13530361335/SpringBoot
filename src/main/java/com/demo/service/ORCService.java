package com.demo.service;

import com.demo.com.Result;
import org.springframework.web.multipart.MultipartFile;

public interface ORCService {

    /**
     * 识别图片
     * @param file
     * @return
     */
    Result simpleAnalysis(MultipartFile file);

}
