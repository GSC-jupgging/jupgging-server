package com.gdsc.jupgging.service;

import com.gdsc.jupgging.domain.User;

public interface FirebaseService {

    public void saveUser(User user) throws Exception;

    public User getUserDetail(String email) throws Exception;

    public void updateUser(User user) throws Exception;

    public void deleteUser(String email) throws Exception;

}
