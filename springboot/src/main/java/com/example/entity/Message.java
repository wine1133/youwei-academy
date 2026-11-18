package com.example.entity;
import lombok.Data;
import java.time.LocalDateTime;
@Data
public class Message {
    private Integer id;
    private Integer userId;
    private String role;
    private String title;
    private String content;
    private String type;
    private Integer relatedId;
    private Integer isRead;
    private LocalDateTime createTime;
}
