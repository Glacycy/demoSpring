package fr.digi.demospring2.controleurs;

import fr.digi.demospring2.services.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class HelloControleur {

    @Autowired
    private HelloService helloService;

//    private final HelloService helloService;
//
//    public HelloControleur(HelloService helloService) {
//        this.helloService = helloService;
//    }

    @GetMapping
    public String hello() {
        return helloService.salutation();
    }
}
