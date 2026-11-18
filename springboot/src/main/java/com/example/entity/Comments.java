package com.example.entity;
import lombok.Data;
import java.time.LocalDateTime;
@Data
public class Comments {
    private Integer id;
    private Integer articleId;
    private Integer userId;
    private String content;
    private LocalDateTime createTime;
    private String userName;
    private String avatar;
}
