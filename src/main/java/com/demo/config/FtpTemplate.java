package com.demo.config;

import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.*;

@Component
public class FtpTemplate {

    private final static Logger log = LoggerFactory.getLogger(FtpFactory.class);

    private final GenericObjectPool<FTPClient> ftpClientPool;

    public FtpTemplate(FtpFactory ftpFactory) {
        ftpClientPool = new GenericObjectPool<>(ftpFactory);
    }

    /**
     * 上传文件
     *
     * @param remoteDir FTP的文件夹
     * @param fileName  FTP的文件名
     * @param in        上传的输入流
     * @return
     */
    public boolean upload(String remoteDir, String fileName, InputStream in) {
        FTPClient ftpClient = null;
        BufferedInputStream bufferedIn = null;
        try {
            ftpClient = ftpClientPool.borrowObject();
            bufferedIn = new BufferedInputStream(in);

            if (!ftpClient.changeWorkingDirectory(remoteDir)) {
                ftpClient.makeDirectory(remoteDir);
                ftpClient.changeWorkingDirectory(remoteDir);
            }
            final int retryTimes = 3;
            for (int j = 0; j <= retryTimes; j++) {
                if (ftpClient.storeFile(fileName, bufferedIn)) {
                    return true;
                }
                log.warn("upload file failure:" + j);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            IOUtils.closeQuietly(bufferedIn);
            ftpClientPool.returnObject(ftpClient);//将对象放回池中
        }
        return false;
    }

    /**
     * 下载文件
     *
     * @param remoteDir FTP的文件夹
     * @param fileName  FTP的文件名
     * @param out       下载的输出流
     * @return
     */
    public boolean download(String remoteDir, String fileName, OutputStream out) {
        FTPClient ftpClient = null;
        BufferedOutputStream bufferedOut = null;
        try {
            ftpClient = ftpClientPool.borrowObject();
            bufferedOut = new BufferedOutputStream(out);

            ftpClient.changeWorkingDirectory(remoteDir);
            FTPFile[] ftpFiles = ftpClient.listFiles();
            for (FTPFile file : ftpFiles) {
                if (file.isFile() && file.getName().equals(fileName)) {
                    ftpClient.retrieveFile(file.getName(), bufferedOut);
                    return true;
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            IOUtils.closeQuietly(bufferedOut);
            ftpClientPool.returnObject(ftpClient);
        }
        return false;
    }

    /**
     * 删除文件
     *
     * @param remoteDir FTP的文件夹
     * @param fileName  FTP的文件名
     * @return
     */
    public boolean delete(String remoteDir, String fileName) {
        FTPClient ftpClient = null;
        try {
            ftpClient = ftpClientPool.borrowObject();
            ftpClient.changeWorkingDirectory(remoteDir);
            ftpClient.dele(fileName);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        } finally {
            ftpClientPool.returnObject(ftpClient);
        }
    }

}
