package com.demo.controller;

import com.demo.service.FileService;
import com.demo.util.HttpUtil;
import com.demo.web.Result;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    @GetMapping(value = "download/{id}")
    public void download(HttpServletRequest request, HttpServletResponse response, @PathVariable int id) throws IOException {
        fileService.download(request,response,id);
    }

    @GetMapping(value = "delete/{id}")
    public Result download(@PathVariable int id){
        return fileService.delete(id);
    }
}
