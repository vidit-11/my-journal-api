package com.ejournal.journal.controller;

import com.ejournal.journal.entity.JournalEntry;
import com.ejournal.journal.entity.User;
import com.ejournal.journal.service.JournalEntryService;
import com.ejournal.journal.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/journal")
public class JournalEntryControllerV2 {

    @Autowired
    private JournalEntryService journalEntryService;
    @Autowired
    private UserService userService;

    @GetMapping("{userName}")
    public ResponseEntity<?> getAllJournalEntriesOfUser(@PathVariable String userName){
        User user = userService.findByUsername(userName);
        List<JournalEntry> allEntries = user.getJournalEntries();
        if(allEntries!=null && !allEntries.isEmpty()){
            return new ResponseEntity<>(allEntries,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("{userName}")
    public ResponseEntity<JournalEntry> createEntryOfUser(@RequestBody JournalEntry myEntry, @PathVariable String userName){
        try {
            journalEntryService.saveEntry(myEntry, userName);
            return new ResponseEntity<>(myEntry, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("id/{myId}")
    public ResponseEntity<JournalEntry> getEntryByIdOfUser(@PathVariable ObjectId myId) {
        Optional<JournalEntry> journalEntry = journalEntryService.getById(myId);
        if (journalEntry.isPresent()){
            return new ResponseEntity<>(journalEntry.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("id/{userName}/{myId}")
    public ResponseEntity<?> deleteEntryByIdOfUser(@PathVariable String userName, @PathVariable  ObjectId myId){
        try {
            journalEntryService.deleteById(myId, userName);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @PutMapping("id/{userName}/{myId}")
    public ResponseEntity<?> updateEntryByIdOfUser(@PathVariable String userName, @PathVariable ObjectId myId, @RequestBody JournalEntry newEntry){
        JournalEntry oldjournalEntry=journalEntryService.getById(myId).orElse(null);
        if (oldjournalEntry!=null){
            oldjournalEntry.setTitle(newEntry.getTitle()!=null && !newEntry.getTitle().equals("")? newEntry.getTitle():oldjournalEntry.getTitle());
            oldjournalEntry.setContent(newEntry.getContent()!=null && !newEntry.getContent().equals("")? newEntry.getContent() : oldjournalEntry.getContent());
            journalEntryService.saveEntry(oldjournalEntry);
            return new ResponseEntity<>(oldjournalEntry,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
