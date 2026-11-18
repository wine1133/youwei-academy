package com.example.mapper;
import com.example.entity.Article;
import com.example.entity.Collect;
import org.apache.ibatis.annotations.*;
import java.util.List;
public interface CollectMapper {
    @Select("select * from collect where article_id=#{articleId} and user_id=#{userId}") Collect selectOne(@Param("articleId") Integer articleId,@Param("userId") Integer userId);
    @Insert("insert into collect(article_id,user_id) values(#{articleId},#{userId})") int insert(Collect collect);
    @Delete("delete from collect where article_id=#{articleId} and user_id=#{userId}") int deleteOne(@Param("articleId") Integer articleId,@Param("userId") Integer userId);
    @Select("select a.*,u.name authorName,u.avatar authorAvatar from collect c join article a on c.article_id=a.id left join `user` u on a.user_id=u.id where c.user_id=#{userId} order by c.create_time desc") List<Article> selectArticlesByUser(Integer userId);
}
