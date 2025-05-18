package net.engineeringdigest.journalApp.service;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import net.engineeringdigest.journalApp.Entity.JournalEntry;
import net.engineeringdigest.journalApp.Entity.UserEntry;
import net.engineeringdigest.journalApp.repository.JournalEntryRepository;
import net.engineeringdigest.journalApp.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@JsonSerialize
@Service
@Component
public class UserService {
    @Autowired

    private UserRepository userRespository;

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    public UserEntry createEntry(UserEntry entry) {
        return userRespository.save(entry);
    }

    public Optional<UserEntry> getById(ObjectId id) {

        return userRespository.findById(id);
    }

    public List<UserEntry> getAll(){

        return  userRespository.findAll();
    }

    public ResponseEntity<?> deleteById(String userName){
        UserEntry user = userRespository.findByUsername(userName);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        List<JournalEntry>list=user.getJournalEntries();
        journalEntryRepository.deleteAll(list);
        userRespository.deleteById(user.getId());
        return  ResponseEntity.ok("user and their journal entry deleted successfully");
    }

    public UserEntry findByUserName(String userName){
        return  userRespository.findByUsername(userName);
    }


}
