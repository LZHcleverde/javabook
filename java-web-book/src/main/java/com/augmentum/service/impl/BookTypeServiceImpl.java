package com.augmentum.service.impl;

import com.augmentum.dao.BookTypeDao;
import com.augmentum.domain.Book;
import com.augmentum.domain.BookType;
import com.augmentum.service.BookTypeService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lzh
 * @createe 2023-01-20-17:42
 */

@Service
public class BookTypeServiceImpl implements BookTypeService {

    @Autowired
    private BookTypeDao bookTypeDao;


    @Override
    public List<BookType> queryBookTypes() {

        return bookTypeDao.queryBookTypes();
    }

    @Override
    public String queryBookTypeNameByBookTypeId(int btid) {

        return bookTypeDao.queryBookTypeNameByBookTypeId(btid);
    }

    @Override
    public List<Book> queryBooksById(int page, int size, Integer btid) {

        PageHelper.startPage(page,size);

        return bookTypeDao.queryBooksById(btid);
    }

    @Override
    public List<Book> queryBooksAll(int page, int size) {

        PageHelper.startPage(page,size);
        return bookTypeDao.queryBooksAll();
    }

    @Override
    public boolean addBookType(String btname) {

        int result = bookTypeDao.insertBookTyep(btname);

        if(result == 1){
            return true;
        }else {
            return false;
        }

    }

    @Override
    public boolean changebookTypeName(int btid, String btname) {

        int result = bookTypeDao.updateBookTypeName(btid,btname);
        if(result == 1){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public boolean deletbookType(int btid) {

        //删除此类型的书籍
        int resultBook = bookTypeDao.deleteBookByType(btid);

        //删除此类型
        int resultType =  bookTypeDao.deleteType(btid);

        if(resultType == 1){
            return true;
        }else {
            return false;
        }
    }
}
