package com.example.mapper;
import com.example.entity.Homework;
import org.apache.ibatis.annotations.*;
import java.util.List;
public interface HomeworkMapper {
    int insert(Homework homework);
    int updateById(Homework homework);
    @Delete("delete from homework where id=#{id}") int deleteById(Integer id);
    @Select("select * from homework where id=#{id}") Homework selectById(Integer id);
    List<Homework> selectAll(Homework homework);
    @Select("select count(*) from homework") int countAll();
}
