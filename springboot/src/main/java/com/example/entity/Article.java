package com.example.entity;
import lombok.Data;
import java.time.LocalDateTime;
@Data
public class Article {
    private Integer id;
    private String title;
    private String summary;
    private String cover;
    private String content;
    private String category;
    private String tags;
    private Integer userId;
    private Integer views;
    private Integer likes;
    private Integer collects;
    private String status;
    private LocalDateTime createTime;
    private String authorName;
    private String authorAvatar;
}
