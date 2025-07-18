package net.engineeringdigest.journalApp;

import net.engineeringdigest.journalApp.Entity.User;
import net.engineeringdigest.journalApp.repository.UserRepository;
import net.engineeringdigest.journalApp.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserEntry {
    @Autowired
    private UserRepository userRepository;

    @Test
    public void testAddition() {
        assertEquals(4, 2 + 2); // This will pass
    }


    @Test
    public void testUser(){
        User user=userRepository.findByUsername("kiran");
        assertNotNull(user);

    }

}
