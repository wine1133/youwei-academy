package com.example.controller;
import com.example.common.Result;
import com.example.entity.Space;
import com.example.service.SpaceService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/space")
public class SpaceController {
    @Resource private SpaceService service;
    @GetMapping("/selectAll") public Result all(){return Result.success(service.selectAll());}
    @PostMapping("/add") public Result add(@RequestBody Space s){service.add(s);return Result.success();}
    @PutMapping("/update") public Result update(@RequestBody Space s){service.update(s);return Result.success();}
    @DeleteMapping("/delete/{id}") public Result delete(@PathVariable Integer id){service.delete(id);return Result.success();}
}
