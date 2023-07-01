package com.augmentum.controller;

import com.augmentum.dao.BookDao;
import com.augmentum.domain.Book;
import com.augmentum.domain.Order;
import com.augmentum.domain.OrderItem;
import com.augmentum.domain.User;
import com.augmentum.excep.NotEnoughExeception;
import com.augmentum.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author lzh
 * @createe 2023-01-23-12:28
 */


@Controller
public class OrderController {

    @Autowired
    private OrderService orderService;


    @Autowired
    private BookDao bookDao;


    //提交订单
    @RequestMapping("/order_submit.action")
    public String orderSubmit(HttpServletRequest request,double money) throws InterruptedException {
        String path = "";
        if(money == 0){
            request.setAttribute("failMsg","购物车没书啊！亲！");
            return "redirect:/index.action";
        }
        User usertip = (User) request.getSession().getAttribute("usertip");
        if(usertip == null){
            request.setAttribute("failMsg","还未登录，请登录！");
            path = "user_login";
        }

        if(usertip != null && money != 0){
            path = "order_submit";
        }
        return path;
    }
    //确定订单
    @RequestMapping("/order_confirm.action")
    public String orderConfirm(int opaytype,HttpServletRequest request){
        String path ="order_result";
        //删除消息
        request.getSession().removeAttribute("failbook");
        request.getSession().removeAttribute("failorder");


        //将订单和用户绑定在一起
        Order order = (Order) request.getSession().getAttribute("order");
        order.setOstatus(2);
        order.setOpaytype(opaytype);
        User usertip = (User) request.getSession().getAttribute("usertip");
        order.setUid(usertip.getUid());
        order.setOrealname(usertip.getUrealname());
        order.setOphone(usertip.getUphone());
        order.setOaddress(usertip.getUaddress());

        boolean result = false;
        //添加订单
        try {
            result = orderService.addOrder(order);
        }catch (NotEnoughExeception e){
            ArrayList<Book> arrayListBook = new ArrayList<>();
            for (Map.Entry<Integer, OrderItem> en : order.getItemMap().entrySet()) {
                //每本书的库存
                Integer key = en.getKey();
                Book book = bookDao.queryBookBybid(key);
                Integer bstock = book.getBstock();

                //需要购买的书的数量
                OrderItem item = en.getValue();
                Integer oiamount = item.getOiamount();

                if(bstock < oiamount){
                    arrayListBook.add(book);
                }
            }
            request.getSession().setAttribute("failbook",arrayListBook);
            request.getSession().setAttribute("failorder","订单支付失败了！");

            path = "redirect:/book_cart.jsp";
        }

        if(result){
            request.setAttribute("msg","订单支付成功！");
        }
//        return "order_result";
        return path;
    }




    //展示订单
    @RequestMapping("/order_list.action")
    public String ShowOrderByUid(HttpServletRequest request){

        User usertip = (User) request.getSession().getAttribute("usertip");

        List<Order> orderList = orderService.queryOrderByUid(usertip.getUid());
        request.setAttribute("orderList",orderList);

        return "order_list";
    }


/**
 * -------------------------管理员 -------------------------管理员  -------------------------管理员  ------------------------- ------------------------- -------------------------
 */
    //展示订单
    @RequestMapping("/admin/order_list.action")
    public String ShowOrderAll(int ostatus,HttpServletRequest request){
        System.out.println(ostatus);
        //返回状态
        request.setAttribute("ostatus",ostatus);
        List<Order> orderList = orderService.queryOrderByOstatus(ostatus);
        request.setAttribute("p",orderList);
        return "admin/order_list";
    }
    //修改订单
    @RequestMapping("/admin/order_status_change.action")
    public String order_status_change(String oid,Integer ostatus,HttpServletRequest request){

        boolean result = orderService.changeorderOstatusByOid(oid,ostatus);
        List<Order> orderList = orderService.queryOrderByOstatus(ostatus);
        request.setAttribute("p",orderList);
        return "admin/order_list";
    }
    //删除订单
    @RequestMapping("/admin/order_delete.action")
    public String orderDelete(String oid,Integer ostatus,HttpServletRequest request){

        boolean result = orderService.deleteorder(oid,ostatus);
        request.setAttribute("ostatus",ostatus);
        List<Order> orderList = orderService.queryOrderByOstatus(ostatus);
        request.setAttribute("p",orderList);

        return "admin/order_list";
    }





}
