package kr.co.okheeokey.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {
    @GetMapping
    public String index() {
        return "Hello! Welcome to Lvlz1s!";
    }

    @GetMapping("/private")
    public String privateDataTest() {
        return "This data is private~!!";
    }
}
