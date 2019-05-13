package com.xuyu.handmvc.service;

import com.xuyu.handmvc.annoations.XuyuService;

@XuyuService("xuyuServiceImpl")
public class UserServiceImpl implements UserService{

    @Override
    public String query(String name, Integer age) {
        return "name : "+name+"  age : "+age;
    }

    @Override
    public String insert(String param) {
        return "insert success..........";
    }

    @Override
    public String update(String param) {
        return "update success...........";
    }
}
