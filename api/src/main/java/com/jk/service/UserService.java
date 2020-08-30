package com.jk.service;

import com.jk.entity.ProductBean;
import com.jk.entity.ProductEntity;
import com.jk.entity.UserEntity;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@FeignClient(value = "provider")
public interface UserService {

    @RequestMapping("login")
    UserEntity selectuserAccount(@RequestParam String account);

    @RequestMapping("createIndexMySQL")
    void saveAll(@RequestBody List<ProductBean> productBeans);

    @RequestMapping("findProductList")
    Map findProductLists(@RequestParam Integer page, @RequestParam Integer rows);

    @RequestMapping("findProductByCid")
    List<ProductBean> findProductByCid(@RequestParam("adTitle") String adTitle,@RequestParam("skuCid3") Integer skuCid3);

    @RequestMapping("zhuce")
    Map selectuser(@RequestParam("userName") String userName, @RequestParam("password") String password,@RequestParam("passwords")  String passwords, @RequestParam("account") String account);

    @RequestMapping("productbyid")
    ProductEntity productbyid(@RequestParam("id") Integer id);
}
