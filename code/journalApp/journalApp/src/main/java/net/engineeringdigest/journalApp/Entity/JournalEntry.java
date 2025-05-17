package net.engineeringdigest.journalApp.Entity;
// Entity can also called as model

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "journal_entries")
//above line tell this  is the collection name like table name in database

@Getter
@Setter
public class JournalEntry {

    @Id
    private int  id;
    private String title;
    private String content;




//    public void setDate(Date date) {
//        this.date = date;
//    }

//    public JournalEntry() {
//    }
//
//    public JournalEntry(String title, String content) {
//        this.title = title;
//        this.content = content;
//    }
//
//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }
//
//    public String getTitle() {
//        return title;
//    }
//
//    public void setTitle(String title) {
//        this.title = title;
//    }
//
//    public String getContent() {
//        return content;
//    }
//
//    public void setContent(String content) {
//        this.content = content;
//    }
}