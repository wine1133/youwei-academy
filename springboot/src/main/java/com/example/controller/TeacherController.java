package com.example.controller;
import com.example.common.Result;
import com.example.entity.Teacher;
import com.example.service.TeacherService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/teacher")
public class TeacherController {
 @Resource private TeacherService service;
 @PostMapping("/add") public Result add(@RequestBody Teacher x){service.add(x);return Result.success();}
 @DeleteMapping("/delete/{id}") public Result delete(@PathVariable Integer id){service.deleteById(id);return Result.success();}
 @PutMapping("/update") public Result update(@RequestBody Teacher x){service.updateById(x);return Result.success();}
 @GetMapping("/selectById/{id}") public Result selectById(@PathVariable Integer id){return Result.success(service.selectById(id));}
 @GetMapping("/selectAll") public Result selectAll(Teacher x){return Result.success(service.selectAll(x));}
 @GetMapping("/selectPage") public Result page(Teacher x,@RequestParam(defaultValue="1") Integer pageNum,@RequestParam(defaultValue="8") Integer pageSize){PageInfo<Teacher> p=service.selectPage(x,pageNum,pageSize);return Result.success(p);}
}
