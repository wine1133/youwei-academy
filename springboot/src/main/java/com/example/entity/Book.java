package com.example.entity;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data
public class Book {
    private Integer id;
    private String name;
    private String author;
    private String cover;
    private String category;
    private String publisher;
    private String isbn;
    private String description;
    private Integer total;
    private Integer stock;
    private BigDecimal score;
    private String status;
    private LocalDateTime createTime;
}
