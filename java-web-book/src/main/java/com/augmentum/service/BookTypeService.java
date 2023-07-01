package com.augmentum.service;

import com.augmentum.domain.Book;
import com.augmentum.domain.BookType;

import java.util.List;

/**
 * @author lzh
 * @createe 2023-01-20-17:41
 */


public interface BookTypeService {


    List<BookType> queryBookTypes();    //所有书籍类型

    String queryBookTypeNameByBookTypeId(int btid);     //根据类型id查找名称

    List<Book> queryBooksById(int page, int size, Integer btid);    //根据类型id找书籍

    List<Book> queryBooksAll(int page, int size);   //所有书籍

    boolean addBookType(String btname);     //添加类目

    boolean changebookTypeName(int btid, String btname);    //修改数类型名称

    boolean deletbookType(int btid);    //删除书籍类型
}
