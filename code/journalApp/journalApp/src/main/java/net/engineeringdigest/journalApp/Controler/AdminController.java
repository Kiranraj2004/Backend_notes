package net.engineeringdigest.journalApp.Controler;


import net.engineeringdigest.journalApp.Entity.User;
import net.engineeringdigest.journalApp.service.JournalEntryService;
import net.engineeringdigest.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private UserService userService;

    @Autowired
    private JournalEntryService journalEntryService;

   @GetMapping("/get-all")
    public ResponseEntity<?>getall() {
       List<User> users = userService.getAll();
       if (users != null && !users.isEmpty()) {
           return ResponseEntity.ok(users);
       }
       return new ResponseEntity<>(HttpStatus.NOT_FOUND);

   }

   @PostMapping("/create-admin")
    public ResponseEntity<?> createAdmin(@RequestBody  User user){
       return userService.createAdmin(user);
   }

   @GetMapping("/get-All-Journals")
    public ResponseEntity<?> getAllJournals(){
       return  ResponseEntity.ok(journalEntryService.getAll());
   }

}
