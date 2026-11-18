package com.example.controller;
import com.example.common.Result;
import com.example.entity.Account;
import com.example.exception.CustomException;
import com.example.service.AdminService;
import com.example.service.TeacherService;
import com.example.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
@RestController
public class WebController {
    @Resource private AdminService adminService;
    @Resource private UserService userService;
    @Resource private TeacherService teacherService;
    @GetMapping("/health") public Result hello(){return Result.success();}
    @PostMapping("/login")
    public Result login(@RequestBody Account account){
        if("ADMIN".equals(account.getRole())) return Result.success(adminService.login(account));
        if("USER".equals(account.getRole())) return Result.success(userService.login(account));
        if("TEACHER".equals(account.getRole())) return Result.success(teacherService.login(account));
        throw new CustomException("请选择正确的登录角色");
    }
    @PostMapping("/register")
    public Result register(@RequestBody Account account){
        if("USER".equals(account.getRole())) userService.register(account);
        else if("TEACHER".equals(account.getRole())) teacherService.register(account);
        else throw new CustomException("当前角色不支持注册");
        return Result.success();
    }
    @PutMapping("/updatePassword")
    public Result updatePassword(@RequestBody Account account){
        if("ADMIN".equals(account.getRole())) adminService.updatePassword(account);
        else if("USER".equals(account.getRole())) userService.updatePassword(account);
        else if("TEACHER".equals(account.getRole())) teacherService.updatePassword(account);
        else throw new CustomException("当前角色不支持修改密码");
        return Result.success();
    }
}

