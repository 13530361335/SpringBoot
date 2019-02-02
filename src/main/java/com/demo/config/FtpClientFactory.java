package com.demo.config;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class FtpClientFactory extends BasePooledObjectFactory<FTPClient> {

    private static Logger log = LoggerFactory.getLogger(FtpClientFactory.class);

    @Value("${ftp.host}")
    private String host;

    @Value("${ftp.port}")
    private int port;

    @Value("${ftp.username}")
    private String username;

    @Value("${ftp.password}")
    private String password;

    private int connectTimeOut = 5000;//ftp 连接超时时间 毫秒

    private String controlEncoding = "utf-8";

    private int bufferSize = 1024 * 1024;//缓冲区大小

    private int dataTimeout = 60000;

    private boolean passiveMode = true;//是否启用被动模式

    /**
     * 新建对象
     */
    @Override
    public FTPClient create() {
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.setConnectTimeout(connectTimeOut);
            ftpClient.connect(host, port);
            ftpClient.login(username, password);
            ftpClient.setControlEncoding(controlEncoding);
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftpClient.setBufferSize(bufferSize);
            ftpClient.setDataTimeout(dataTimeout);
            if (passiveMode) {
                ftpClient.enterLocalPassiveMode();
            }
            return ftpClient;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    @Override
    public PooledObject<FTPClient> wrap(FTPClient ftpClient) {
        return new DefaultPooledObject<>(ftpClient);
    }

    /**
     * 销毁对象
     */
    @Override
    public void destroyObject(PooledObject<FTPClient> p) throws Exception {
        FTPClient ftpClient = p.getObject();
        ftpClient.logout();
        super.destroyObject(p);
    }

    /**
     * 验证对象
     */
    @Override
    public boolean validateObject(PooledObject<FTPClient> p) {
        FTPClient ftpClient = p.getObject();
        boolean connect = false;
        try {
            connect = ftpClient.sendNoOp();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return connect;
    }

}


