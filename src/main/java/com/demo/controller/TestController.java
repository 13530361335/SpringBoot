package com.demo.controller;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.demo.dao.UserInfoMapper;
import com.demo.entity.UserInfo;
import com.demo.util.HttpUtil;
import com.demo.util.ftp.FtpTemplate;
import com.demo.service.WebSocket;
import com.demo.util.ShellUtil;
import com.demo.common.Result;
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

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
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
        redisTemplate.opsForHash().put("image", "1", HttpUtil.parseImg(image.getInputStream()));
    }

    @GetMapping("/redisGetImg")
    public void redisGetImg(HttpServletResponse response) throws IOException {
        String imgStr = (String) redisTemplate.opsForHash().get("image", "1");
        HttpUtil.generateImg(imgStr, response.getOutputStream());
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

    @GetMapping(value = "down")
    public void down(HttpServletRequest request,HttpServletResponse response) throws IOException {
        HttpUtil.setDownHeader(request,response,"navicat121_premium_cs_x64.exe");
        OutputStream out = response.getOutputStream();
        InputStream in = new FileInputStream(new File(("D:\\Downloads\\" + "navicat121_premium_cs_x64.exe")));
        byte[] bytes = new byte[1024 * 8];
        int len;
        while ((len = in.read(bytes)) != -1) {
            out.write(bytes, 0, len);
        }
        out.close();
        in.close();
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


    @GetMapping("/a.htm")
    public void cooperation(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ServletOutputStream out = response.getOutputStream();
        ExcelWriter writer = new ExcelWriter(out, ExcelTypeEnum.XLSX, true);
        String fileName = new String(("UserInfo " + new SimpleDateFormat("yyyy-MM-dd").format(new Date()))
                .getBytes(), "UTF-8");
        Sheet sheet1 = new Sheet(1, 0);
        sheet1.setSheetName("第一个sheet");

        List<List<String>>  lists = new LinkedList();
        List<String>  l1 = new LinkedList();
        l1.add("jing");
        l1.add("123");
        List<String>  l2 = new LinkedList();
        l2.add("jing");
        l2.add("123");
        lists.add(l1);
        lists.add(l2);

        writer.write0(lists, sheet1);
        writer.finish();
        response.setContentType("multipart/form-data");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-disposition", "attachment;filename="+fileName+".xlsx");
        out.flush();
    }
}


