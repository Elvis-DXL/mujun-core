package com.mujun.core.oss.impl;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.mujun.core.base.enums.DatePattern;
import com.mujun.core.base.interfaces.BaseThrowBizEx;
import com.mujun.core.base.tool.DSTool;
import com.mujun.core.base.tool.EmptyTool;
import com.mujun.core.base.tool.TimeTool;
import com.mujun.core.oss.OssFile;
import com.mujun.core.oss.OssService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.time.LocalDate;

public class YdServiceImpl implements OssService, BaseThrowBizEx {
    private final Logger log = LoggerFactory.getLogger(YdServiceImpl.class);

    private AmazonS3Client amazonS3Client;
    private final String endpoint;

    private YdServiceImpl(String endpointUrl, String accessKey, String secretKey) {
        System.out.println("=============使用Yd存储文件=============");
        try {
            ClientConfiguration opts = new ClientConfiguration();
            opts.setSignerOverride("S3SignerType");
            opts.setConnectionTimeout(15000);
            this.amazonS3Client = new AmazonS3Client(new BasicAWSCredentials(accessKey, secretKey), opts);
            this.amazonS3Client.setEndpoint(endpointUrl);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throwBizEx("amazonS3Client instantiation error");
        }
        this.endpoint = endpointUrl;
    }

    public static YdServiceImpl newInstance(String endpointUrl, String accessKey, String secretKey) {
        DSTool.trueThrow(EmptyTool.isEmpty(endpointUrl), new IllegalArgumentException("endpointUrl must not be empty"));
        DSTool.trueThrow(EmptyTool.isEmpty(accessKey), new IllegalArgumentException("accessKey must not be empty"));
        DSTool.trueThrow(EmptyTool.isEmpty(secretKey), new IllegalArgumentException("secretKey must not be empty"));
        return new YdServiceImpl(endpointUrl, accessKey, secretKey);
    }

    private boolean bucketNotExist(String bucketName) {
        try {
            return !this.amazonS3Client.doesBucketExist(bucketName);
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
            PutObjectRequest request = new PutObjectRequest(bucketName, fileName, iStream, (ObjectMetadata) null);
            request.setCannedAcl(CannedAccessControlList.PublicRead);
            this.amazonS3Client.putObject(request);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throwBizEx(fileName + " putFile error");
        }
        return ossFile;
    }

    @Override
    public InputStream getFile(String bucketName, String fileName) {
        DSTool.trueThrow(this.bucketNotExist(bucketName), bizEx("bucket " + bucketName + " is not exist"));
        S3Object object = amazonS3Client.getObject(bucketName, fileName);
        if (null == object) {
            throw bizEx(fileName + " getFile error");
        }
        return object.getObjectContent();
    }

    @Override
    public void deleteFile(String bucketName, String fileName) {
        DSTool.trueThrow(this.bucketNotExist(bucketName), bizEx("bucket " + bucketName + " is not exist"));
        try {
            this.amazonS3Client.deleteObject(bucketName, fileName);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw bizEx(fileName + " deleteFile error");
        }
    }
}