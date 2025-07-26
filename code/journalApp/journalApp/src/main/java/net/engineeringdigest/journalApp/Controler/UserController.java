package net.engineeringdigest.journalApp.Controler;


import net.engineeringdigest.journalApp.Entity.User;
import net.engineeringdigest.journalApp.service.UserService;
import net.engineeringdigest.journalApp.service.WeatherServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    WeatherServices weatherServices;

    @GetMapping()
    public Optional<User> getById(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();
        return userService.getByName(userName);
    }

    @PutMapping
    public ResponseEntity<?> upadate(@RequestBody User userEntry){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();
        User userEntryInDb=userService.findByUserName(userName);
        if(userEntryInDb!=null){
            if(!userEntry.getUsername().isEmpty()) {
                userEntryInDb.setUsername(userEntry.getUsername());
            }
            userEntryInDb.setPassword(userEntry.getPassword());
            return userService.updateUser(userEntryInDb);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("user Not Found");

    }

    @DeleteMapping()
    public  ResponseEntity<?> deleteUserByName(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();
        return userService.deleteByName(userName);
    }
    @GetMapping("/greeting/{city}")
    public  ResponseEntity<?>greeting(@PathVariable String city){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();
        String greeting = "Hi " + userName + "!";
        double  weather = weatherServices.getWeather(city);
        return ResponseEntity.ok(greeting + " feels like Temp :" + weather);
    }










//    before spring security

//    @GetMapping
//    public ResponseEntity<?> getAllUsers() {
//        List<User> allEntry = userService.getAll();
//
//        if (allEntry.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No user entries found.");
//        }
//
//        return ResponseEntity.ok(allEntry);
//    }
//
//
//
//
//    @PutMapping("/{userName}")
//    public ResponseEntity<?> upadate(@PathVariable String userName, @RequestBody User userEntry){
//        User userEntryInDb=userService.findByUserName(userName);
//        if(userEntryInDb!=null){
//            if(!userEntry.getUsername().isEmpty()) {
//                userEntryInDb.setUsername(userEntry.getUsername());
//            }
//            userEntryInDb.setPassword(userEntry.getPassword());
//            userService.createEntry(userEntryInDb);
//            return ResponseEntity.ok(userEntryInDb);
//        }
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("user Not Found");
//
//    }
//
//    @GetMapping("/{id}")
//    public Optional<User> getById(@PathVariable ObjectId id){
//        return userService.getById(id);
//
//    }
//
//    @DeleteMapping("/{userName}")
//    public  ResponseEntity<?> deleteUserByName(@PathVariable String userName){
//        return userService.deleteById(userName);
//    }
}
