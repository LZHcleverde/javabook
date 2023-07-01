package com.augmentum.dao;

import com.augmentum.domain.Order;
import com.augmentum.domain.OrderItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author lzh
 * @createe 2023-01-23-12:30
 */
public interface OrderDao {

    void addOrder(Order order);  //添加订单

    void addOrderItem(OrderItem item);  //添加orderItem

    List<Order> queryOrderByUid(@Param("uid") Integer uid);   //查询该id下的订单

    List<OrderItem> queryOrderItembyOid(@Param("oid") String oid);  //根据订单id查询订单的书籍信息

    List<Order> queryOrderByostatus(@Param("ostatus") int ostatus);   //通过状态获取订单

    int deleteOBook(@Param("oid") String oid);   //删除订单书籍

    int deleteOrder(String oid);   //删除订单
}
