package com.luochan.spark_web.controllers;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@EnableAutoConfiguration
public class pagesControllers {
    @RequestMapping("index")
    public String test() {
       return "redirect:/index.html";
    }

}
