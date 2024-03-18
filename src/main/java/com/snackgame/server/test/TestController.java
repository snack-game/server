package com.snackgame.server.test;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test")
    public void test() throws InterruptedException {
        Thread.sleep(15000);
    }
}
