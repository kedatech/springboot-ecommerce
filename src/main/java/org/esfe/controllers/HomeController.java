package org.esfe.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping ("/")
public class HomeController {
    @GetMapping
    public  String index(){
        return "home/index";
    }

    // controlador healthcheck
    @GetMapping("/healthcheck")
    @ResponseBody
    public Object healthcheck(){
        Object response = new Object(){
            public String status = "ok";
        };

        return response;
    }
}
