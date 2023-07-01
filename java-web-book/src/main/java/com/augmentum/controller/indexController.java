package com.augmentum.controller;

import com.augmentum.domain.Book;
import com.augmentum.domain.BookType;
import com.augmentum.service.BookService;
import com.augmentum.service.BookTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author lzh
 * @createe 2023-01-20-12:11
 */

@Controller
public class indexController {

    //书籍service
    @Autowired
    private BookService bookService;

    //书籍类别service
    @Autowired
    private BookTypeService bookTypeService;


    //主页
    @RequestMapping("/index.action")
    public String Init(HttpServletRequest request){

        //精选推荐书籍
        List<Book> books = bookService.queryBookByRecommendType(1,1,6);
        request.setAttribute("scrollBook",books.get(0));

        //第二种推荐书籍
        books = bookService.queryBookByRecommendType(2,1,6);
        request.setAttribute("hotList",books);

        //第三种推荐书籍
        books = bookService.queryBookByRecommendType(3,1,8);
        request.setAttribute("newList",books);

        //书籍所有类型
        List<BookType> bookTypes = bookTypeService.queryBookTypes();
        request.getServletContext().setAttribute("bookTypes",bookTypes);

        return "index";
    }

    //书籍分类




}
