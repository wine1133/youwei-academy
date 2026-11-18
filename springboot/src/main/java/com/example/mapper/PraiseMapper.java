package com.example.mapper;
import com.example.entity.Praise;
import org.apache.ibatis.annotations.*;
public interface PraiseMapper {
    @Select("select * from praise where article_id=#{articleId} and user_id=#{userId}") Praise selectOne(@Param("articleId") Integer articleId,@Param("userId") Integer userId);
    @Insert("insert into praise(article_id,user_id) values(#{articleId},#{userId})") int insert(Praise praise);
    @Delete("delete from praise where article_id=#{articleId} and user_id=#{userId}") int deleteOne(@Param("articleId") Integer articleId,@Param("userId") Integer userId);
}
