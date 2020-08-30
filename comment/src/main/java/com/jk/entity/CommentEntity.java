package com.jk.entity;

import lombok.Data;

@Data
public class CommentEntity {

    private Integer id;
    private String commentParticulars;
    private String commentTime;
    private Integer commentImgId;

}
