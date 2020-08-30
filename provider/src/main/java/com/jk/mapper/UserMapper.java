package com.jk.mapper;

import com.jk.entity.ProductBean;
import com.jk.entity.ProductEntity;
import com.jk.entity.UserEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper {

    @Select("select * from t_user where account = #{value}")
    UserEntity selectuserAccount( String account);

    void saveAll(List<ProductBean> productBeans);

    @Select("select count(*) from t_product")
    int counta();

    @Select("select * from t_product  limit #{str},#{end}")
    List<ProductBean> findProductLista(@Param("str") int str,@Param("end") int end);

    @Select("select * from t_product  a where a.ad_title like '%${adTitle}%'  and a.sku_cid3=#{skuCid3}")
    List<ProductBean> findProductByCid(@Param("adTitle")String adTitle,@Param("skuCid3") Integer skuCid3);

    @Insert("insert into t_user (user_name,account,password) value (#{userName},#{account},#{password})")
    void saveUser(@Param("userName")String userName,@Param("account") String account,@Param("password") String password);

    @Select("select * from t_product a where a.id=#{id}")
    ProductEntity productbyid(Integer id);
}
