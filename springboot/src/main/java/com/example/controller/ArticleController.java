package com.example.controller;
import com.example.common.Result;
import com.example.entity.Article;
import com.example.entity.Comments;
import com.example.service.ArticleService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/article")
public class ArticleController {
 @Resource private ArticleService service;
 @PostMapping("/add") public Result add(@RequestBody Article x){service.add(x);return Result.success();}
 @DeleteMapping("/delete/{id}") public Result delete(@PathVariable Integer id){service.deleteById(id);return Result.success();}
 @PutMapping("/update") public Result update(@RequestBody Article x){service.updateById(x);return Result.success();}
 @GetMapping("/selectById/{id}") public Result detail(@PathVariable Integer id){return Result.success(service.detail(id));}
 @GetMapping("/selectAll") public Result all(Article x){return Result.success(service.selectAll(x));}
 @GetMapping("/selectPage") public Result page(Article x,@RequestParam(defaultValue="1") Integer pageNum,@RequestParam(defaultValue="8") Integer pageSize){PageInfo<Article> p=service.selectPage(x,pageNum,pageSize);return Result.success(p);}
 @PostMapping("/like") public Result like(@RequestParam Integer articleId,@RequestParam Integer userId){return Result.success(service.toggleLike(articleId,userId));}
 @GetMapping("/liked") public Result liked(@RequestParam Integer articleId,@RequestParam Integer userId){return Result.success(service.liked(articleId,userId));}
 @PostMapping("/collect") public Result collect(@RequestParam Integer articleId,@RequestParam Integer userId){return Result.success(service.toggleCollect(articleId,userId));}
 @GetMapping("/collected") public Result collected(@RequestParam Integer articleId,@RequestParam Integer userId){return Result.success(service.collected(articleId,userId));}
 @GetMapping("/myCollect/{userId}") public Result myCollect(@PathVariable Integer userId){return Result.success(service.myCollects(userId));}
 @GetMapping("/comments/{articleId}") public Result comments(@PathVariable Integer articleId){return Result.success(service.comments(articleId));}
 @PostMapping("/comment") public Result addComment(@RequestBody Comments x){service.addComment(x);return Result.success();}
 @DeleteMapping("/comment/{id}") public Result deleteComment(@PathVariable Integer id){service.deleteComment(id);return Result.success();}
 @GetMapping("/comments/all") public Result allComments(){return Result.success(service.allComments());}
}
