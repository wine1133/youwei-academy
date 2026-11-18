package com.example.controller;
import com.example.common.Result;
import com.example.entity.Book;
import com.example.service.BookService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/book")
public class BookController {
 @Resource private BookService service;
 @PostMapping("/add") public Result add(@RequestBody Book x){service.add(x);return Result.success();}
 @DeleteMapping("/delete/{id}") public Result delete(@PathVariable Integer id){service.deleteById(id);return Result.success();}
 @PutMapping("/update") public Result update(@RequestBody Book x){service.updateById(x);return Result.success();}
 @GetMapping("/selectById/{id}") public Result selectById(@PathVariable Integer id){return Result.success(service.selectById(id));}
 @GetMapping("/selectAll") public Result selectAll(Book x){return Result.success(service.selectAll(x));}
 @GetMapping("/selectPage") public Result page(Book x,@RequestParam(defaultValue="1") Integer pageNum,@RequestParam(defaultValue="8") Integer pageSize){PageInfo<Book> p=service.selectPage(x,pageNum,pageSize);return Result.success(p);}
}
