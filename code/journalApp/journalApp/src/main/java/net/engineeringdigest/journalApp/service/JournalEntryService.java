package net.engineeringdigest.journalApp.service;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import net.engineeringdigest.journalApp.Entity.JournalEntry;
import net.engineeringdigest.journalApp.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@JsonSerialize
@Service
@Component
public class JournalEntryService {
    @Autowired
    private JournalEntryRepository journalEntryRepository;

    public JournalEntry createEntry(JournalEntry entry) {
        entry.setCreatedDate(LocalDateTime.now());
        return journalEntryRepository.save(entry);
    }

    public Optional<JournalEntry> getById(ObjectId id) {

        return journalEntryRepository.findById(id);
    }

    public List<JournalEntry> getAll(){

        return  journalEntryRepository.findAll();
    }

    public void deleteById(ObjectId id){

        journalEntryRepository.deleteById(id);
    }

    public JournalEntry  update(ObjectId id,JournalEntry newEntry){
        JournalEntry oldentry=getById(id).get();

        if(oldentry!=null){
            if(newEntry.getTitle()!=null&&!newEntry.getTitle().isEmpty()){
                oldentry.setTitle(newEntry.getTitle());
            }
            if(newEntry.getContent()!=null&&!newEntry.getContent().isEmpty()){
                oldentry.setContent(newEntry.getContent());
            }
            return createEntry(oldentry);

        }
        return null;
    }
}
