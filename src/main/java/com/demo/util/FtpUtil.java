package com.demo.util;

import com.demo.config.FtpClientFactory;
import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;

@Component
public class FtpUtil {

    private static Logger log = LoggerFactory.getLogger(FtpUtil.class);

    @Autowired
    FtpClientFactory ftpClientFactory;

    public  boolean upload(String dir,String fileName, InputStream in){
        FTPClient ftpClient = ftpClientFactory.create();
        try {
            ftpClient.makeDirectory(dir);
            ftpClient.changeWorkingDirectory(dir);
            ftpClient.storeFile(fileName,in);
            in.close();
        } catch (SocketException e) {
            log.error(e.getMessage(),e);
            return false;
        } catch (IOException e) {
            log.error(e.getMessage(),e);
            return false;
        } finally {
            try {
                ftpClient.logout();
            } catch (IOException e) {
                log.error(e.getMessage(),e);
            }
        }
        return true;
    }


}
