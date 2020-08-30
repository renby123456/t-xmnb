package com.jk.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@Document(indexName = "product_index",type="product")
public class ProductEntity {

    @Id
    private String id;

    @Field(type = FieldType.Text,analyzer = "ik_max_word")
    private String ad_title;

    private String image_url;
    private Double pc_price;
    private String color;
    private String link_url;

    private Integer startPrice;
    private Integer endPrice;
}
