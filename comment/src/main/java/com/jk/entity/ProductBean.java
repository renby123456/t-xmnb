package com.jk.entity;


import lombok.Data;

@Data
public class ProductBean {


    private Integer id;

    private String adTitle;

    private String imageUrl;
    private Double pcPrice;
    private String color;
    private String linkUrl;

    private Integer skuCid3;
}
