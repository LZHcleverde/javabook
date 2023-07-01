package com.augmentum.service.impl;

import com.augmentum.dao.BookDao;
import com.augmentum.dao.RecommendDao;
import com.augmentum.domain.Book;
import com.augmentum.domain.BookType;
import com.augmentum.domain.Recommend;
import com.augmentum.service.BookService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lzh
 * @createe 2023-01-20-12:14
 */

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookDao bookDao;

    @Autowired
    private RecommendDao recommendDao;


    @Override
    public List<Book> queryBookByRecommendType(Integer rtype, int pageNumber, int pageSize) {

        PageHelper.startPage(pageNumber,pageSize);

        return recommendDao.queryBooks(rtype);

    }

    @Override
    public List<Book> queryBooksAll() {

        PageHelper.startPage(1,8);

        return bookDao.queryBooksAll();
    }

    @Override
    public List<Book> queryBooksListById(int btid) {

        PageHelper.startPage(1,8);

        return bookDao.queryBooksListById(btid);
    }

    @Override
    public List<Book> queryBookRecommendByType(int page, int size, int t) {
        PageHelper.startPage(page,size);
        return recommendDao.queryBooks(t);
    }

    @Override
    public Book queryBookBybid(int bid) {

        return bookDao.queryBookBybid(bid);
    }


    @Override
    public List<Book> searchBooksByKeyword(int page,Integer size,String keyword) {

        PageHelper.startPage(page,size);

        return bookDao.searchBooksByKeyword(keyword);
    }

    @Override
    public List<Book> queryBooksByRtype(int rtype) {

        List<Book> bookList = new ArrayList<>();
        bookList = bookDao.queryBooksByRtype(rtype);

        //查找所属类目
        for (Book book : bookList) {
            BookType bookTypeName = bookDao.queryBookTypeName(book.getBid(), book.getBtid());
            book.setBtname(bookTypeName.getBtname());

            if (rtype == 1) {
                book.setScroll(true);
            } else if (rtype == 2) {
                book.setHot(true);
            } else if (rtype == 3) {
                book.setNew(true);
            } else if (rtype == 0) {
                Recommend recommend = bookDao.queryBooksrtypeByBid(book.getBid());
                if(recommend == null){
                    continue;
                }
                if (recommend.getRtype() == 1) {
                    book.setScroll(true);
                } else if (recommend.getRtype() == 2) {
                    book.setHot(true);
                } else if (recommend.getRtype() == 3) {
                    book.setNew(true);

                }
            }

        }
        return bookList;
    }

    @Override
    public void updateBookrtypeById(int bid, int rtype) {

        int result = bookDao.querybookrecommentBeing(bid);
        if(result == 1){
            bookDao.updatebookrecommend(bid,rtype);
        }else {
            //新增
            bookDao.insertBookrtypeById(bid,rtype);
        }

    }

    @Override
    public void deleteBookrtypeById(int bid,int rtype) {

        bookDao.deleteBookrtypeById(bid,rtype);
    }

    @Override
    public void addBook(Book b) {
        bookDao.addBook(b);
    }

    @Override
    public void modifyBook(Book b) {
        bookDao.updateBook(b);
    }

    @Override
    public void deleteBookByBid(int bid) {

        //删除书籍的推荐
        bookDao.deleteBookrtypeByid(bid);
        //删除书籍
        bookDao.deleteBook(bid);
    }



}
