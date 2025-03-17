package com.mujun.core.base.form;

import lombok.Data;

import java.io.Serializable;
import java.util.Collection;

@Data
public class PageResp<T> implements Serializable {
    private int pageSize;
    private int pageIndex;
    private int totalPage;
    private long total;
    private Collection<T> list;
}