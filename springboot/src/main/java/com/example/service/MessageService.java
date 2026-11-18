package com.example.service;
import com.example.entity.Message;
import com.example.mapper.MessageMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class MessageService {
    @Resource private MessageMapper messageMapper;
    public void sendToUser(Integer userId,String role,String title,String content,String type,Integer relatedId){Message m=new Message();m.setUserId(userId);m.setRole(role);m.setTitle(title);m.setContent(content);m.setType(type);m.setRelatedId(relatedId);messageMapper.insert(m);}
    public void sendToRole(String role,String title,String content,String type,Integer relatedId){messageMapper.insertToRole(role,title,content,type,relatedId);}
    public List<Message> selectAll(Message m){return messageMapper.selectAll(m);}
    public PageInfo<Message> selectPage(Message m,Integer p,Integer s){PageHelper.startPage(p,s);return PageInfo.of(messageMapper.selectAll(m));}
    public int unreadCount(Integer userId,String role){return messageMapper.countUnread(userId,role);}
    public void markRead(Integer id){messageMapper.markRead(id);}
    public void markAllRead(Integer userId,String role){messageMapper.markAllRead(userId,role);}
    public void delete(Integer id){messageMapper.deleteById(id);}
}
