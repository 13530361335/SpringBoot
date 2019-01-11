package com.demo.service.impl;


import com.demo.dao.FileInfoMapper;
import com.demo.entity.FileInfo;
import com.demo.service.FileService;
import com.demo.util.FtpUtil;
import com.demo.web.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private FtpUtil ftpUtil;

    @Autowired
    private FileInfoMapper fileInfoMapper;


    @Override
    public Result<Integer> upload(MultipartFile file) throws IOException {
        String md5 = DigestUtils.md5DigestAsHex(file.getInputStream());
        long size =  file.getSize();
        String fileName = file.getOriginalFilename();
        String type = fileName.substring(fileName.lastIndexOf("."),fileName.length());
        List<FileInfo> fileInfos = fileInfoMapper.selectByMD5(md5);
        boolean flag = false;
        FileInfo fileInfo = new FileInfo(md5,fileName,size,type,new Date());
        if(fileInfos.size() > 0){
            for (FileInfo info :fileInfos) {
               if(info.getSize().equals(size) && info.getMd5().equals(md5)){
                   fileInfo.setUploadTime(info.getUploadTime());
                   flag = true;
                   break;
               }
            }
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dir = sdf.format(fileInfo.getUploadTime());
        if(!flag){
            ftpUtil.upload(dir,md5,file.getInputStream());
        }
        return new Result<>(fileInfoMapper.insertSelective(fileInfo));
    }

    @Override
    public void download(int id) {

    }

    @Override
    public void delete(int id) {

    }

}
