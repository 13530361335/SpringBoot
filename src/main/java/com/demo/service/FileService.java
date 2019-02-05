package com.demo.service;

import com.demo.web.Result;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface FileService {

    Result<Integer> upload(MultipartFile file) throws IOException;

    void download(HttpServletRequest request, HttpServletResponse response,int id) throws IOException;

    Result<Integer> delete(int id);

}
