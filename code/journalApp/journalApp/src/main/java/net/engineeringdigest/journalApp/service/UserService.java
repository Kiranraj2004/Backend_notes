package net.engineeringdigest.journalApp.service;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import net.engineeringdigest.journalApp.Entity.JournalEntry;
import net.engineeringdigest.journalApp.Entity.User;
import net.engineeringdigest.journalApp.repository.JournalEntryRepository;
import net.engineeringdigest.journalApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@JsonSerialize
@Service
@Component
public class UserService {
    @Autowired

    private UserRepository userRespository;
    private final PasswordEncoder passwordEncoder=new BCryptPasswordEncoder();

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    public ResponseEntity<?> createEntry(User entry) {
        User user = userRespository.findByUsername(entry.getUsername());
       if (user==null) {
           entry.setPassword(passwordEncoder.encode(entry.getPassword()));
           entry.setRoles(Arrays.asList("User"));
           return new ResponseEntity<>(userRespository.save(entry),HttpStatus.CREATED);
       }
       return new ResponseEntity<>("User Already exist ",HttpStatus.ALREADY_REPORTED);
    }



    public ResponseEntity<?> deleteByName(String userName){
        User user = userRespository.findByUsername(userName);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        List<JournalEntry>list=user.getJournalEntries();
        journalEntryRepository.deleteAll(list);
        userRespository.deleteById(user.getId());
        return  ResponseEntity.ok("user and their journal entry deleted successfully");
    }

    public User findByUserName(String userName){
        return  userRespository.findByUsername(userName);
    }



    public Optional<User> getByName(String userName) {

        return Optional.ofNullable(userRespository.findByUsername(userName));
    }



    public List<User> getAll(){

        return  userRespository.findAll();
    }

}
