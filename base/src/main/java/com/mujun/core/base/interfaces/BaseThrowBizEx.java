package com.mujun.core.base.interfaces;


import com.mujun.core.base.enums.ResCode;
import com.mujun.core.base.exception.BizException;

public interface BaseThrowBizEx {
    default void throwBizEx(String msgStr) {
        throw bizEx(msgStr);
    }

    default BizException bizEx(String msgStr) {
        return new BizException(msgStr);
    }

    default void throwBizEx(ResCode resCode) {
        throw bizEx(resCode);
    }

    default BizException bizEx(ResCode resCode) {
        return new BizException(resCode);
    }
}