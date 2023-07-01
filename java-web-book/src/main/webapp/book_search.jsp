<%--
  Created by IntelliJ IDEA.
  User: vili
  Date: 2019/8/27
  Time: 20:17
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page isELIgnored="false" %>
<!DOCTYPE html>
<html>
<head>
    <title>首页</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <link type="text/css" rel="stylesheet" href="css/bootstrap.css">
    <link type="text/css" rel="stylesheet" href="css/style.css">
    <script type="text/javascript" src="js/jquery.min.js"></script>
    <script type="text/javascript" src="js/bootstrap.min.js"></script>
    <script type="text/javascript" src="js/simpleCart.min.js"></script>
    <script type="text/javascript" src="layer/layer.js"></script>
    <script type="text/javascript" src="js/cart.js"></script>
    <script type="text/javascript">
        $(function (){
            $("#a5").click(function (){
                input1 = document.getElementById("input1").value;
                if(input1 === ''){
                    input1 = 1;
                }
                var abq = document.getElementById("a5");
                abq.href = "/book/pagesSearchBooks.action?page="+input1+"&size=${pageInfo.pageSize}&pages=${pageInfo.pages}&keyword=${keyword}";
            })
        })
    </script>
</head>
<body>


<jsp:include page="/header.jsp">
    <jsp:param value="8" name="flag"/>
</jsp:include>


<!--products-->
<div class="products">
    <div class="container">
        <h2> 搜索 ‘${param.keyword }’的结果 </h2>

        <div class="col-md-12 product-model-sec">

            <c:forEach items="${p}" var="book">
                <div class="product-grid">
                    <a href="book_detail.action?bid=${book.bid}">
                        <div class="more-product"><span> </span></div>
                        <div class="product-img b-link-stripe b-animate-go  thickbox">
                            <img src="${book.bcover}" class="img-responsive" alt="${book.bname }" width="240" height="240">
                            <div class="b-wrapper">
                                <h4 class="b-animate b-from-left  b-delay03">
                                    <button>查看详情</button>
                                </h4>
                            </div>
                        </div>
                    </a>
                    <div class="product-info simpleCart_shelfItem">
                        <div class="product-info-cust prt_name">
                            <h4>${book.bname }</h4>
                            <span class="item_price">¥ ${book.bprice }</span>
                            <input type="button" class="item_add items" value="加入购物车" onclick="buy(${book.bid})">
                            <div class="clearfix"> </div>
                        </div>
                    </div>
                </div>
            </c:forEach>

            <div class="clearfix"> </div>
        </div>


        <%--        翻页--%>
        <div style='text-align:center;'>
            <a class='btn btn-info' id="a1" href="/book/pagesSearchBooks.action?page=1&size=${pageInfo.pageSize}&keyword=${keyword}">首页</a>

            <a class='btn btn-info' id="a2" href="/book/pagesSearchBooks.action?page=${pageInfo.pageNum-1}&size=${pageInfo.pageSize}&keyword=${keyword}">上一页</a>

            <h3 style='display:inline;'>[${pageInfo.pageNum}/${pageInfo.pages}]</h3>
            <h3 style='display:inline;'>[${pageInfo.total}]</h3>

            <a class='btn btn-info' id="a3" href="/book/pagesSearchBooks.action?page=${pageInfo.pageNum+1}&size=${pageInfo.pageSize}&pages=${pageInfo.pages}&keyword=${keyword}">下一页</a>
            <a class='btn btn-info' id="a4" href="/book/pagesSearchBooks.action?page=${pageInfo.pages}&size=${pageInfo.pageSize}&keyword=${keyword}">尾页</a>
            <input type='text' id="input1" class='form-control' style='display:inline;width:60px;' value=''/>
            <a class='btn btn-info' id="a5">GO</a>

        </div>



<%--        <div>--%>
<%--            <jsp:include page="page.jsp">--%>
<%--                <jsp:param name="url" value="search_books.action"></jsp:param>--%>
<%--                <jsp:param name="param" value="&keyword=${keyword}"></jsp:param>--%>
<%--            </jsp:include>--%>
<%--        </div>--%>
    </div>
</div>
<!--//products-->

<jsp:include page="/footer.jsp"></jsp:include>

</body>
</html>
