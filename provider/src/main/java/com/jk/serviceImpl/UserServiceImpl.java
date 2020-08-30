package com.jk.serviceImpl;

import com.jk.entity.ProductBean;
import com.jk.entity.ProductEntity;
import com.jk.entity.UserEntity;
import com.jk.mapper.UserMapper;
import com.jk.service.UserService;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;


    @Override
    @RequestMapping("login")
    public UserEntity selectuserAccount(String account) {
        UserEntity userEntity = userMapper.selectuserAccount(account);
        return userEntity;
    }

    @Override
    @RequestMapping("createIndexMySQL")
    public void saveAll(@RequestBody List<ProductBean> productBeans) {
        userMapper.saveAll(productBeans);
    }

    @Override
    @RequestMapping("findProductList")
    public Map findProductLists(@RequestParam Integer page, @RequestParam Integer rows) {
            int counta = userMapper.counta();
            int str = (page-1)*rows;
            int end = rows;
            List<ProductBean> lista = userMapper.findProductLista(str,end);
            HashMap<String, Object> map = new HashMap<>();
            map.put("total",counta);
            map.put("rows",lista);
            return map;
    }

    @Override
    public List<ProductBean> findProductByCid(@RequestParam String adTitle,@RequestParam Integer skuCid3) {
        return userMapper.findProductByCid(adTitle,skuCid3);
    }

    @Override
    @RequestMapping("zhuce")
    public Map selectuser(@RequestParam String userName,@RequestParam String password,@RequestParam String passwords,@RequestParam String account) {
        HashMap map = new HashMap();
        if(!passwords.equals(password)){
            map.put("code", 1);
            map.put("msg", "两次密码不一致，请重新输入！");
            return map;
        }
        UserEntity userEntitie =  userMapper.selectuserAccount(account);
        if(userEntitie  !=null ){
            map.put("code", 2);
            map.put("msg", "账号已存在，请重新输入！");
            return map;
        }
            userMapper.saveUser(userName,account,password);
            map.put("code", 0);
            map.put("msg", "注册成功,请登录！");
            return map;
    }

    @Override
    @RequestMapping("productbyid")
    public ProductEntity productbyid(@RequestParam Integer id) {
        return userMapper.productbyid(id);
    }
}
