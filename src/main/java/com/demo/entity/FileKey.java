package com.demo.entity;

public class FileKey {
    private Integer id;

    private String md5;

    private Integer size;

    private String dir;

    public FileKey() {
    }

    public FileKey(String md5, Integer size, String dir) {
        this.md5 = md5;
        this.size = size;
        this.dir = dir;
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

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }
}