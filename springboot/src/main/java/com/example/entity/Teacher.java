package com.example.entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(callSuper = true)
public class Teacher extends Account {
    private Integer id;
    private String username;
    private String password;
    private String name;
    private String avatar;
    private String role;
}
