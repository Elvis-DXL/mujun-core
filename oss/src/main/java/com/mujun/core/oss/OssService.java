package com.mujun.core.oss;

import java.io.InputStream;

public interface OssService {
    OssFile putFile(String bucketName, String fileName, InputStream iStream);

    InputStream getFile(String bucketName, String fileName);

    void deleteFile(String bucketName, String fileName);
}