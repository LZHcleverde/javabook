<%--
  Created by IntelliJ IDEA.
  User: vili
  Date: 2019/8/23
  Time: 14:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page isELIgnored="false" %>

<!DOCTYPE html>
<html>
<head>
    <title>用户注册</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <link type="text/css" rel="stylesheet" href="css/bootstrap.css">
    <link type="text/css" rel="stylesheet" href="css/style.css">
    <script type="text/javascript" src="js/jquery.min.js"></script>
    <script type="text/javascript" src="js/bootstrap.min.js"></script>
    <script type="text/javascript" src="js/cart.js"></script>
</head>
<script type="text/javascript">

    function c(i){

        var btn = document.getElementById("btn")
        btn.disabled = true;

        i--;
        if(i==0){
            btn.value = "获取验证码"
            btn.disabled=false
        } else{

            btn.value = "稍后重试(" + i + ")"
            setTimeout("c("+i+")",1000)
        }
    }


    $(function (){
        $("#btn").click(function (){

        document.getElementById("btn").disabled = true;
        c(60)

            var pho = document.getElementById("pho").value

            $.ajax({
                type : "GET",
                url : "registerCode.action",
                data : "phone=" + pho,
                async : true
            })
        })
    })

</script>
<body>

<!--header-->
<jsp:include page="/header.jsp">
    <jsp:param name="flag" value="10"></jsp:param>
</jsp:include>
<!--//header-->


<!--account-->
<div class="account">
    <div class="container">
        <div class="register">
            <c:if test="${!empty msg }">
                <div class="alert alert-danger">${msg }</div>
            </c:if>
            <form action="register.action" method="post">
                <div class="register-top-grid">
                    <h3>注册新用户</h3>
                    <div class="input">
                        <span>用户名 <label style="color:red;">*</label></span>
                        <input type="text" name="uname" placeholder="请输入用户名" required="required">
                    </div>
                    <div class="input">
                        <span>真实姓名 <label style="color:red;">*</label></span>
                        <input type="text" name="urealname" placeholder="请输入真实姓名" required="required">
                    </div>
                    <div class="input">
                        <span>密码 <label style="color:red;">*</label></span>
                        <input type="password" name="upwd" placeholder="请输入密码" required="required">
                    </div>
                    <div class="input">
                        <span>收货电话<label style="color:red;">*</label></span>
                        <input type="text" id="pho" name="uphone" placeholder="请输入收货电话">
                    </div>
                    <div class="input">
                        <span>收货地址<label></label></span>
                        <input type="text" name="uaddress" placeholder="请输入收货地址">
                    </div>
                    <div style="width: 200px">
                        <span>(收货电话)验证码<label style="color:red;">*</label></span>
                        <input type="text" name="ucode" placeholder="请输入验证码？" required="required">
                    </div>
                    <input type="button" id="btn" value="获取验证码" >
                    <div class="clearfix"> </div>
                </div>
                <div class="register-but text-center">
                    <input type="submit" value="提交">
                    <div class="clearfix"> </div>
                </div>
            </form>
            <div class="clearfix"> </div>
        </div>
    </div>
</div>
<!--//account-->






<!--footer-->
<jsp:include page="/footer.jsp"></jsp:include>
<!--//footer-->


</body>
</html>