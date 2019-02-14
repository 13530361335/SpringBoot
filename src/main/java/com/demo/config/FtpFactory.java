package com.demo.config;

import com.demo.entity.FtpConfig;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class FtpFactory extends BasePooledObjectFactory<FTPClient> {

    private final static Logger log = LoggerFactory.getLogger(FtpFactory.class);

    @Value("${ftp.host}")
    private String host;

    @Value("${ftp.port}")
    private int port;

    @Value("${ftp.username}")
    private String username;

    @Value("${ftp.password}")
    private String password;

    @Value("${ftp.controlEncoding}")
    private String controlEncoding;

    @Value("${ftp.passiveMode}")
    private boolean passiveMode;

    private Integer connectTimeOut = 10 * 1000;

    private Integer dataTimeOut = 60 * 1000;

    private Integer bufferSize = 1024 * 1024;

    private Integer keepAliveTimeOut = 0;

    @Bean
    @ConfigurationProperties(prefix = "ftp")
    public FtpConfig getConfig(){
        return new FtpConfig();
    }

    /**
     * 新建FtpClient对象
     */
    @Override
    public FTPClient create() {
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.setControlEncoding(controlEncoding);
            ftpClient.setControlKeepAliveTimeout(keepAliveTimeOut);
            ftpClient.setConnectTimeout(connectTimeOut);
            ftpClient.setDataTimeout(dataTimeOut);
            ftpClient.connect(host, port);
            ftpClient.login(username, password);
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftpClient.setBufferSize(bufferSize);
            if (passiveMode) {
                ftpClient.enterLocalPassiveMode();
            }
            return ftpClient;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * 将FtpClient对象放入连接池
     */
    @Override
    public PooledObject<FTPClient> wrap(FTPClient ftpClient) {
        return new DefaultPooledObject<>(ftpClient);
    }

    /**
     * 销毁FtpClient对象
     */
    @Override
    public void destroyObject(PooledObject<FTPClient> ftpPooled) {
        if (ftpPooled == null) {
            return;
        }
        FTPClient ftpClient = ftpPooled.getObject();
        try {
            ftpClient.logout();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.disconnect();
                }
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    /**
     * 验证FtpClient对象
     */
    @Override
    public boolean validateObject(PooledObject<FTPClient> ftpPooled) {
        try {
            FTPClient ftpClient = ftpPooled.getObject();
            return ftpClient.sendNoOp();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

}


