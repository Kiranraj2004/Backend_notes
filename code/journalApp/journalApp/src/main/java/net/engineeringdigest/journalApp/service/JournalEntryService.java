package net.engineeringdigest.journalApp.service;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import net.engineeringdigest.journalApp.Entity.JournalEntry;
import net.engineeringdigest.journalApp.Entity.User;
import net.engineeringdigest.journalApp.repository.JournalEntryRepository;
import net.engineeringdigest.journalApp.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@JsonSerialize
@Service
@Component

public class JournalEntryService {
    @Autowired
    private JournalEntryRepository journalEntryRepository;
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;


    @Transactional
    public ResponseEntity<?> createEntry(JournalEntry entry,String userName) {
        entry.setCreatedDate(LocalDateTime.now());
        JournalEntry saved = journalEntryRepository.save(entry);
        User user = userService.findByUserName(userName);
        if(user==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("user found");
        }
       user.getJournalEntries().add(saved);
        userRepository.save(user);
        return ResponseEntity.ok(saved);
    }

    public Optional<JournalEntry> getById(ObjectId id) {

        return journalEntryRepository.findById(id);
    }

    public List<JournalEntry> getAll(){

        return  journalEntryRepository.findAll();
    }


    @Transactional
    public ResponseEntity<?> deleteById(ObjectId id,String userName){
        try{
            User user = userService.findByUserName(userName);
            if(user==null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("user found");
            }
            user.getJournalEntries().remove(id);
            userService.createEntry(user);
            journalEntryRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("journal Deleted successfully");
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("Transaction failed "+e);
        }
    }

    public void deleteAll(List<JournalEntry>list){
        for(JournalEntry journal:list){
            journalEntryRepository.deleteById(journal.getId());
        }
    }

    public ResponseEntity<?>  update(ObjectId id,JournalEntry newEntry) {
        JournalEntry existing = journalEntryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Entry not found"));

        existing.setTitle(newEntry.getTitle());
        existing.setContent(newEntry.getContent());


        JournalEntry saved = journalEntryRepository.save(existing);

        return ResponseEntity.ok(saved);
    }



}
