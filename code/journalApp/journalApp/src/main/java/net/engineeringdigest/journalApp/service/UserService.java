package net.engineeringdigest.journalApp.service;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import net.engineeringdigest.journalApp.Entity.UserEntry;
import net.engineeringdigest.journalApp.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
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

    public UserEntry createEntry(UserEntry entry) {
        return userRespository.save(entry);
    }

    public Optional<UserEntry> getById(ObjectId id) {

        return userRespository.findById(id);
    }

    public List<UserEntry> getAll(){

        return  userRespository.findAll();
    }

    public void deleteById(ObjectId id){

        userRespository.deleteById(id);
    }

    public UserEntry findByUserName(String userName){
        return  userRespository.findByUsername(userName);
    }


}
