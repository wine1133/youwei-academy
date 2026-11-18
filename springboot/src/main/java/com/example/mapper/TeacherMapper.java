package com.example.mapper;
import com.example.entity.Teacher;
import org.apache.ibatis.annotations.*;
import java.util.List;
public interface TeacherMapper {
    int insert(Teacher teacher);
    int updateById(Teacher teacher);
    @Delete("delete from `teacher` where id=#{id}") int deleteById(Integer id);
    @Select("select * from `teacher` where id=#{id}") Teacher selectById(Integer id);
    @Select("select * from `teacher` where username=#{username}") Teacher selectByUsername(String username);
    List<Teacher> selectAll(Teacher teacher);
}
