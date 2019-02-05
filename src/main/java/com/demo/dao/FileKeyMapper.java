package com.demo.dao;

import com.demo.entity.FileKey;
import org.apache.ibatis.annotations.Param;

public interface FileKeyMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FileKey record);

    int insertSelective(FileKey record);

    FileKey selectByPrimaryKey(Integer id);

    FileKey selectByMD5AndSize(@Param("md5") String md5, @Param("size") int size);

    int updateByPrimaryKeySelective(FileKey record);

    int updateByPrimaryKey(FileKey record);
}