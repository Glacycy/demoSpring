package fr.digi.demospring2.controleurs;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class HelloControleur {

    @GetMapping
    public String hello() {
        return "Hello";
    }
}
