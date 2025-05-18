package net.engineeringdigest.journalApp.Controler;

import net.engineeringdigest.journalApp.Entity.JournalEntry;
import net.engineeringdigest.journalApp.service.JournalEntryService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController

@RequestMapping("/journal")
public class JournalEntryControllerV2 {
    @Autowired
    private JournalEntryService journalEntryService;

    // GET all entries


//    Post Method
    @PostMapping()
    public boolean createEntry(@RequestBody JournalEntry obj){
        journalEntryService.createEntry(obj);
        return true;
    }

    @GetMapping("/{id}")
    public Optional<JournalEntry> getById(@PathVariable("id") ObjectId id ){
        return journalEntryService.getById(id);
    }


    @GetMapping()
    public List<JournalEntry>  getall(){

        return journalEntryService.getAll();
    }
//
    @PutMapping("/{id}")
    public JournalEntry updateOne(@PathVariable ObjectId id, @RequestBody JournalEntry obj) {
        return journalEntryService.update(id,obj);

    }
//
    @DeleteMapping("/{id}")
    public boolean deleteone(@PathVariable ObjectId id){
       journalEntryService.deleteById(id);
        return true;
    }



}
