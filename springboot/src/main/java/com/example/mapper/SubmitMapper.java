package com.example.mapper;
import com.example.entity.Submit;
import org.apache.ibatis.annotations.*;
import java.util.List;
public interface SubmitMapper {
    int insert(Submit submit);
    int grade(Submit submit);
    @Select("select * from submit where id=#{id}") Submit selectById(Integer id);
    @Delete("delete from submit where id=#{id}") int deleteById(Integer id);
    List<Submit> selectAll(Submit submit);
    @Select("select count(*) from submit where homework_id=#{homeworkId} and user_id=#{userId}") int countByHomeworkAndUser(@Param("homeworkId") Integer homeworkId,@Param("userId") Integer userId);
    @Select("select count(*) from submit") int countAll();
}

