package com.ejournal.journal.service;

import com.ejournal.journal.entity.User;
import com.ejournal.journal.repository.UserRepo;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class UserService {

    @Autowired
    private UserRepo userRepo;

    public void saveEntry(User user){
            userRepo.save(user);
    }

    public List<User> getAll(){
        return userRepo.findAll();
    }

    public Optional<User> getById(ObjectId id){
        return userRepo.findById(id);
    }

    public void deleteById(ObjectId id){
        userRepo.deleteById(id);
    }

}
