package com.demo.controller;

import com.demo.service.ORCService;
import com.demo.web.Result;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Api(description = "图片识别")
@RequestMapping("OCR")
@RestController
public class OCRController {

    @Autowired
    private ORCService orcService;

    @PostMapping("simpleAnalysis")
    public Result simpleAnalysis(MultipartFile file){
        return orcService.simpleAnalysis(file);
    }
}
