package com.augmentum.dao;

import com.augmentum.domain.Order;
import com.augmentum.domain.OrderItem;
import com.augmentum.domain.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author lzh
 * @createe 2023-01-21-22:42
 */
public interface UserDao {


    boolean addUser(User user); //添加用户

    int identify(@Param("uname") String uname); //判断用户名是否重复

    User login(@Param("uname") String uname);

    int updatePhoneAddress(User user); //更新电话和地址

    String queryUpwd(@Param("uid") String uid);   //查找旧密码

    void updatePassword(@Param("uid") String uid,@Param("upwd") String encrypt);    //更新密码

    List<User> queryUserListAll();  //查询所有user对象

    User queryUserByUid(@Param("uid") int uid);   //根据uid查询user

    void updateUserByUid(@Param("uid") int uid, @Param("uphone") String uphone,@Param("uaddress") String uaddress); //uid更改电话地址

    List<Order> selectOidByUid(@Param("uid") int uid);     //返回将要删除的订单编号

    List<OrderItem> selectOiidByOid(@Param("oid") String oid);    //返回要删除的item信息

    void deleteOrderItemByOiid(@Param("oiid") Integer oiid);   //删除所有订单书籍信息

    void deleteOrderByOid(@Param("oid") String oid);  //删除所有订单

    void deleteUserByUid(@Param("uid") int uid);  //删除用户
}
