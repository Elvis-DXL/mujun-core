package com.mujun.core.base.form;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PageReq implements Serializable {
    protected Integer current;
    protected Integer size;
    protected List<OrderItem> orders;
}