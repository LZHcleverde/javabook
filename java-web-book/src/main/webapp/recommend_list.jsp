<%--
  Created by IntelliJ IDEA.
  User: vili
  Date: 2019/8/26
  Time: 13:14
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
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
                abq.href = "/book/pagesRecommend.action?page="+input1+"&size=${pageInfo.pageSize}&pages=${pageInfo.pages}&t=${t}";
            })
        })
    </script>
</head>
<body>

<!--header-->
<jsp:include page="header.jsp">
    <jsp:param name="flag" value="3"></jsp:param>
</jsp:include>
<!--//header-->

<!--products-->
<div class="products">
    <div class="container">
        <h2><c:choose><c:when test="${t==2}">热销商品</c:when><c:otherwise>新品商品</c:otherwise></c:choose></h2>

        <div class="col-md-12 product-model-sec">

            <c:forEach items="${p}" var="g">
                <div class="product-grid">
                    <a href="book_detail.action?bid=${g.bid}">
                        <div class="more-product"><span> </span></div>
                        <div class="product-img b-link-stripe b-animate-go  thickbox">
                            <img src="${g.bcover}" class="img-responsive" alt="${g.bname}" width="240" height="240">
                            <div class="b-wrapper">
                                <h4 class="b-animate b-from-left  b-delay03">
                                    <button href="book_detail.action?bid=${g.bid}">查看详情</button>
                                </h4>
                            </div>
                        </div>
                    </a>
                    <div class="product-info simpleCart_shelfItem">
                        <div class="product-info-cust prt_name">
                            <h4>${g.bname}</h4>
                            <span class="item_price">¥ ${g.bprice}</span>
                            <input type="button" class="item_add items" value="加入购物车" onclick="buy(${g.bid})">
                            <div class="clearfix"> </div>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>

        <%--        翻页--%>
        <div style='text-align:center;'>
            <a class='btn btn-info' id="a1" href="/book/pagesRecommend.action?page=1&size=${pageInfo.pageSize}&t=${t}">首页</a>

            <a class='btn btn-info' id="a2" href="/book/pagesRecommend.action?page=${pageInfo.pageNum-1}&size=${pageInfo.pageSize}&t=${t}">上一页</a>

            <h3 style='display:inline;'>[${pageInfo.pageNum}/${pageInfo.pages}]</h3>
            <h3 style='display:inline;'>[${pageInfo.total}]</h3>

            <a class='btn btn-info' id="a3" href="/book/pagesRecommend.action?page=${pageInfo.pageNum+1}&size=${pageInfo.pageSize}&pages=${pageInfo.pages}&t=${t}">下一页</a>
            <a class='btn btn-info' id="a4" href="/book/pagesRecommend.action?page=${pageInfo.pages}&size=${pageInfo.pageSize}&t=${t}">尾页</a>
            <input type='text' id="input1" class='form-control' style='display:inline;width:60px;' value=''/>
            <a class='btn btn-info' id="a5">GO</a>

        </div>

    </div>
</div>
</div>
<!--//products-->
<%--<jsp:include page="page.jsp">--%>
<%--    <jsp:param name="url" value="recommend_books.action"></jsp:param>--%>
<%--    <jsp:param name="param" value="&rtype=${t}"></jsp:param>--%>
<%--</jsp:include>--%>



<!--footer-->
<jsp:include page="footer.jsp"></jsp:include>
<!--//footer-->

</body>
</html>
