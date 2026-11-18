package com.example.entity;
import lombok.Data;
import java.time.LocalDateTime;
@Data
public class Praise {
    private Integer id;
    private Integer articleId;
    private Integer userId;
    private LocalDateTime createTime;
}
