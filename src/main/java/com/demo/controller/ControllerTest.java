package com.demo.controller;

import com.demo.util.ftp.FtpTemplate;
import com.demo.com.WebSocketServer;
import com.demo.dao.FileInfoMapper;
import com.demo.entity.FileInfo;
import com.demo.util.ImageUtil;
import com.demo.util.ShellUtil;
import com.demo.com.Result;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mongodb.BasicDBObject;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Api(description = "快速测试")
@RequestMapping("test")
@RestController
public class ControllerTest {

    private final static Logger logger = LoggerFactory.getLogger(ControllerTest.class);

    @Value("${spring.mail.username}")
    private String from;

    @Autowired
    private FileInfoMapper fileInfoMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private ShellUtil shellUtil;

    @Autowired
    FtpTemplate ftpTemplate;

    @RequestMapping(value = "sql", method = RequestMethod.GET)
    public Result sql() {
        PageHelper.startPage(1, 2);
        List<FileInfo> fileInfos = fileInfoMapper.selectAll();
        PageInfo<FileInfo> pageInfo = new PageInfo<>(fileInfos);
        return new Result(pageInfo);
    }

    @PostMapping("/redisSaveImg")
    public void redisSaveImg(MultipartFile image) throws IOException {
        redisTemplate.opsForHash().put("image", "1", ImageUtil.parseImg(image.getInputStream()));
    }

    @GetMapping("/redisGetImg")
    public void redisGetImg(HttpServletResponse response) throws IOException {
        String imgStr = (String) redisTemplate.opsForHash().get("image", "1");
        ImageUtil.generateImg(imgStr, response.getOutputStream());
    }

    @GetMapping("/mongodb")
    public Result getData(String id) {
        Query query = new Query();
        Criteria criteria = Criteria.where("id").is(id);
        query.addCriteria(criteria);
        List<BasicDBObject> basicDBObjects = mongoTemplate.find(query, BasicDBObject.class, "use_info");
        return new Result<>(basicDBObjects);
    }

    @PostMapping(value = "upload")
    public String upload(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        String dir = "/2018-12-31";
        ftpTemplate.upload(dir, fileName, file.getInputStream());
        return "success";
    }

    @PostMapping("/sendEmail")
    public void sendSimpleMail(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        mailSender.send(message);
    }

    @RequestMapping(value = "websocket", method = RequestMethod.GET)
    public String websocket(String message) {
        WebSocketServer.sendInfo(message);
        return "success";
    }

    @GetMapping("/testProcess")
    public void testProcess(String... command) {
        shellUtil.execute(command);
    }

    @PostMapping("/testBody")
    public Result testProcess(@RequestBody FileInfo fileInfo) {
        return new Result();
    }

}
