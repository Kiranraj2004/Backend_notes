package net.engineeringdigest.journalApp.Controler;

import net.engineeringdigest.journalApp.Entity.User;
import net.engineeringdigest.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// for this incoming request do not requeries authentication
@RestController
@RequestMapping("/public")
public class PublicControler {
    @Autowired
    private UserService userService;

    @PostMapping("/user-creation")
    public ResponseEntity<?> createUser(@RequestBody User userEntry){
        return userService.createEntry(userEntry);
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthcheck() {
        return ResponseEntity.ok("server running");
    }

}
