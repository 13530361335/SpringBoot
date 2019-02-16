package com.demo.com;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class FtpFactory extends BasePooledObjectFactory<FTPClient> {

    @Autowired
    private FtpConfig ftpConfig;

    private final static Logger log = LoggerFactory.getLogger(FtpFactory.class);

    private final static String FTP_PREFIX = "ftp";

    @Bean
    @Primary
    @ConfigurationProperties(prefix = FTP_PREFIX)
    public FtpConfig ftpConfig() {
        return new FtpConfig();
    }

    /**
     * 新建FtpClient对象
     */
    @Override
    public FTPClient create() {
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.setControlEncoding(ftpConfig.getControlEncoding());
            ftpClient.setControlKeepAliveTimeout(ftpConfig.getKeepAliveTimeOut());
            ftpClient.setConnectTimeout(ftpConfig.getConnectTimeOut());

            ftpClient.connect(ftpConfig.getHost(), ftpConfig.getPort());
            ftpClient.login(ftpConfig.getUsername(), ftpConfig.getPassword());
            ftpClient.setDataTimeout(ftpConfig.getDataTimeOut());
            ftpClient.setFileType(ftpConfig.getFileType());
            ftpClient.setBufferSize(ftpConfig.getBufferSize());
            if (ftpConfig.isPassiveMode()) {
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


