package com.example.entity;
import lombok.Data;
import java.time.LocalDateTime;
@Data
public class Borrow {
    private Integer id;
    private Integer bookId;
    private Integer userId;
    private LocalDateTime borrowTime;
    private LocalDateTime dueTime;
    private LocalDateTime returnTime;
    private String status;
    private String bookName;
    private String bookCover;
    private String userName;
    private String userAvatar;
}
