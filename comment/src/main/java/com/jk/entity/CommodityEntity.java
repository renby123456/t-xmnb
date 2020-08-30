package com.jk.entity;

import lombok.Data;

@Data
public class CommodityEntity {


    private Integer id;
    private String commodityName;
    private String commodityColour;
    private Double commodityPrice;
    private String commodityIntroduce;
    private Integer commodityBrandId;
    private Integer commodityImgId;
    private Integer commodityTypeId;


}
