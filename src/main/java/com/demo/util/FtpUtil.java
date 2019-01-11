package com.demo.util;

import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;

@Component
public class FtpUtil {

    @Value("${ftp.host}")
    private String host;

    @Value("${ftp.port}")
    private int port;

    @Value("${ftp.username}")
    private String username;

    @Value("${ftp.password}")
    private String password;

    private Logger log = LoggerFactory.getLogger(FtpUtil.class);

    public  boolean upload(String dir,String fileName, InputStream in){
        FTPClient ftpClient = new FTPClient();
        System.out.println(ftpClient);
        try {
            ftpClient.connect(host, port);
            ftpClient.login(username, password);
            ftpClient.setControlEncoding("utf-8");
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftpClient.setBufferSize(1024 * 1024);
            ftpClient.enterLocalPassiveMode();
            ftpClient.makeDirectory(dir);
            ftpClient.changeWorkingDirectory(dir);
            ftpClient.storeFile(fileName,in);
            in.close();
        } catch (SocketException e) {
            log.info("FTP connect error");
            return false;
        } catch (IOException e) {
            log.info("FTP IO error");
            return false;
        } finally {
            try {
                ftpClient.logout();
            } catch (IOException e) {
                log.info("FTP logout error");
            }
        }
        return true;
    }
}
