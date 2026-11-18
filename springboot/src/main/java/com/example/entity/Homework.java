package com.example.entity;
import lombok.Data;
import java.time.LocalDateTime;
@Data
public class Homework {
    private Integer id;
    private String title;
    private String content;
    private String file;
    private Integer teacherId;
    private LocalDateTime deadline;
    private LocalDateTime createTime;
    private String teacherName;
    private Integer submitCount;
}
