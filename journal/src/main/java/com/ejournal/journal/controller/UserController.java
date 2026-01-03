package com.ejournal.journal.controller;

import com.ejournal.journal.entity.User;
import com.ejournal.journal.service.UserService;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<?> getAllUsers(){
        List<User> allUser = userService.getAll();
        if(allUser!=null && !allUser.isEmpty()){
            return new ResponseEntity<>(allUser, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("id/{myId}")
    public ResponseEntity<?> getUserById(@PathVariable ObjectId myId){
        Optional<User> userById = userService.getById(myId);
        if(userById.isPresent()){
            return new ResponseEntity<>(userById.get(),HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody User myUser){
        try {
            userService.saveEntry(myUser);
            return new ResponseEntity<>(myUser, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("id/{myId}")
    public ResponseEntity<?> deleteUser(@PathVariable ObjectId myId){
        try {
            userService.deleteById(myId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @PutMapping()
    public ResponseEntity<?> updateUser(@RequestBody @NonNull User newUser){
        User UserInDb = userService.findByUsername(newUser.getUsername());
        if (UserInDb!=null){
            UserInDb.setPassword(newUser.getPassword());
            UserInDb.setUsername(newUser.getUsername());
            UserInDb.setJournalEntries(newUser.getJournalEntries()!=null && !newUser.getJournalEntries().equals("")? newUser.getJournalEntries(): UserInDb.getJournalEntries());
            userService.saveEntry(UserInDb);
            return new ResponseEntity<>(UserInDb,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
