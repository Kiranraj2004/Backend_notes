package net.engineeringdigest.journalApp.service;


import net.engineeringdigest.journalApp.Entity.JournalEntry;
import net.engineeringdigest.journalApp.repository.JournalEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.print.attribute.standard.JobHoldUntil;
import java.util.List;
import java.util.Optional;

@Service
@Component
public class JournalEntryService {
    @Autowired
    private JournalEntryRepository journalEntryRepository;

    public JournalEntry createEntry(JournalEntry entry) {
        return journalEntryRepository.save(entry);
    }
    public JournalEntry getById(int id) {
       return journalEntryRepository.findById(id).get();
    }

    public List<JournalEntry> getAll(){
        return  journalEntryRepository.findAll();
    }

    public void deleteById(int id){
        journalEntryRepository.deleteById(id);
    }
    public JournalEntry  update(int id,JournalEntry newEntry){
        JournalEntry oldentry=getById(id);
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
