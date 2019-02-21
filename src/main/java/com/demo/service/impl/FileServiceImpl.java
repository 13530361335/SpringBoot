package com.demo.service.impl;


import com.demo.util.ftp.FtpTemplate;
import com.demo.dao.FileInfoMapper;
import com.demo.dao.FileKeyMapper;
import com.demo.entity.FileInfo;
import com.demo.entity.FileKey;
import com.demo.service.FileService;
import com.demo.util.HttpUtil;
import com.demo.com.Result;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private FileKeyMapper fileKeyMapper;

    @Autowired
    private FileInfoMapper fileInfoMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private FtpTemplate ftpTemplate;

    @Override
    public Result<Integer> upload(MultipartFile file) throws IOException {
        String md5 = DigestUtils.md5DigestAsHex(file.getInputStream());
        long size = file.getSize();
        String fileName = file.getOriginalFilename();
        String type = fileName.substring(fileName.lastIndexOf("."));
        String dir = "/" + new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        FileKey fileKey = fileKeyMapper.selectByMD5AndSize(md5, (int) size);

        if (null == fileKey) {  //需上传
            ftpTemplate.upload(dir, md5, file.getInputStream());
            fileKeyMapper.insert(new FileKey(md5, (int) size, dir));
        }
        int keyId = fileKeyMapper.selectByMD5AndSize(md5, (int) size).getId();
        return new Result<>(fileInfoMapper.insertSelective(new FileInfo(keyId, fileName, new Date(), type, size)));
    }

    @Override
    public void download(HttpServletRequest request, HttpServletResponse response, int id) throws IOException {
        FileInfo fileInfo = fileInfoMapper.selectByPrimaryKey(id);
        FileKey fileKey = fileKeyMapper.selectByPrimaryKey(fileInfo.getKeyId());
        HttpUtil.setDownHeader(request,response,fileInfo.getFileName());
        ftpTemplate.download(fileKey.getDir(), fileKey.getMd5(), response.getOutputStream());
    }

    @Override
    public Result<Integer> delete(int id) {
        return new Result(fileInfoMapper.deleteByPrimaryKey(id));
    }

}
