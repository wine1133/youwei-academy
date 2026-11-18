package com.example.controller;
import com.example.common.Result;
import com.example.entity.Message;
import com.example.service.MessageService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
@RestController @RequestMapping("/message")
public class MessageController {
    @Resource private MessageService service;
    @GetMapping("/selectAll") public Result all(Message m){return Result.success(service.selectAll(m));}
    @GetMapping("/selectPage") public Result page(Message m,@RequestParam(defaultValue="1") Integer pageNum,@RequestParam(defaultValue="10") Integer pageSize){PageInfo<Message> p=service.selectPage(m,pageNum,pageSize);return Result.success(p);}
    @GetMapping("/unreadCount") public Result unread(@RequestParam Integer userId,@RequestParam String role){return Result.success(Map.of("count",service.unreadCount(userId,role)));}
    @PutMapping("/read/{id}") public Result read(@PathVariable Integer id){service.markRead(id);return Result.success();}
    @PutMapping("/readAll") public Result readAll(@RequestParam Integer userId,@RequestParam String role){service.markAllRead(userId,role);return Result.success();}
    @DeleteMapping("/delete/{id}") public Result delete(@PathVariable Integer id){service.delete(id);return Result.success();}
    @PostMapping("/send") public Result send(@RequestBody Message m){service.sendToUser(m.getUserId(),m.getRole(),m.getTitle(),m.getContent(),m.getType(),m.getRelatedId());return Result.success();}
    @PostMapping("/sendRole") public Result sendRole(@RequestBody Message m){service.sendToRole(m.getRole(),m.getTitle(),m.getContent(),m.getType(),m.getRelatedId());return Result.success();}
}
