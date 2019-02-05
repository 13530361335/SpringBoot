package com.demo.dao;

import com.demo.entity.FileInfo;

import java.util.List;

public interface FileInfoMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(FileInfo record);

    int insertSelective(FileInfo record);

    FileInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FileInfo record);

    int updateByPrimaryKey(FileInfo record);

    List<FileInfo> selectAll();
}