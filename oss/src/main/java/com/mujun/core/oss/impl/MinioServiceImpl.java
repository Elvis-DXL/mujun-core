package com.mujun.core.oss.impl;

import com.mujun.core.base.enums.DatePattern;
import com.mujun.core.base.interfaces.BaseThrowBizEx;
import com.mujun.core.base.tool.DSTool;
import com.mujun.core.base.tool.EmptyTool;
import com.mujun.core.base.tool.TimeTool;
import com.mujun.core.oss.OssFile;
import com.mujun.core.oss.OssService;
import io.minio.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.time.LocalDate;

public class MinioServiceImpl implements OssService, BaseThrowBizEx {
    private final Logger log = LoggerFactory.getLogger(MinioServiceImpl.class);

    private MinioClient minioClient;
    private final String endpoint;

    private MinioServiceImpl(String endpointUrl, String accessKey, String secretKey) {
        System.out.println("=============使用Minio存储文件=============");
        try {
            this.minioClient = MinioClient.builder()
                    .endpoint(endpointUrl).credentials(accessKey, secretKey).build();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throwBizEx("minioClient instantiation error");
        }
        this.endpoint = endpointUrl;
    }

    public static MinioServiceImpl newInstance(String endpointUrl, String accessKey, String secretKey) {
        DSTool.trueThrow(EmptyTool.isEmpty(endpointUrl), new IllegalArgumentException("endpointUrl must not be empty"));
        DSTool.trueThrow(EmptyTool.isEmpty(accessKey), new IllegalArgumentException("accessKey must not be empty"));
        DSTool.trueThrow(EmptyTool.isEmpty(secretKey), new IllegalArgumentException("secretKey must not be empty"));
        return new MinioServiceImpl(endpointUrl, accessKey, secretKey);
    }

    private boolean bucketNotExist(String bucketName) {
        try {
            return !this.minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw bizEx("bucket " + bucketName + " check exist error");
        }
    }

    private String getFileName(String fileName) {
        return TimeTool.formatLD(LocalDate.now(), DatePattern.yyyyMMdd)
                .concat("/")
                .concat(DSTool.UUID36())
                .concat(".")
                .concat(DSTool.fileExtName(fileName));
    }

    @Override
    public OssFile putFile(String bucketName, String fileName, InputStream iStream) {
        DSTool.trueThrow(this.bucketNotExist(bucketName), bizEx("bucket " + bucketName + " is not exist"));
        String fn = this.getFileName(fileName);
        OssFile ossFile = new OssFile();
        ossFile.setFullName(fileName);
        ossFile.setFullUrl(this.endpoint.concat("/").concat(bucketName).concat("/").concat(fn));
        ossFile.setRelUrl(fn);
        try {
            this.minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName).object(fn)
                    .stream(iStream, iStream.available(), -1L)
                    .build());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throwBizEx(fileName + " putFile error");
        }
        return ossFile;
    }

    @Override
    public InputStream getFile(String bucketName, String fileName) {
        DSTool.trueThrow(this.bucketNotExist(bucketName), bizEx("bucket " + bucketName + " is not exist"));
        try {
            return this.minioClient.getObject(GetObjectArgs.builder()
                    .bucket(bucketName).object(fileName)
                    .build());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw bizEx(fileName + " getFile error");
        }
    }

    @Override
    public void deleteFile(String bucketName, String fileName) {
        DSTool.trueThrow(this.bucketNotExist(bucketName), bizEx("bucket " + bucketName + " is not exist"));
        try {
            this.minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .build());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw bizEx(fileName + " deleteFile error");
        }
    }
}