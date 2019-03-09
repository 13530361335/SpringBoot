package com.demo.util;

import java.io.*;
import java.util.Enumeration;

import org.apache.commons.io.IOUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 文件操作工具类
 */
public class FileUtil {

    private final static Logger logger = LoggerFactory.getLogger(FileUtil.class);

    public static InputStream getResourcesFileInputStream(String fileName) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream("" + fileName);
    }

    /**
     * 解压缩ZIP文件，将ZIP文件里的内容解压到descFileName目录下
     *
     * @param zipFileName 需要解压的ZIP文件
     * @param targetPath  目标路径
     */
    public static boolean unZipFile(String zipFileName, String targetPath) {
        logger.info("unZipFile start");
        String targetDir = targetPath.endsWith(File.separator) ? targetPath : targetPath + File.separator;
        ZipFile zipFile = null;
        ZipEntry entry;
        String entryName, unFile;
        byte[] bytes = new byte[4096];
        int len;
        boolean flag;
        try {
            zipFile = new ZipFile(zipFileName);
            Enumeration<ZipEntry> enums = zipFile.getEntries();
            while (enums.hasMoreElements()) {
                entry = enums.nextElement();
                entryName = entry.getName();
                unFile = targetDir + entryName;
                boolean isDirectory = entry.isDirectory();
                File dir = isDirectory ? new File(unFile) : new File(unFile).getParentFile();
                if (!dir.exists()) {
                    flag = dir.mkdirs();
                    if (!flag) {
                        logger.warn("create descFileDir failed");
                    }
                }
                if (isDirectory) {
                    continue;
                }
                File file = new File(unFile);
                OutputStream os = null;
                InputStream is = null;
                try {
                    os = new FileOutputStream(file);
                    is = zipFile.getInputStream(entry);
                    while ((len = is.read(bytes)) != -1) {
                        os.write(bytes, 0, len);
                    }
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                    return false;
                } finally {
                    IOUtils.closeQuietly(os);
                    IOUtils.closeQuietly(is);
                }
            }
            logger.info("unZipFile success");
            return true;
        } catch (IOException e) {
            logger.error("unZipFile error", e);
            return false;
        } finally {
            try {
                if (zipFile != null) {
                    zipFile.close();
                }
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    /**
     * 删除文件或文件夹
     *
     * @param path 路径
     */
    public static void delete(String path) {
        File file = new File(path);
        if (!file.exists()) {
            logger.warn("file is not exists:{}", path);
        }
        if (file.isDirectory()) {// 如果是目录，先递归删除
            String[] list = file.list();
            if (list != null) {
                for (int i = 0; i < list.length; i++) {
                    delete(path + File.separator + list[i]);// 先删除目录下的文件
                }
            }
        }
        boolean flag = file.delete();
        if (!flag) {
            logger.error("delete file error:{}", path);
        }
    }


}