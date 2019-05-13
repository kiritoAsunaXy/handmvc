package com.xuyu.handmvc.service;

import com.xuyu.handmvc.annoations.XuyuService;


public interface UserService {
    String query(String name,Integer age);
    String insert(String param);
    String update(String param);

}
