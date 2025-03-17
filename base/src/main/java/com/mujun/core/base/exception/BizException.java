package com.mujun.core.base.exception;


import com.mujun.core.base.enums.ResCode;

public class BizException extends RuntimeException {
    private final ResCode resCode;

    public BizException(String message) {
        super(message);
        this.resCode = ResCode.FAILURE;
    }

    public BizException(ResCode resCode) {
        super(resCode.getMessage());
        this.resCode = resCode;
    }

    public BizException(String msg, Throwable cause) {
        super(msg, cause);
        this.resCode = ResCode.FAILURE;
    }

    public BizException(ResCode resCode, Throwable cause) {
        super(cause);
        this.resCode = resCode;
    }

    public ResCode getResCode() {
        return this.resCode;
    }
}