package net.engineeringdigest.journalApp.Controler;

import net.engineeringdigest.journalApp.Entity.JournalEntry;
import net.engineeringdigest.journalApp.Entity.User;
import net.engineeringdigest.journalApp.service.JournalEntryService;
import net.engineeringdigest.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController

@RequestMapping("/journal")
public class JournalEntryControllerV2 {
    @Autowired
    private JournalEntryService journalEntryService;
    @Autowired
    private UserService userService;

//    this code is having authentication

    @PostMapping()
    public ResponseEntity<?> createEntry(@RequestBody JournalEntry obj){
        Authentication auth=SecurityContextHolder.getContext().getAuthentication();
        String userName=auth.getName();

        return journalEntryService.createEntry(obj,userName);
    }



    @GetMapping()
    public ResponseEntity<?> getAllJournalEntriesOfUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();
        User user = userService.findByUserName(userName);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        return ResponseEntity.ok(user.getJournalEntries());
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateOne(@PathVariable ObjectId id, @RequestBody JournalEntry obj) {
        return journalEntryService.update(id,obj);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteone(@PathVariable ObjectId id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();
        return journalEntryService.deleteById(id, userName);
    }




//     this code was basically without authentication
//    // GET all entries  journal entry
//    @GetMapping
//    public List<JournalEntry>getAllJournal(){
//
//        return journalEntryService.getAll();
//
//    }
//
//    // GET all entries of particular user entry
//    @GetMapping("/{userName}")
//    public ResponseEntity<?> getAllJournalEntriesOfUser(@PathVariable String userName) {
//        User user = userService.findByUserName(userName);
//        if (user == null) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
//        }
//        return ResponseEntity.ok(user.getJournalEntries());
//    }
//
//
//
//    //    Post Method
//    @PostMapping("/{userName}")
//    public ResponseEntity<?> createEntry(@PathVariable String userName,@RequestBody JournalEntry obj){
//        return journalEntryService.createEntry(obj,userName);
//    }
//
//    @GetMapping("id/{id}")
//    public ResponseEntity<?> getById(@PathVariable("id") ObjectId id ){
//        Optional<JournalEntry> byId = journalEntryService.getById(id);
//        if(byId.isPresent()){
//            return ResponseEntity.ok(byId.get());
//        }
//        return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("journal not found");
//    }
//
//
//
//
//    @PutMapping("/{id}")
//    public ResponseEntity<?> updateOne(@PathVariable ObjectId id, @RequestBody JournalEntry obj) {
//        return journalEntryService.update(id,obj);
//    }
//
//
////    deleting the particular journal of user
//    @DeleteMapping("/{userName}/{id}")
//    public ResponseEntity<?> deleteone(@PathVariable ObjectId id,@PathVariable String userName){
//
//        return journalEntryService.deleteById(id,userName);
//    }



}
