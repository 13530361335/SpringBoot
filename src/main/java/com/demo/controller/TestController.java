package com.demo.controller;

import com.demo.dao.UserInfoMapper;
import com.demo.entity.UserInfo;
import com.demo.util.FastIOUtil;
import com.demo.util.ftp.FtpTemplate;
import com.demo.service.WebSocket;
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
import java.util.Set;

@Api(description = "快速测试")
@RequestMapping("test")
@RestController
public class TestController {

    private final static Logger logger = LoggerFactory.getLogger(TestController.class);

    @Value("${spring.mail.username}")
    private String from;

    @Autowired
    private UserInfoMapper userInfoMapper;

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
        List<UserInfo> userInfos = userInfoMapper.selectAll();
        PageInfo<UserInfo> pageInfo = new PageInfo(userInfos);
        return new Result(pageInfo);
    }

    @PostMapping("/redisSaveImg")
    public void redisSaveImg(MultipartFile image) throws IOException {
        redisTemplate.opsForHash().put("image", "1", FastIOUtil.parseImg(image.getInputStream()));
    }

    @GetMapping("/redisGetImg")
    public void redisGetImg(HttpServletResponse response) throws IOException {
        String imgStr = (String) redisTemplate.opsForHash().get("image", "1");
        FastIOUtil.generateImg(imgStr, response.getOutputStream());
    }

    @GetMapping("/mongodb")
    public Result getData(String id) {
        Query query = new Query();
        Criteria criteria = Criteria.where("id").is(id);
        query.addCriteria(criteria);
        List<BasicDBObject> basicDBObjects = mongoTemplate.find(query, BasicDBObject.class, "use_info");
        return new Result(basicDBObjects);
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
        return "success";
    }

    @GetMapping("/testProcess")
    public void testProcess(String... command) {
        shellUtil.execute(command);
    }

    @GetMapping("/getOnline")
    public Result getOnline() {
        Set<String> users = WebSocket.getClients().keySet();
        return new Result(users);
    }



}
