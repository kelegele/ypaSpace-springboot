package com.kelegele.ypaSpace.controller;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@Component
public class HelloDemo {

    @GetMapping("/")
    public RedirectView api() {
        RedirectView redirectTarget = new RedirectView();
        redirectTarget.setContextRelative(true);
        redirectTarget.setUrl("/swagger-ui.html");
        return redirectTarget;
    }

    @GetMapping("/hello")
    public String hello() {
        return "hello world!";
    }
}
