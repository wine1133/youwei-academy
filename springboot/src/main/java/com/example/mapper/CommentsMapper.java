package com.example.mapper;
import com.example.entity.Comments;
import org.apache.ibatis.annotations.*;
import java.util.List;
public interface CommentsMapper {
    int insert(Comments comments);
    @Delete("delete from comments where id=#{id}") int deleteById(Integer id);
    @Select("select c.*,u.name userName,u.avatar from comments c left join `user` u on c.user_id=u.id where c.article_id=#{articleId} order by c.create_time desc") List<Comments> selectByArticle(Integer articleId);
    @Select("select c.*,u.name userName,u.avatar,a.title articleTitle from comments c left join `user` u on c.user_id=u.id left join article a on c.article_id=a.id order by c.create_time desc") List<Comments> selectAll();
    @Select("select count(*) from comments") int countAll();
}
