package com.demo.controller;

import com.demo.config.WebSocketServer;
import com.demo.dao.FileInfoMapper;
import com.demo.entity.FileInfo;
import com.demo.service.impl.TestServiceImpl;
import com.demo.util.EmailUtil;
import com.demo.util.FtpUtil;
import com.demo.util.Shell;
import com.demo.web.Result;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mongodb.BasicDBObject;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Api(description = "快速测试")
@RequestMapping("test")
@RestController
public class ControllerTest {

    @Value("${server.port}")
    int port;

    @Autowired
    private FtpUtil ftpUtil;

    @Autowired
    private FileInfoMapper fileInfoMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping(value = "port", method = RequestMethod.GET)
    public String port() {
        return "你访问的rest的端口是:" + port;
    }

    @PostMapping(value = "upload")
    public String upload(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        String dir = "/2018-12-31";
        ftpUtil.upload(dir, fileName, file.getInputStream());
        return "success";
    }

    @RequestMapping(value = "sql", method = RequestMethod.GET)
    public Result sql() {
        PageHelper.startPage(1,2);
        List<FileInfo> fileInfos =fileInfoMapper.selectByMD5("da117cfd0f948dd564194d9d6032ab3b");
        PageInfo<FileInfo> pageInfo = new PageInfo<>(fileInfos);
        return new Result(fileInfos);
    }

    @RequestMapping(value = "redis", method = RequestMethod.GET)
    public Result redis() {
        FileInfo fileInfo = (FileInfo) redisTemplate.opsForHash().get("FileInfo", "1");
        System.out.println(fileInfo.getUploadTime());
        return new Result(fileInfo);
    }

    @RequestMapping(value = "websocket", method = RequestMethod.GET)
    public String websocket(String message) {
        WebSocketServer.sendInfo(message);
        return "success";
    }

    @Autowired
    private TestServiceImpl testService;

    @RequestMapping(value = "async", method = RequestMethod.GET)
    public String async() {
        testService.sendSms();
        System.out.println("##################");
        return "success";
    }

    @Autowired
    private MongoTemplate mongoTemplate;

    @GetMapping("/mongodb")
    public Result getData(String id){
        Query query = new Query();
        Criteria criteria = Criteria.where("id").is(id);
        query.addCriteria(criteria);
        List<BasicDBObject> basicDBObjects =   mongoTemplate.find(query,BasicDBObject.class,"use_info");
        return new Result<>(basicDBObjects);
    }

    @Autowired
    private EmailUtil emailUtil;

    @PostMapping("/sendEmail")
    public Result sendEmail(String to,String subject,String content) throws Exception {
        emailUtil.send(to,subject,content);
        return new Result<>();
    }


    @Autowired
    private Shell shell;

    @GetMapping("/testProcess")
    public void testProcess(){
        shell.doSomeThing();
    }


}
