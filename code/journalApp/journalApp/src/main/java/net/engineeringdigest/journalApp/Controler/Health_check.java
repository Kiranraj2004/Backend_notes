package net.engineeringdigest.journalApp.Controler;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Health_check {

    @GetMapping("/")
    public String healthcheck() {
        return " server running";
    }
}
