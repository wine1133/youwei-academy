package com.example.entity;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data
public class Submit {
    private Integer id;
    private Integer homeworkId;
    private Integer userId;
    private String file;
    private String content;
    private BigDecimal score;
    private String comment;
    private LocalDateTime submitTime;
    private String status;
    private String userName;
    private String homeworkTitle;
}
