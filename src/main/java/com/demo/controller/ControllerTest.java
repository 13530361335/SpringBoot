package com.demo.controller;

import com.demo.dao.UserInfoMapper;
import com.demo.entity.UserInfo;
import com.demo.util.FtpUtil;
import com.demo.web.Result;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@Api(description = "快速测试")
@RequestMapping("test")
@RestController
public class ControllerTest {

    @Value("${server.port}")
    int port;

    @Autowired
    private FtpUtil ftpUtil;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @RequestMapping(value = "port",method = RequestMethod.GET)
    public String port() {
        return "你访问的rest的端口是:" + port;
    }

    @PostMapping(value = "upload")
    public String upload(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        String dir = "/2018-12-31";
        ftpUtil.upload(dir,fileName,file.getInputStream());
        return "success";
    }

    @RequestMapping(value = "sql",method = RequestMethod.GET)
    public Result sql() {
        UserInfo userInfo = userInfoMapper.selectByPrimaryKey(1);
        return new Result(userInfo);
    }

}
