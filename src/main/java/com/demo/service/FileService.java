package com.demo.service;

import com.demo.web.Result;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {

    Result<Integer> upload(MultipartFile file) throws IOException;

    void download(int id);

    void delete(int id);

}
