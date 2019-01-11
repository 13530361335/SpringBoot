package com.demo.controller;

import com.demo.service.FileService;
import com.demo.web.Result;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Api(description = "文件服务")
@RequestMapping("file")
@RestController
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping(value = "upload")
    public Result<Integer> upload(MultipartFile file) throws IOException {
        return fileService.upload(file);
    }
}
