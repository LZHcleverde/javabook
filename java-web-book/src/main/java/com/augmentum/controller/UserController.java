package com.augmentum.controller;

import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.augmentum.Utils.MyCipher;
import com.augmentum.Utils.SmsUtils;
import com.augmentum.domain.User;
import com.augmentum.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author lzh
 * @createe 2023-01-21-22:31
 */


@Controller
public class UserController {


    @Autowired
    private UserService userService;

    //注册
    @RequestMapping("/register.action")
    public String register(User user,String ucode, HttpServletRequest request){

        String path = "";
        //设置权限
        user.setUrole(1);
        user.setUmark("普通户用");

        //验证码验证
        if(request.getSession().getAttribute("code") != null && ucode.equals(request.getSession().getAttribute("code"))){
            path = "redirect:user_login.jsp";
        }else {
            request.setAttribute("msg","验证码错误或已失效！");
            path = "user_register";
            return path;
        }
        //用户名正常
        if(userService.register(user)){
            //转发到登录
            path = "redirect:user_login.jsp";
        }else {
            //用户名重复
            request.setAttribute("msg","用户名重复！");
            path = "user_register";
        }
        return path;
    }

    @RequestMapping("/registerCode.action")
    public void registerCode(String phone, HttpServletResponse response,HttpSession session){
        //定义验证码
        int v = (int) (Math.random() * 90000 + 10000);
        String code = String.valueOf(v);
        System.out.println(code);
        session.setAttribute("code",code);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(300000);
                    session.removeAttribute("code");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();


        try {
            SmsUtils.sendSms(phone, code);
        } catch (ClientException e) {
            System.out.println("信息发送失败！");
            e.printStackTrace();
        }
    }



    //登录
    @RequestMapping("/login.action")
    public String login(User tempUser, HttpServletRequest request, HttpSession session){

        //判断用户名是否存在
        User user = userService.login(tempUser.getUname());

        if(user == null){
            request.setAttribute("failMsg","用户名不存在！");
            return "user_login";
        }else {
            //密码比对
            if(tempUser.getUpwd().equals(user.getUpwd())){
                if(user.getUname().equals("admin")){
                    user.setIsadmin(true);
                }
                session.setAttribute("usertip",user);
                request.setAttribute("msg","登陆成功");
                return "redirect:/index.action";
            }else {
                request.setAttribute("failMsg","密码错误！");
                return "user_login";
            }
        }
    }
    //退出登录
    @RequestMapping(value = {"/logout.action","/admin/logout.action"})
    public String logout(HttpSession session){
        session.removeAttribute("usertip");
        return "redirect:/index.action";
    }



    //更改电话和地址
    @RequestMapping("/change_phone_and_address.action")
    public String updatePhoneAndAddress(User user,HttpServletRequest request){

        int i = userService.updatePhoneAddress(user);

        if(i==1){
            request.setAttribute("usertip",user);
        }

        return "user_center";
    }

    //更改密码
    @RequestMapping("/change_password.action")
    public String updatePassword(String uid,String oldupwd,String upwd,HttpServletRequest request){

        //id寻找账号的密码，进行替换
        boolean flag = userService.updatePassword(uid,oldupwd,upwd);
        if(flag){
            request.setAttribute("msg","修改成功！");
            User usertip = (User) request.getSession().getAttribute("usertip");
            MyCipher mc = new MyCipher();
            usertip.setUpwd(mc.encrypt(upwd,"!"));
        }else {
            request.setAttribute("failMsg","修改密码时出现错误，请确认原密码是否正确或联系管理员!");
        }

        return "user_center";
    }


/**
 * -------------------------管理员 -------------------------管理员  -------------------------管理员  ------------------------- ------------------------- -------------------------
 */

    @RequestMapping("/admin/user_list.action")
    public String userList(HttpServletRequest request){

        List<User> userList = userService.queryUserListAll();
        request.setAttribute("p",userList);

        return "admin/user_list";
    }


    //重置密码
    @RequestMapping("/admin/change_password.action")
    public String changePassword(int uid,String upwd,HttpServletRequest request){

        boolean result = userService.updatePasswordByadmin(uid,upwd);

        if(result){
            request.setAttribute("msg","修改成功！");
        }else {
            request.setAttribute("failMsg","修改失败");
        }


        //更新用户信息
        List<User> userList = userService.queryUserListAll();
        request.setAttribute("p",userList);
        return "admin/user_list";
    }


    //修改用户信息
    @RequestMapping("/admin/user_edit_show.action")
    public String userEditShow(int uid,HttpServletRequest request){

        User user = userService.queryUserByUid(uid);
        request.setAttribute("u",user);

        return "admin/user_edit";
    }


    @RequestMapping("/admin/user_update.action")
    public String userUpdate(int uid,String uphone,String uaddress,HttpServletRequest request){

        boolean result = userService.updateUserByUid(uid, uphone, uaddress);
        if(result){
            request.setAttribute("msg","修改成功！");

        }else {
            request.setAttribute("failMsg","修改失败");
        }


        //更新用户信息
        List<User> userList = userService.queryUserListAll();
        request.setAttribute("p",userList);

        return "admin/user_list";
    }


    //删除用户信息
    @RequestMapping("/admin/user_delete.action")
    public String userDelete(int uid,HttpServletRequest request){

        boolean result = userService.deleteUserByUid(uid);
        if(result){
            request.setAttribute("msg","删除成功！");

        }else {
            request.setAttribute("failMsg","删除失败");
        }

        //更新用户信息
        List<User> userList = userService.queryUserListAll();
        request.setAttribute("p",userList);

        return "admin/user_list";
    }









}
