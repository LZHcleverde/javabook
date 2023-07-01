package com.augmentum.service.impl;

import com.augmentum.dao.BookDao;
import com.augmentum.dao.OrderDao;
import com.augmentum.domain.Book;
import com.augmentum.domain.Order;
import com.augmentum.domain.OrderItem;
import com.augmentum.excep.NotEnoughExeception;
import com.augmentum.service.BookService;
import com.augmentum.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.print.attribute.IntegerSyntax;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author lzh
 * @createe 2023-01-23-12:29
 */


@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;


    @Autowired
    private BookDao bookDao;


    //处理事务
    @Transactional(
            //传播行为
            propagation = Propagation.REQUIRED,
            isolation = Isolation.DEFAULT,
            readOnly = false,
            rollbackFor = {
                    NotEnoughExeception.class
            }
    )


    @Override
    public boolean addOrder(Order order) {

        //生成订单id
        String orderid = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
        order.setOid(orderid);

        //插入订单数据
        orderDao.addOrder(order);
        for (Map.Entry<Integer, OrderItem> en : order.getItemMap().entrySet()) {
            OrderItem item = en.getValue();
            //item设置属性=订单id
            item.setOid(orderid);
            //插入书籍的信息到orderItem表
            orderDao.addOrderItem(item);
        }



        for (Map.Entry<Integer, OrderItem> en : order.getItemMap().entrySet()) {
            //每本书的库存
            Integer key = en.getKey();
            Book book = bookDao.queryBookBybid(key);
            Integer bstock = book.getBstock();

            //需要购买的书的数量
            OrderItem item = en.getValue();
            Integer oiamount = item.getOiamount();

            if(bstock < oiamount){
                //回滚事务
                throw new NotEnoughExeception("书籍：" + book.getBname() + "库存不足！" + "可购买：" + book.getBstock() + "本！");
            }else {
                //更新库存
                book.setBstock(bstock - oiamount);
                bookDao.updateBook(book);
            }

        }
        return true;
    }

    @Override
    public List<Order> queryOrderByUid(Integer uid) {

        List<Order> orderList = new ArrayList<>();

        //通过用户id查询多个订单
        orderList = orderDao.queryOrderByUid(uid);

        //根据每一个订单号在item中查询多个书的id，数量，价格
        for (Order order : orderList) {

            String oid = order.getOid();
            List<OrderItem> orderItem = orderDao.queryOrderItembyOid(oid);
            order.setItemList(orderItem);
            for (OrderItem item : orderItem) {
                //根据书id查询书名
                Book book = bookDao.queryBookBybid(item.getBid());
                item.setBook(book);
            }
        }
        return orderList;
    }

    @Override
    public List<Order> queryOrderByOstatus(int ostatus) {

        List<Order> orderList = new ArrayList<>();

        //通过用户id查询多个订单
        orderList = orderDao.queryOrderByostatus(ostatus);

        //根据每一个订单号在item中查询多个书的id，数量，价格
        for (Order order : orderList) {

            String oid = order.getOid();
            List<OrderItem> orderItem = orderDao.queryOrderItembyOid(oid);
            order.setItemList(orderItem);
            for (OrderItem item : orderItem) {
                //根据书id查询书名
                Book book = bookDao.queryBookBybid(item.getBid());
                item.setBook(book);
            }
        }
        return orderList;

    }

    @Override
    public boolean changeorderOstatusByOid(String oid, Integer ostatus) {
        int result = bookDao.updateOrderOstatusByOid(oid,ostatus);

        if(result == 1){
            return true;
        }else {
            return false;
        }

    }

    @Override
    public boolean deleteorder(String oid, Integer ostatus) {

        //删除订单书籍
        int result = orderDao.deleteOBook(oid);

        //删除订单
        int resultBook = orderDao.deleteOrder(oid);

        if(result >= 1 && resultBook >=1){
            return true;
        }

        return false;
    }
}
