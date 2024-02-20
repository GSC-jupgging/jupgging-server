package com.gdsc.jupgging.controller;

import com.gdsc.jupgging.domain.User;
import com.gdsc.jupgging.service.FirebaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class FirebaseController {

    @Autowired
    FirebaseService firebaseService;

    @PostMapping
    public void saveUser(@RequestBody User user) throws Exception {
        firebaseService.saveUser(user);
    }

    @GetMapping("/{email}")
    public User getUserDetail(@PathVariable String email) throws Exception {
        return firebaseService.getUserDetail(email);
    }

    @PutMapping
    public void updateUser(@RequestBody User user) throws Exception {
        firebaseService.updateUser(user);
    }

    @DeleteMapping ("/{email}")
    public void deleteUser(@PathVariable String email) throws Exception {
        firebaseService.deleteUser(email);
    }

}
