package com.augmentum.service;

import com.augmentum.domain.User;

import java.util.List;

/**
 * @author lzh
 * @createe 2023-01-21-22:40
 */
public interface UserService {

    boolean register(User user);    //是否

    User login(String uname);   //登录

    int updatePhoneAddress(User user);     //更新电话and地址

    boolean updatePassword(String uid, String oldupwd, String upwd);    //更改密码

    List<User> queryUserListAll();  //查询所有用户

    boolean updatePasswordByadmin(int uid, String upwd);    //管理员修改密码

    User queryUserByUid(int uid);   //根据uid查询user

    boolean updateUserByUid(int uid, String uphone, String uaddress);   //uid更改电话地址

    boolean deleteUserByUid(int uid);   //删除用户
}
