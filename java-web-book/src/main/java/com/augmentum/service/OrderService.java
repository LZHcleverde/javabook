package com.augmentum.service;

import com.augmentum.domain.Order;

import java.util.List;

/**
 * @author lzh
 * @createe 2023-01-23-12:29
 */
public interface OrderService {


    boolean addOrder(Order order);  //添加订单

    List<Order> queryOrderByUid(Integer uid);   //查询该用户下的订单

    List<Order> queryOrderByOstatus(int ostatus);   //通过状态获取订单

    boolean changeorderOstatusByOid(String oid, Integer ostatus);    //更改书籍状态

    boolean deleteorder(String oid, Integer ostatus);   //删除订单

}
