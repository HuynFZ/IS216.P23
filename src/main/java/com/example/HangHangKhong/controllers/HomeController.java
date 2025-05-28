package com.example.HangHangKhong.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class HomeController {
    @RequestMapping("")
    public String home()
    {
        return "login-res";
    }
    

}
