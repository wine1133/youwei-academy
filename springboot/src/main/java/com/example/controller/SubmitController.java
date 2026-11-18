package com.example.controller;
import com.example.common.Result;
import com.example.entity.Submit;
import com.example.service.SubmitService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/submit")
public class SubmitController {
 @Resource private SubmitService service;
 @PostMapping("/add") public Result add(@RequestBody Submit x){service.add(x);return Result.success();}
 @PutMapping("/grade") public Result grade(@RequestBody Submit x){service.grade(x);return Result.success();}
 @DeleteMapping("/delete/{id}") public Result delete(@PathVariable Integer id){service.deleteById(id);return Result.success();}
 @GetMapping("/selectAll") public Result all(Submit x){return Result.success(service.selectAll(x));}
 @GetMapping("/selectPage") public Result page(Submit x,@RequestParam(defaultValue="1") Integer pageNum,@RequestParam(defaultValue="8") Integer pageSize){PageInfo<Submit> p=service.selectPage(x,pageNum,pageSize);return Result.success(p);}
}
