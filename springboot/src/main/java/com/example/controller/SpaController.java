package com.example.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class SpaController {
    @GetMapping({"/front/**","/manager/**","/login","/register"})
    public String forward(){return "forward:/index.html";}
}
