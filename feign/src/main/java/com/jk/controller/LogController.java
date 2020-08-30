package com.jk.controller;


import com.jk.entity.ProductBean;
import com.jk.entity.ProductEntity;
import com.jk.entity.UserEntity;
import com.jk.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.servlet.http.PushBuilder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("user")
public class LogController {

    @Resource
    private UserService userService;


    //跳转导航栏页面
    @RequestMapping("topro")
    public String topro(@RequestParam("adTitle") String adTitle, @RequestParam("skuCid3") Integer skuCid3, Model model){
        model.addAttribute("adTitle",adTitle);
        model.addAttribute("skuCid3",skuCid3);
        return "product";
    }

    //跳转商品详情页面
    @RequestMapping("toproductss")
    public String toproductss(){
        return "productbyid";
    }

    //注册
    @RequestMapping("zhuce")
    @ResponseBody
    public Map zhuce(@RequestParam("userName") String userName,@RequestParam("passwords") String passwords,@RequestParam("account") String account,@RequestParam("password") String password){
        return userService.selectuser(userName,password,passwords,account);
    }
    //跳转注册页面
    @RequestMapping("tozhuce")
    public String tozhuce(){
        return "zhuce";
    }
    //登录
    @RequestMapping("login")
    @ResponseBody
    public Map login(HttpSession session,@RequestParam("account") String account,@RequestParam("password") String password){
        HashMap map = new HashMap();
        UserEntity userEntities = userService.selectuserAccount(account);
        //判断账号是否存在
        if (userEntities == null){
            map.put("code", 1);
            map.put("msg", "账号不存在");
            return map;
        }
        if (!userEntities.getPassword().equals(password)){
            map.put("code", 2);
            map.put("msg", "密码错误");
            return map;
        }
        session.setAttribute(session.getId(),userEntities);
        map.put("code", 0);
        map.put("msg", "登录成功");
        return map;
    }
    //跳转ES查询页面
    @RequestMapping("toproduct")
    public String toproduct(){
        return "product";
    }
    //跳转登录页面
    @RequestMapping("tologin")
    public String tologin(){
        return "login";
    }
    //跳转主页面
    @RequestMapping("tomain")
    public String tomain(){
        return "main";
    }
}
