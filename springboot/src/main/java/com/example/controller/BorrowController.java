package com.example.controller;
import com.example.common.Result;
import com.example.entity.Borrow;
import com.example.service.BorrowService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/borrow")
public class BorrowController {
 @Resource private BorrowService service;
 @PostMapping("/add") public Result add(@RequestBody Borrow x){service.borrow(x);return Result.success();}
 @PutMapping("/return/{id}") public Result returnBook(@PathVariable Integer id){service.returnBook(id);return Result.success();}
 @GetMapping("/selectAll") public Result selectAll(Borrow x){return Result.success(service.selectAll(x));}
 @GetMapping("/selectPage") public Result page(Borrow x,@RequestParam(defaultValue="1") Integer pageNum,@RequestParam(defaultValue="8") Integer pageSize){PageInfo<Borrow> p=service.selectPage(x,pageNum,pageSize);return Result.success(p);}
}
