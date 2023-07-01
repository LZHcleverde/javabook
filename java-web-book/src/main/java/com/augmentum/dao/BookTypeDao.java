package com.augmentum.dao;

import com.augmentum.domain.Book;
import com.augmentum.domain.BookType;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author lzh
 * @createe 2023-01-20-17:49
 */
public interface BookTypeDao {

    List<BookType> queryBookTypes();//所有书籍类型

    String queryBookTypeNameByBookTypeId(@Param("btid") int btid); //根据id找对应书籍名称

    List<Book> queryBooksById(@Param("btid") Integer btid);    //根据类型id找书籍

    List<Book> queryBooksAll();     //所有书籍

    int insertBookTyep(@Param("btname") String btname); //添加类目

    int updateBookTypeName(@Param("btid") int btid, @Param("btname") String btname);    //更改书类型名

    int deleteBookByType(@Param("btid") int btid); //删除此类型的书

    int deleteType(@Param("btid") int btid);   //删除此类型
}
