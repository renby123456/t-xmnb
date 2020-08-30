package com.jk.controller;


import com.jk.entity.ProductEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ItemRepository extends ElasticsearchRepository<ProductEntity,String> {


}
