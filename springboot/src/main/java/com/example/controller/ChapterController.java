package com.example.controller;
import com.example.common.Result;
import com.example.entity.Chapter;
import com.example.service.ChapterService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/chapter")
public class ChapterController {
 @Resource private ChapterService service;
 @PostMapping("/add") public Result add(@RequestBody Chapter x){service.add(x);return Result.success();}
 @DeleteMapping("/delete/{id}") public Result delete(@PathVariable Integer id){service.deleteById(id);return Result.success();}
 @PutMapping("/update") public Result update(@RequestBody Chapter x){service.updateById(x);return Result.success();}
 @GetMapping("/selectById/{id}") public Result selectById(@PathVariable Integer id){return Result.success(service.selectById(id));}
 @GetMapping("/selectByBookId/{bookId}") public Result list(@PathVariable Integer bookId){return Result.success(service.selectByBookId(bookId));}
}
