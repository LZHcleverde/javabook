package com.augmentum.controller;

import com.augmentum.domain.Book;
import com.augmentum.domain.BookType;
import com.augmentum.domain.Order;
import com.augmentum.service.BookService;
import com.augmentum.service.BookTypeService;
import com.github.pagehelper.PageInfo;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @author lzh
 * @createe 2023-01-20-12:11
 */

@Controller
public class BookController {

    @Autowired
    private BookTypeService bookTypeService;

    @Autowired
    private BookService bookService;

    //导航栏 类型选择
    @RequestMapping("/booktypes_list.action")
    public String showBooksByBookTypeID(int btid, HttpServletRequest request){

        //展示的名称
        String bookTypeName = "";
        bookTypeName = bookTypeService.queryBookTypeNameByBookTypeId(btid);
        request.setAttribute("t",bookTypeName);

        List<Book> bookList = null;
        if(btid == -1){
            //查询所有书籍
            bookList = bookService.queryBooksAll();

        }else {
            //有类别的书籍
            bookList = bookService.queryBooksListById(btid);
        }

        //封装页码
        PageInfo<Book> pageInfo = new PageInfo<>(bookList);
        request.setAttribute("pageInfo",pageInfo);
        request.setAttribute("p",bookList);

        //书籍类型
        request.setAttribute("btid",btid);

        return "booktypes_list";
    }

    //类型分页展示+首尾上下
    @RequestMapping("/pages.action")
    public String pages(Integer page,int size,Integer btid,Integer pages,HttpServletRequest request){

        List<Book> bookList = null;

        if(page <= 0){

            page = 1;
        }
        if(pages != null && page > pages){
            page = pages;
        }

        if(btid == -1){

            bookList = bookTypeService.queryBooksAll(page,size);
        }else {
            bookList = bookTypeService.queryBooksById(page,size,btid);
        }
        PageInfo<Book> pageInfo = new PageInfo<>(bookList);
        request.setAttribute("pageInfo",pageInfo);
        request.setAttribute("p",bookList);
        //书籍类型
        request.setAttribute("btid",btid);

        return "booktypes_list";
    }


    //2热销 3新品展示
    @RequestMapping("/recommend_books.action")
    public String showRecommend_books(Integer rtype,int pageNumber,HttpServletRequest request){

        List<Book> bookList = bookService.queryBookByRecommendType(rtype,pageNumber,8);
        request.setAttribute("t",rtype);
        request.setAttribute("p",bookList);

        PageInfo<Book> pageInfo = new PageInfo<>(bookList);
        request.setAttribute("pageInfo",pageInfo);

        return "recommend_list";
    }


    //热销+新品分页展示+首尾上下
    @RequestMapping("/pagesRecommend.action")
    public String pagesRecommend(int page,int size,int t,Integer pages,HttpServletRequest request){

        if(page <= 0){

            page = 1;
        }
        if(pages != null && page > pages){
            page = pages;
        }

        List<Book> bookList = bookService.queryBookRecommendByType(page,size,t);
        PageInfo<Book> pageInfo = new PageInfo<>(bookList);

        request.setAttribute("pageInfo",pageInfo);
        request.setAttribute("p",bookList);
        request.setAttribute("t",t);

        return "recommend_list";
    }


    //书籍详情页
    @RequestMapping("/book_detail.action")
    public String bookDetail(int bid,HttpServletRequest request){

        Book book = bookService.queryBookBybid(bid);
        request.setAttribute("book",book);
        return "book_detail";
    }


    //添加书籍到购物车
    @RequestMapping("/books_buy.action")
    public void booksBuy(int bid, HttpServletRequest request, HttpServletResponse response) throws IOException {

        //购物车中的每一本书都是存储在Order对象中
        Order order = null;
        if(request.getSession().getAttribute("order") != null){
            order = (Order)request.getSession().getAttribute("order");
        }else {
            order = new Order();
            order.setOamount(0);//数量
            order.setOtotal(0.0);//总金额
            request.getSession().setAttribute("order",order);
        }
        //订单的对象已经准备好
        //进行对订单对象中添加书籍
        Book book = bookService.queryBookBybid(bid);
        if(book.getBstock() > 0){
            order.addGoods(book);
            response.getWriter().print("ok");
        }else {
            response.getWriter().print("fail");
        }
    }


    //减少购物车书籍
    @RequestMapping("/books_lessen.action")
    public void booksLessen(int bid ,HttpServletRequest request,HttpServletResponse response) throws IOException {

        Order order = (Order) request.getSession().getAttribute("order");
        order.lessen(bid);
        response.getWriter().print("ok");

    }


    //删除购物车的书籍
    @RequestMapping("/books_delete.action")
    public void booksDelete(int bid,HttpServletRequest request,HttpServletResponse response) throws IOException {

        Order order = (Order) request.getSession().getAttribute("order");

        order.delete(bid);

        response.getWriter().print("ok");
    }


    //搜索书籍 展示第一页
    @RequestMapping("/search_books.action")
    public String searchBooks(int pageNumber,String keyword,HttpServletRequest request){

        List<Book> bookList = bookService.searchBooksByKeyword(pageNumber,8,keyword);

        PageInfo<Book> pageInfo = new PageInfo<>(bookList);

        request.setAttribute("pageInfo",pageInfo);
        request.setAttribute("p",bookList);
        request.setAttribute("keyword",keyword);

        return "book_search";
    }

    //搜索书籍首尾+上下
    @RequestMapping("/pagesSearchBooks.action")
    public String pagesSearchBooks(Integer page,Integer size,Integer pages,String keyword,HttpServletRequest request){

        if(page <= 0){

            page = 1;
        }
        if(pages != null && page > pages){
            page = pages;
        }

        List<Book> bookList = bookService.searchBooksByKeyword(page, size, keyword);
        PageInfo<Book> PageInfo = new PageInfo<>(bookList);

        request.setAttribute("p",bookList);
        request.setAttribute("pageInfo",PageInfo);
        request.setAttribute("keyword",keyword);


        return "book_search";
    }


/**
 * -------------------------管理员 -------------------------管理员  -------------------------管理员  ------------------------- ------------------------- -------------------------
 */


    @RequestMapping("/admin/book_list.action")
    public String book_list(int rtype,HttpServletRequest request){

        //全部商品
        List<Book> bookList = bookService.queryBooksByRtype(rtype);

        request.setAttribute("p",bookList);
        request.getSession().setAttribute("type",rtype);

        return "/admin/book_list";
    }


    //书籍的增删改
    @RequestMapping("/admin/book_change.action")
    public String bookChange(int bid,int rtype,String method,HttpServletRequest request){

        //浮条
        if(rtype == 1){
            if(method.equals("add")){
                bookService.updateBookrtypeById(bid,rtype);
                System.out.println("添加成功");
            }else {
                bookService.deleteBookrtypeById(bid,rtype);
                System.out.println("删除成功");
            }

            //热销
        }else if(rtype == 2){
            if(method.equals("add")){
                bookService.updateBookrtypeById(bid,rtype);
                System.out.println("add");

            }else {
                bookService.deleteBookrtypeById(bid,rtype);
                System.out.println("remove");
            }

            //新品
        }if(rtype == 3){
            if(method.equals("add")){
                bookService.updateBookrtypeById(bid,rtype);
                System.out.println("add");
            }else {
                bookService.deleteBookrtypeById(bid,rtype);
                System.out.println("remove");
            }
        }

        int type = (int) request.getSession().getAttribute("type");
        return "redirect:/admin/book_list.action?rtype="+type;
    }




    @RequestMapping("/admin/book_add.action")
    public String bookAdd(HttpServletRequest request) throws Exception {

        DiskFileItemFactory dfFactory = new DiskFileItemFactory();

        ServletFileUpload sfu = new ServletFileUpload(dfFactory);
        Book b = new Book();

        List<FileItem> fileItems = sfu.parseRequest(request);
        for (FileItem item : fileItems) {
            if(item.isFormField()) {
                switch(item.getFieldName()) {
                    case "bname":
                        b.setBname(item.getString("utf-8"));
                        break;
                    case "bprice":
                        b.setBprice(Double.parseDouble(item.getString("utf-8")));
                        break;
                    case "bmark":
                        b.setBmark(item.getString("utf-8"));
                        break;
                    case "bstock":
                        b.setBstock(Integer.parseInt(item.getString("utf-8")));
                        break;
                    case "btid":
                        b.setBtid(Integer.parseInt(item.getString("utf-8")));
                        break;
                    case "bisbn":
                        b.setBisbn(item.getString("utf-8"));
                        break;
                    case "bauthor":
                        b.setBauthor(item.getString("utf-8"));
                        break;
                    case "bpublisher":
                        b.setBpublisher(item.getString("utf-8"));
                        break;
                }
                //表单提交的文件字段
            }else {
//                if(item.getInputStream().available()<=0)continue;
                String fileName = item.getName();
                fileName = fileName.substring(fileName.lastIndexOf("."));
                fileName = "/"+new Date().getTime()+fileName;//202301272006
                String path = request.getServletContext().getRealPath("/images")+fileName;

                item.write(new File(path));

                switch(item.getFieldName()) {
                    case "bcover":
                        b.setBcover("images"+fileName);
                        break;
                    case "bimage1":
                        b.setBimage1("images"+fileName);
                        break;
                    case "bimage2":
                        b.setBimage2("images"+fileName);
                        break;
                }
            }
        }
        bookService.addBook(b);

        return "redirect:/admin/book_list.action?rtype=0";
    }



    //展示修改页面
    @RequestMapping("/admin/book_edit_show.action")
    public String bookEditShow(int bid,HttpServletRequest request){

        Book book = bookService.queryBookBybid(bid);

        request.setAttribute("g",book);

        return "admin/book_edit";
    }



    //修改书籍
    @RequestMapping("/admin/book_update.action")
    public String bookUpdate(HttpServletRequest request) throws Exception {


        DiskFileItemFactory dfFactory = new DiskFileItemFactory();

        ServletFileUpload sfu = new ServletFileUpload(dfFactory);
        Book b = new Book();
        List<FileItem> fileItems = sfu.parseRequest(request);
        for (FileItem item : fileItems) {
            if(item.isFormField()) {
                switch(item.getFieldName()) {
                    case "bid":
                        b.setBid(Integer.parseInt(item.getString("utf-8")));
                    case "bname":
                        b.setBname(item.getString("utf-8"));
                        break;
                    case "bprice":
                        b.setBprice(Double.parseDouble(item.getString("utf-8")));
                        break;
                    case "bmark":
                        b.setBmark(item.getString("utf-8"));
                        break;
                    case "bstock":
                        b.setBstock(Integer.parseInt(item.getString("utf-8")));
                        break;
                    case "btid":
                        b.setBtid(Integer.parseInt(item.getString("utf-8")));
                        break;
                    case "bisbn":
                        b.setBisbn(item.getString("utf-8"));
                        break;
                    case "bauthor":
                        b.setBauthor(item.getString("utf-8"));
                        break;
                    case "bpublisher":
                        b.setBpublisher(item.getString("utf-8"));
                        break;
                }

                //表单提交的文件字段
            }else {
//                if(item.getInputStream().available()<=0)continue;
                String fileName = item.getName();
                fileName = fileName.substring(fileName.lastIndexOf("."));
                fileName = "/"+new Date().getTime()+fileName;//202301272006

                String path = request.getServletContext().getRealPath("/images")+fileName;

                item.write(new File(path));

                switch(item.getFieldName()) {
                    case "bcover":
                        b.setBcover("images"+fileName);
                        break;
                    case "bimage1":
                        b.setBimage1("images"+fileName);
                        break;
                    case "bimage2":
                        b.setBimage2("images"+fileName);
                        break;
                }
            }
        }
        bookService.modifyBook(b);

        return "redirect:/admin/book_list.action?rtype=0";
    }


    //删除图书
    @RequestMapping("/admin/book_delete.action")
    public String bookDelete(Integer bid){

        bookService.deleteBookByBid(bid);

        return "redirect:/admin/book_list.action?rtype=0";
    }



    //书籍类目
    @RequestMapping("/admin/type_list.action")
    public String typeList(HttpServletRequest request){

        List<BookType> bookTypeList = bookTypeService.queryBookTypes();
        request.setAttribute("list",bookTypeList);

        return "admin/type_list";
    }

    //添加类目
    @RequestMapping("/admin/type_add.action")
    public String typeAdd(HttpServletRequest request,String btname){

        boolean result = bookTypeService.addBookType(btname);

        if(result){
            request.setAttribute("msg","添加成功");
        }else {
            request.setAttribute("failMsg","添加失败");
        }
        //更新数据
        List<BookType> bookTypeList = bookTypeService.queryBookTypes();
        request.setAttribute("list",bookTypeList);

        return "/admin/type_list";
    }

    //类型修改
    @RequestMapping("/admin/book_updateType.action")
    public String bookUpdatetype(int btid,String btname,HttpServletRequest request){

        boolean result = bookTypeService.changebookTypeName(btid,btname);

        if(result){
            request.setAttribute("msg","修改成功");
        }else {
            request.setAttribute("failMsg","修改失败");
        }
        //更新数据
        List<BookType> bookTypeList = bookTypeService.queryBookTypes();
        request.setAttribute("list",bookTypeList);

        return "/admin/type_list";
    }


    //删除书的类型
    @RequestMapping("/admin/type_delete.action")
    public String DeletebookType(int btid ,HttpServletRequest request){

        boolean result = bookTypeService.deletbookType(btid);

        if(result){
            request.setAttribute("msg","删除成功");
        }else {
            request.setAttribute("failMsg","删除失败");
        }


        //更新数据
        List<BookType> bookTypeList = bookTypeService.queryBookTypes();
        request.setAttribute("list",bookTypeList);

        return "/admin/type_list";
    }


}
