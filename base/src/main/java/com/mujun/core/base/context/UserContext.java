package com.mujun.core.base.context;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
@Accessors(chain = true)
public class UserContext implements Serializable {
    private String userId;
    private String userName;
    private String token;
    private Boolean superAdmin;
    private List<String> roleCodes;
    private List<String> permissions;
    private Map<String, Object> extMap;
}