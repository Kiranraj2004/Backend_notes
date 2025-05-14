package net.engineeringdigest.journalApp.Controler;

import net.engineeringdigest.journalApp.Entity.JournalEntry;
import net.engineeringdigest.journalApp.JournalApplication;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

@RestController

@RequestMapping("/journal")
public class JournalEntryController {
//    private HashMap<Long, JournalEntry>map=new HashMap<>();
//
//    // GET all entries
//
//    @GetMapping()
//    public ArrayList<JournalEntry> getAll() {
//        return new ArrayList<>(map.values());
//    }
//
//
//    @PostMapping()
//    public boolean addjournal(@RequestBody JournalEntry obj){
//        map.put(obj.getId(),obj);
//        return true;
//    }
//
//    @GetMapping("/{id}")
//    public JournalEntry getById(@PathVariable("id") long id ){
//        return map.get(id);
//    }
//
//    @PutMapping("/{id}")
//    public JournalEntry updateOne(@PathVariable long id, @RequestBody JournalEntry obj) {
//        obj.setId(id); // Ensure the ID is set properly
//        map.put(id, obj); // Update the entry
//        return obj;
//    }
//
//    @DeleteMapping("/{id}") public boolean deleteone(@PathVariable long id){
//        map.remove(id);
//        return true;
//    }



}
