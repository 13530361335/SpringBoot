package com.demo.controller;

import com.demo.dao.FileInfoMapper;
import com.demo.entity.FileInfo;
import com.demo.service.FileService;

import com.demo.web.Result;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Api(description = "文件服务")
@RequestMapping("file")
@Controller
public class FileController {

    @Autowired
    private FileService fileService;

    @Autowired
    private FileInfoMapper fileInfoMapper;

    @PostMapping(value = "upload")
    @ResponseBody
    public Result<Integer> upload(MultipartFile file) throws IOException {
        return fileService.upload(file);
    }

    @GetMapping(value = "download/{id}")
    @ResponseBody
    public void download(HttpServletRequest request, HttpServletResponse response, @PathVariable int id) throws IOException {
        fileService.download(request,response,id);
    }

    @GetMapping(value = "delete/{id}")
    @ResponseBody
    public Result download(@PathVariable int id){
        return fileService.delete(id);
    }

    @GetMapping("list")
    public String list(Model model, Integer pageNum) {
        if(StringUtils.isEmpty(pageNum)){
            pageNum = 1;
        }
        PageHelper.startPage(pageNum,10);
        List<FileInfo> fileInfos = fileInfoMapper.selectAll();
        PageInfo<FileInfo> pageInfo = new PageInfo(fileInfos);
        model.addAttribute("pageInfo",pageInfo);
        return "home";
    }
}
