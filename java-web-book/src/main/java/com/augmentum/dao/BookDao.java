package com.augmentum.dao;

import com.augmentum.domain.Book;
import com.augmentum.domain.BookType;
import com.augmentum.domain.Recommend;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author lzh
 * @createe 2023-01-20-12:14
 */
public interface BookDao {


    List<Book> queryBooksAll();//查询所有书


    List<Book> queryBooksListById(@Param("btid") int btid);//查询此类别所有书籍

    Book queryBookBybid(@Param("bid") int bid);   //根据id找书籍

    List<Book> searchBooksByKeyword(@Param("keyword") String keyword);    //搜索书籍

    List<Book> queryBooksByRtype(@Param("rtype") int rtype);    //通过类型查找书籍

    BookType queryBookTypeName(@Param("bid") Integer bid, @Param("btid") Integer btid);      //获取类型名

    Recommend queryBooksrtypeByBid(@Param("bid") Integer bid);  //查询r_type

    void insertBookrtypeById(@Param("bid") int bid,@Param("rtype") int rtype);   //新增推荐

    void deleteBookrtypeById(@Param("bid") int bid,@Param("rtype") int rtype);  //删除推荐

    int querybookrecommentBeing(@Param("bid") int bid);   //判断是否是其他的推荐

    void updatebookrecommend(@Param("bid") int bid, @Param("rtype") int rtype);   //修改r_type

    void addBook(Book b);   //添加书籍

    void updateBook(Book b);    //修改书籍

    void deleteBook(@Param("bid") int bid);   //删除书籍

    void deleteBookrtypeByid(@Param("bid") int bid);  //通过id删除推荐

    int updateOrderOstatusByOid(@Param("oid") String oid,@Param("ostatus") Integer ostatus);    //更改书籍状态
}
