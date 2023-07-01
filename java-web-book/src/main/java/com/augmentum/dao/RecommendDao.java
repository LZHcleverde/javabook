package com.augmentum.dao;

import com.augmentum.domain.Book;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author lzh
 * @createe 2023-01-20-17:32
 */
public interface RecommendDao {

    List<Book> queryBooks(@Param(value = "rtype") Integer rtype);
}
