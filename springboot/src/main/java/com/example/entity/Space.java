package com.example.entity;
import lombok.Data;
import java.math.BigDecimal;
@Data
public class Space {
    private Integer id;
    private String name;
    private String address;
    private BigDecimal longitude;
    private BigDecimal latitude;
    private String description;
    private String openTime;
    private String cover;
    private String status;
}
