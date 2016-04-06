package com.NoteBucket.controller;



import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by cansint on 05.04.2016.
 */
@Controller
public class HomeController {
    @RequestMapping(value="/")
    public String home(){
        return "atz2";
    }
}