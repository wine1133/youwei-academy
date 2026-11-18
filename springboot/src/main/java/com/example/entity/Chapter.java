package com.example.entity;
import lombok.Data;
import java.time.LocalDateTime;
@Data
public class Chapter {
    private Integer id;
    private Integer bookId;
    private String title;
    private Integer sortNo;
    private String content;
    private LocalDateTime createTime;
}
