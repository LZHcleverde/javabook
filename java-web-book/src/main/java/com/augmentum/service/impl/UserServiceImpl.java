package com.augmentum.service.impl;

import com.augmentum.Utils.MyCipher;
import com.augmentum.dao.UserDao;
import com.augmentum.domain.Order;
import com.augmentum.domain.OrderItem;
import com.augmentum.domain.User;
import com.augmentum.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lzh
 * @createe 2023-01-21-22:40
 */


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;


    @Override
    public boolean register(User user) {

        boolean flag = false;


        //判断用户名是否重复
        int result = userDao.identify(user.getUname());
        if(result >= 1)
            return flag;

        //判断是否可以注册
        MyCipher mc = new MyCipher();
        user.setUpwd(mc.encrypt(user.getUpwd(),"!"));

        //注册
        userDao.addUser(user);
        flag = true;

        return flag;
    }

    @Override
    public User login(String uname) {

        //解密
        MyCipher mc = new MyCipher();
        //获取用户信息
        User user = userDao.login(uname);

        if(user != null){
            //解密密码设置
            user.setUpwd(mc.decrypt(user.getUpwd(),"!"));
        }
        return user;
    }

    @Override
    public int updatePhoneAddress(User user) {
        return userDao.updatePhoneAddress(user);
    }

    @Override
    public boolean updatePassword(String uid, String oldupwd, String upwd) {

        MyCipher mc = new MyCipher();
        String pwdold = userDao.queryUpwd(uid);
        if(pwdold.equals(mc.encrypt(oldupwd,"!"))){
            //更新密码
            userDao.updatePassword(uid,mc.encrypt(upwd,"!"));
            return true;

        }else {
            return false;
        }
    }

    @Override
    public List<User> queryUserListAll() {
        return userDao.queryUserListAll();
    }

    //管理员修改密码
    @Override
    public boolean updatePasswordByadmin(int uid, String upwd) {

        MyCipher mc = new MyCipher();
        userDao.updatePassword(String.valueOf(uid),mc.encrypt(upwd,"!"));

        return true;
    }

    @Override
    public User queryUserByUid(int uid) {
        return userDao.queryUserByUid(uid);
    }

    @Override
    public boolean updateUserByUid(int uid, String uphone, String uaddress) {

        userDao.updateUserByUid(uid,uphone,uaddress);

        return true;
    }

    @Override
    public boolean deleteUserByUid(int uid) {

        //找到uid的所有订单oid 返回
        List<Order> orderList = userDao.selectOidByUid(uid);
        for (Order order : orderList) {
            String oid = order.getOid();

            //根据oid找到对应的书籍
            List<OrderItem> items = userDao.selectOiidByOid(oid);
            for (OrderItem item : items) {
                Integer oiid = item.getOiid();
                //删除所有订单书籍信息
                userDao.deleteOrderItemByOiid(oiid);
            }
            //删除所有订单
            userDao.deleteOrderByOid(oid);
        }
        //删除用户
        userDao.deleteUserByUid(uid);
        return true;
    }
}
