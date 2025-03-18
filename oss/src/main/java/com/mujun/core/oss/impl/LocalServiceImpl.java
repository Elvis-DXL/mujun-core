package com.mujun.core.oss.impl;


import com.mujun.core.base.interfaces.BaseThrowBizEx;
import com.mujun.core.base.tool.DSTool;
import com.mujun.core.base.tool.EmptyTool;
import com.mujun.core.base.tool.IOTool;
import com.mujun.core.oss.OssFile;
import com.mujun.core.oss.OssService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class LocalServiceImpl implements OssService, BaseThrowBizEx {
    private final Logger log = LoggerFactory.getLogger(LocalServiceImpl.class);
    private final String pathUrl;

    private LocalServiceImpl(String pathUrl) {
        System.out.println("=============使用Local存储文件=============");
        if (!pathUrl.endsWith("/")) {
            pathUrl = pathUrl.concat("/");
        }
        this.pathUrl = pathUrl;
    }

    public static LocalServiceImpl newInstance(String pathUrl) {
        DSTool.trueThrow(EmptyTool.isEmpty(pathUrl), new IllegalArgumentException("pathUrl must not be empty"));
        return new LocalServiceImpl(pathUrl);
    }

    private String getFileName(String fileName) {
        return DSTool.UUID36().concat(".").concat(DSTool.fileExtName(fileName));
    }

    @Override
    public OssFile putFile(String bucketName, String fileName, InputStream iStream) {
        File file = new File(pathUrl.concat(bucketName));
        if (!file.exists()) {
            file.mkdirs();
        }
        String fn = this.getFileName(fileName);
        OssFile ossFile = new OssFile();
        ossFile.setFullName(fileName);
        ossFile.setFullUrl(this.pathUrl.concat(bucketName).concat("/").concat(fn));
        ossFile.setRelUrl(fn);
        try {
            FileOutputStream fos = new FileOutputStream(pathUrl.concat(bucketName).concat("/").concat(fn));
            IOTool.inToOut(iStream, fos);
        } catch (FileNotFoundException e) {
            log.error(e.getMessage(), e);
            throwBizEx(fileName + " putFile error");
        }
        return ossFile;
    }

    @Override
    public InputStream getFile(String bucketName, String fileName) {
        File file = new File(this.pathUrl.concat(bucketName).concat("/").concat(fileName));
        if (!file.exists()) {
            throwBizEx(fileName + " is not exists");
        }
        InputStream iStream = null;
        try {
            iStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            log.error(e.getMessage(), e);
            throw bizEx(fileName + " getFile error");
        }
        return iStream;
    }

    @Override
    public void deleteFile(String bucketName, String fileName) {
        File file = new File(this.pathUrl.concat(bucketName).concat("/").concat(fileName));
        if (!file.exists()) {
            throwBizEx(fileName + " is not exists");
        }
        file.delete();
    }
}