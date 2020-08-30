package com.jk.entity;

import lombok.Data;

@Data
public class OrderEntity {

    private Integer id;
    private String orderNumber;
    private String orderTime;
    private Integer userId;

}
