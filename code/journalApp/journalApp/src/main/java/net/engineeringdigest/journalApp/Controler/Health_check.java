package net.engineeringdigest.journalApp.Controler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Health_check {

    @GetMapping("/")
    public ResponseEntity<String> healthcheck() {
        return ResponseEntity.ok("server running");
    }
}

