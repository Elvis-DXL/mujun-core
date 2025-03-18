package com.mujun.core.oss;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(name = "OssFile", description = "对象存储文件上传返回值")
public class OssFile implements Serializable {
    @Schema(name = "relUrl", description = "文件相对地址")
    private String relUrl;

    @Schema(name = "fullUrl", description = "文件全地址")
    private String fullUrl;

    @Schema(name = "fullName", description = "文件全名称")
    private String fullName;
}