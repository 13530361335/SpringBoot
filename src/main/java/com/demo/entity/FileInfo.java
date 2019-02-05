package com.demo.entity;

import java.util.Date;

public class FileInfo {
    private Integer id;

    private Integer keyId;

    private String fileName;

    private Date uploadTime;

    private String type;

    private Long size;

    public FileInfo() {
    }

    public FileInfo(Integer keyId, String fileName, Date uploadTime, String type, Long size) {
        this.keyId = keyId;
        this.fileName = fileName;
        this.uploadTime = uploadTime;
        this.type = type;
        this.size = size;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getKeyId() {
        return keyId;
    }

    public void setKeyId(Integer keyId) {
        this.keyId = keyId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Date getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }
}