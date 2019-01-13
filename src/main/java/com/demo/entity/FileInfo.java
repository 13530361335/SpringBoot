package com.demo.entity;

import java.util.Date;

public class FileInfo {
    private Integer id;

    private String md5;

    private String fileName;

    private Long size;

    private String type;

    private Date uploadTime;

    public FileInfo(){
    }

    public FileInfo(String md5, String fileName, Long size, String type, Date uploadTime) {
        this.md5 = md5;
        this.fileName = fileName;
        this.size = size;
        this.type = type;
        this.uploadTime = uploadTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }
}