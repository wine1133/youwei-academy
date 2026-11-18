package com.example.mapper;
import com.example.entity.Message;
import org.apache.ibatis.annotations.*;
import java.util.List;
public interface MessageMapper {
    int insert(Message message);
    int insertToRole(@Param("role") String role,@Param("title") String title,@Param("content") String content,@Param("type") String type,@Param("relatedId") Integer relatedId);
    List<Message> selectAll(Message query);
    @Select("select count(*) from message where user_id=#{userId} and role=#{role} and is_read=0") int countUnread(@Param("userId") Integer userId,@Param("role") String role);
    @Update("update message set is_read=1 where id=#{id}") int markRead(Integer id);
    @Update("update message set is_read=1 where user_id=#{userId} and role=#{role}") int markAllRead(@Param("userId") Integer userId,@Param("role") String role);
    @Delete("delete from message where id=#{id}") int deleteById(Integer id);
}
