package com.example.controller;
import com.example.common.Result;
import com.example.entity.Homework;
import com.example.service.HomeworkService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/homework")
public class HomeworkController {
 @Resource private HomeworkService service;
 @PostMapping("/add") public Result add(@RequestBody Homework x){service.add(x);return Result.success();}
 @DeleteMapping("/delete/{id}") public Result delete(@PathVariable Integer id){service.deleteById(id);return Result.success();}
 @PutMapping("/update") public Result update(@RequestBody Homework x){service.updateById(x);return Result.success();}
 @GetMapping("/selectById/{id}") public Result selectById(@PathVariable Integer id){return Result.success(service.selectById(id));}
 @GetMapping("/selectAll") public Result selectAll(Homework x){return Result.success(service.selectAll(x));}
 @GetMapping("/selectPage") public Result page(Homework x,@RequestParam(defaultValue="1") Integer pageNum,@RequestParam(defaultValue="8") Integer pageSize){PageInfo<Homework> p=service.selectPage(x,pageNum,pageSize);return Result.success(p);}
}
