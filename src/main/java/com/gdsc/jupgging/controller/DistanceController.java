package com.gdsc.jupgging.controller;

import com.gdsc.jupgging.domain.Coordinates;
import com.gdsc.jupgging.domain.User;
import com.gdsc.jupgging.service.DistanceService;
import com.gdsc.jupgging.service.FirebaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/distance")
public class DistanceController {

    private static final Logger logger = LoggerFactory.getLogger(DistanceController.class);
    private final DistanceService distanceService;
    private final FirebaseService firebaseService;

    @Autowired
    public DistanceController(DistanceService distanceService, FirebaseService firebaseService) {
        this.distanceService = distanceService;
        this.firebaseService = firebaseService;
    }

    @PostMapping("/calculate-distance")
    public double calculateDistance(@RequestBody Coordinates coordinates) throws IOException {
        return distanceService.calculateDistance(coordinates);
    }

    @PostMapping("/update-distance")
    public void saveDistance(@RequestBody User user) {
        try {
            firebaseService.updateUser(user);
            logger.info("Distance data saved successfully for user with email {}", user.getEmail());
        } catch (Exception e) {
            logger.error("Error occurred while saving distance data: {}", e.getMessage());
        }
    }

    @PostMapping("/user/{userId}/update-distance")
    public ResponseEntity<?> updateUserDistance(@PathVariable String userId, @RequestBody Coordinates coordinates) {
        try {
            // 거리 계산
            double distance = distanceService.calculateDistance(coordinates);
            distanceService.updateDistance(userId, distance);
            logger.info("Distance updated successfully");
            return ResponseEntity.ok().body("Distance updated successfully");
        } catch (IOException e) {
            logger.error("Error calculating distance");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error calculating distance");
        }
    }
}
