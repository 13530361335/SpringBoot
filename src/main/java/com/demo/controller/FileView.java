package com.demo.controller;

import com.demo.dao.FileInfoMapper;
import com.demo.entity.FileInfo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("file")
@Controller
public class FileView {

    @Autowired
    private FileInfoMapper fileInfoMapper;

    @GetMapping("list")
    public String list(Model model) {
        PageHelper.startPage(1, 2);
        List<FileInfo> fileInfos = fileInfoMapper.selectAll();
        PageInfo<FileInfo> pageInfo = new PageInfo<>(fileInfos);
        model.addAttribute("pageInfo",pageInfo);
        return "home";
    }

}
