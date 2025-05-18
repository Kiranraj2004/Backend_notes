package net.engineeringdigest.journalApp.Controler;


import net.engineeringdigest.journalApp.Entity.UserEntry;
import net.engineeringdigest.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        List<UserEntry> allEntry = userService.getAll();

        if (allEntry.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No user entries found.");
        }

        return ResponseEntity.ok(allEntry);
    }


    @PostMapping
    public UserEntry createUser(@RequestBody UserEntry userEntry){
        return userService.createEntry(userEntry);
    }

    @PutMapping("/{userName}")
    public ResponseEntity<?> upadate(@PathVariable String userName, @RequestBody UserEntry userEntry){
        UserEntry userEntryInDb=userService.findByUserName(userName);
        if(userEntryInDb!=null){
            if(!userEntry.getUsername().isEmpty()) {
                userEntryInDb.setUsername(userEntry.getUsername());
            }
            userEntryInDb.setPassword(userEntry.getPassword());
            userService.createEntry(userEntryInDb);
            return ResponseEntity.ok(userEntryInDb);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("user Not Found");

    }

    @GetMapping("/{id}")
    public Optional<UserEntry> getById(@PathVariable ObjectId id){
        return userService.getById(id);

    }

    @DeleteMapping("/{userName}")
    public  ResponseEntity<?> deleteUserByName(@PathVariable String userName){
        return userService.deleteById(userName);
    }
}
