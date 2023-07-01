package com.augmentum.service;

import com.augmentum.domain.Book;
import com.augmentum.domain.BookType;

import java.util.List;

/**
 * @author lzh
 * @createe 2023-01-20-12:13
 */
public interface BookService {

    List<Book> queryBookByRecommendType(Integer rtype,int pageNumber,int pageSize);//查询热销和新品

    List<Book> queryBooksAll();//查询所有书籍

    List<Book> queryBooksListById(int btid);//根据书籍类型id查询书籍

    List<Book> queryBookRecommendByType(int page, int size, int t);//根据类型查找热销和新品

    Book queryBookBybid(int bid);   //根据bid找书籍

    List<Book> searchBooksByKeyword(int pageNumber,Integer size,String keyword);    //搜索书籍

    List<Book> queryBooksByRtype(int rtype);    //通过类型查找对应的书籍

    void updateBookrtypeById(int bid, int rtype);   //新增rtype

    void deleteBookrtypeById(int bid,int rtype);  //删除

    void addBook(Book b);   //添加书籍

    void modifyBook(Book b);    //修改书籍

    void deleteBookByBid(int bid);   //删除书

}
