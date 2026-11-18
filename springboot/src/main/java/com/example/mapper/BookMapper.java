package com.example.mapper;
import com.example.entity.Book;
import org.apache.ibatis.annotations.*;
import java.util.List;
public interface BookMapper {
    int insert(Book book);
    int updateById(Book book);
    @Delete("delete from book where id=#{id}") int deleteById(Integer id);
    @Select("select * from book where id=#{id}") Book selectById(Integer id);
    List<Book> selectAll(Book book);
    @Update("update book set stock=stock-1 where id=#{id} and stock>0") int decreaseStock(Integer id);
    @Update("update book set stock=stock+1 where id=#{id} and stock<total") int increaseStock(Integer id);
}
