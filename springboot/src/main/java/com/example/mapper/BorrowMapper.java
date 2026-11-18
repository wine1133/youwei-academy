package com.example.mapper;
import com.example.entity.Borrow;
import org.apache.ibatis.annotations.*;
import java.util.List;
public interface BorrowMapper {
    int insert(Borrow borrow);
    int returnBook(Borrow borrow);
    @Select("select * from borrow where id=#{id}") Borrow selectById(Integer id);
    List<Borrow> selectAll(Borrow borrow);
    @Select("select count(*) from borrow where user_id=#{userId} and status='BORROWED'") int countActiveByUserId(Integer userId);
    @Select("select count(*) from borrow where user_id=#{userId} and book_id=#{bookId} and status='BORROWED'") int countActiveByUserAndBook(@Param("userId") Integer userId,@Param("bookId") Integer bookId);
    @Select("select count(*) from borrow where status='BORROWED'") int countActive();
}

