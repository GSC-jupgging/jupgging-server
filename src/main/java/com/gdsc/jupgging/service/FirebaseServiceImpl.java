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
        firestore.collection(COLLECTION_NAME).document(user.getUid()).set(user).get();
        logger.info("User added successfully");
    }

    @Override
    public User getUserDetail(String uid) throws Exception {
        Firestore firestore = FirestoreClient.getFirestore();
        DocumentSnapshot document = firestore.collection(COLLECTION_NAME).document(uid).get().get();

        if (document.exists()) {
            logger.info("User found");
            return document.toObject(User.class);
        } else {
            logger.info("No user found");
            return null;
        }
    }

    @Override
    public void updateUser(User user) throws Exception {
        Firestore firestore = FirestoreClient.getFirestore();
        firestore.collection(COLLECTION_NAME).document(user.getUid()).set(user, SetOptions.merge()).get();
        logger.info("User updated successfully");
    }

    @Override
    public void deleteUser(String uid) throws Exception {
        Firestore firestore = FirestoreClient.getFirestore();
        firestore.collection(COLLECTION_NAME).document(uid).delete().get();
        logger.info("User deleted successfully");
    }

}
