package com.example.controller;
import com.example.common.Result;
import com.example.service.DashboardService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/dashboard")
public class DashboardController {
 @Resource private DashboardService service;
 @GetMapping("/stats") public Result stats(){return Result.success(service.stats());}
}
