package com.mall.util;

import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class FtpUtil {

    private Logger logger = LoggerFactory.getLogger(FtpUtil.class);

    private static String ftpIp = PropertiesUtil.getProperty("ftp.server.ip");
    private static String ftpUser = PropertiesUtil.getProperty("ftp.user");
    private static String ftpPassword = PropertiesUtil.getProperty("ftp.pass");
    private static int ftpPort = Integer.parseInt(PropertiesUtil.getProperty("ftp.port"));

    private String ip;
    private String user;
    private String password;
    private int port;
    private FTPClient ftpClient;

    public FtpUtil() {
        this.ip = ftpIp;
        this.user = ftpUser;
        this.password = ftpPassword;
        this.port = ftpPort;
    }

    public FtpUtil(String ip, String user, String password, int port) {
        this.ip = ip;
        this.user = user;
        this.password = password;
        this.port = port;
    }

    public boolean updateFile(List<File> fileList) throws IOException {
        return updateFile("images", fileList);
    }

    public boolean updateFile(InputStream is, String filename) throws IOException {
        return updateFile("images", is, filename);
    }

    public boolean updateFile(String remotePath, InputStream is, String filename) throws IOException {
        boolean isSuccess = false;
        if (connetServer()) {
            try {
                this.initFtpClient(remotePath);
                ftpClient.storeFile(filename, is);
                isSuccess = true;
            } catch (IOException e) {
                logger.error("ftp上传文件失败", e);
            } finally {
                is.close();
                ftpClient.disconnect();
            }
        }
        return isSuccess;
    }


    public boolean updateFile(String remotePath, List<File> fileList) throws IOException {
        boolean isSuccess = false;
        FileInputStream fis = null;
        if (connetServer()) {
            try {
                this.initFtpClient(remotePath);
                for (File file : fileList) {
                    fis = new FileInputStream(file);
                    ftpClient.storeFile(file.getName(), fis);
                }
                isSuccess = true;
            } catch (IOException e) {
                logger.error("ftp上传文件失败", e);
            } finally {
                fis.close();
                ftpClient.disconnect();
            }
        }
        return isSuccess;
    }

    private boolean connetServer() {
        boolean isSuccess = false;
        ftpClient = new FTPClient();
        try {
            ftpClient.connect(ip, port);
            isSuccess = ftpClient.login(user, password);
        } catch (IOException e) {
            logger.error("连接ftp服务器失败", e);
        }
        return isSuccess;
    }

    private void initFtpClient(String remotePath) throws IOException {
        ftpClient.changeWorkingDirectory(remotePath);
        ftpClient.setBufferSize(1024);
        ftpClient.setControlEncoding("UTF-8");
        ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
        ftpClient.enterLocalPassiveMode();
    }


    public static void main(String[] args) {
        FtpUtil ftpUtil = new FtpUtil();
        boolean b = ftpUtil.connetServer();
        System.out.println(b);
    }
}
