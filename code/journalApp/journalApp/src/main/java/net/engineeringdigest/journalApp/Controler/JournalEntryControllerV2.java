package net.engineeringdigest.journalApp.Controler;

import net.engineeringdigest.journalApp.Entity.JournalEntry;
import net.engineeringdigest.journalApp.service.JournalEntryService;
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
    public JournalEntry getById(@PathVariable("id") int  id ){
        return journalEntryService.getById(id);
    }


    @GetMapping()
    public List<JournalEntry>  getall(){
        return journalEntryService.getAll();
    }
//
    @PutMapping("/{id}")
    public JournalEntry updateOne(@PathVariable int id, @RequestBody JournalEntry obj) {
        return journalEntryService.update(id,obj);

    }
//
    @DeleteMapping("/{id}") public boolean deleteone(@PathVariable int id){
       journalEntryService.deleteById(id);
        return true;
    }



}
