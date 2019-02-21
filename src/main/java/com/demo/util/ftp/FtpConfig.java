package com.demo.util.ftp;

import org.apache.commons.net.ftp.FTPClient;

public class FtpConfig {

    private String host;

    private int port = 21;

    private String username;

    private String password;

    private String controlEncoding = "utf-8";

    private boolean passiveMode = false;

    private int fileType = FTPClient.BINARY_FILE_TYPE;

    private int bufferSize = 1024 * 1024;

    private int connectTimeOut = 10 * 1000;

    private int dataTimeOut = 60 * 1000;

    private long keepAliveTimeOut = 0;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getControlEncoding() {
        return controlEncoding;
    }

    public void setControlEncoding(String controlEncoding) {
        this.controlEncoding = controlEncoding;
    }

    public boolean isPassiveMode() {
        return passiveMode;
    }

    public void setPassiveMode(boolean passiveMode) {
        this.passiveMode = passiveMode;
    }

    public int getFileType() {
        return fileType;
    }

    public void setFileType(int fileType) {
        this.fileType = fileType;
    }

    public int getBufferSize() {
        return bufferSize;
    }

    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }

    public int getConnectTimeOut() {
        return connectTimeOut;
    }

    public void setConnectTimeOut(int connectTimeOut) {
        this.connectTimeOut = connectTimeOut;
    }

    public int getDataTimeOut() {
        return dataTimeOut;
    }

    public void setDataTimeOut(int dataTimeOut) {
        this.dataTimeOut = dataTimeOut;
    }

    public long getKeepAliveTimeOut() {
        return keepAliveTimeOut;
    }

    public void setKeepAliveTimeOut(long keepAliveTimeOut) {
        this.keepAliveTimeOut = keepAliveTimeOut;
    }
}
