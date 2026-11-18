package com.example.mapper;
import com.example.entity.Chapter;
import org.apache.ibatis.annotations.*;
import java.util.List;
public interface ChapterMapper {
    int insert(Chapter chapter);
    int updateById(Chapter chapter);
    @Delete("delete from chapter where id=#{id}") int deleteById(Integer id);
    @Select("select * from chapter where id=#{id}") Chapter selectById(Integer id);
    @Select("select * from chapter where book_id=#{bookId} order by sort_no,id") List<Chapter> selectByBookId(Integer bookId);
    @Select("select count(*) from chapter where book_id=#{bookId}") int countByBookId(Integer bookId);
}
