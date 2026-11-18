package com.example.mapper;
import com.example.entity.Article;
import org.apache.ibatis.annotations.*;
import java.util.List;
public interface ArticleMapper {
    int insert(Article article);
    int updateById(Article article);
    @Delete("delete from article where id=#{id}") int deleteById(Integer id);
    Article selectById(Integer id);
    List<Article> selectAll(Article article);
    @Update("update article set views=views+1 where id=#{id}") int increaseViews(Integer id);
    @Update("update article set likes=greatest(0,likes+#{delta}) where id=#{articleId}") int changeLikes(@Param("articleId") Integer articleId,@Param("delta") Integer delta);
    @Update("update article set collects=greatest(0,collects+#{delta}) where id=#{articleId}") int changeCollects(@Param("articleId") Integer articleId,@Param("delta") Integer delta);
    @Select("select count(*) from article") int countAll();
}
