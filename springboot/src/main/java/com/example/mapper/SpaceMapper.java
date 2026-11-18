package com.example.mapper;
import com.example.entity.Space;
import org.apache.ibatis.annotations.*;
import java.util.List;
public interface SpaceMapper {
    @Select("select * from space order by id") List<Space> selectAll();
    int insert(Space space);
    int updateById(Space space);
    @Delete("delete from space where id=#{id}") int deleteById(Integer id);
}
