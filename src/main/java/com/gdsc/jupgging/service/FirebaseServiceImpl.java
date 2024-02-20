package com.gdsc.jupgging.service;

import com.gdsc.jupgging.domain.User;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class FirebaseServiceImpl implements FirebaseService {

    private static final Logger logger = LoggerFactory.getLogger(FirebaseServiceImpl.class);
    public static final String COLLECTION_NAME = "users";


    @Override
    public void saveUser(User user) throws Exception {
        Firestore firestore = FirestoreClient.getFirestore();
        firestore.collection(COLLECTION_NAME).document(user.getEmail()).set(user).get();
        logger.info("User with email {} added successfully", user.getEmail());
    }

    @Override
    public User getUserDetail(String email) throws Exception {
        Firestore firestore = FirestoreClient.getFirestore();
        DocumentSnapshot document = firestore.collection(COLLECTION_NAME).document(email).get().get();

        if (document.exists()) {
            return document.toObject(User.class);
        } else {
            logger.info("No user found with email {}", email);
            return null;
        }
    }

    @Override
    public void updateUser(User user) throws Exception {
        Firestore firestore = FirestoreClient.getFirestore();
        firestore.collection(COLLECTION_NAME).document(user.getEmail()).set(user, SetOptions.merge()).get();
        logger.info("User with email {} updated successfully", user.getEmail());
    }

    @Override
    public void deleteUser(String email) throws Exception {
        Firestore firestore = FirestoreClient.getFirestore();
        firestore.collection(COLLECTION_NAME).document(email).delete().get();
        logger.info("User with email {} deleted successfully", email);
    }
}
