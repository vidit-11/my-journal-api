package com.ejournal.journal.controller;

import com.ejournal.journal.entity.User;
import com.ejournal.journal.service.UserService;
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

    @PutMapping("id/{myId}")
    public ResponseEntity<?> updateUserById(@PathVariable ObjectId myId, @RequestBody User newUser){
        User oldUser = userService.getById(myId).orElse(null);
        if (oldUser!=null){
            oldUser.setPassword(newUser.getPassword()!=null && !newUser.getPassword().equals("")? newUser.getPassword(): oldUser.getPassword());
            oldUser.setUsername(newUser.getUsername()!=null && !newUser.getUsername().equals("")? newUser.getUsername(): oldUser.getUsername());
            oldUser.setJournalEntries(newUser.getJournalEntries()!=null && !newUser.getJournalEntries().equals("")? newUser.getJournalEntries(): oldUser.getJournalEntries());
            userService.saveEntry(oldUser);
            return new ResponseEntity<>(oldUser,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
