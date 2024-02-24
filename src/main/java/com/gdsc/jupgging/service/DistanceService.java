package com.gdsc.jupgging.service;

import com.gdsc.jupgging.domain.Coordinates;
import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutureCallback;
import com.google.api.core.ApiFutures;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.firebase.cloud.FirestoreClient;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Service
public class DistanceService {

    private final OkHttpClient client;
    private final String mapApiKey;

    public DistanceService() {
        this.client = new OkHttpClient();
        this.mapApiKey = loadApiKey();
    }

    private String loadApiKey() {
        Properties properties = new Properties();
        String apiKey = "";

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("resources.properties")) {
            properties.load(inputStream);
            apiKey = properties.getProperty("map.api.key");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return apiKey;
    }

    public double calculateDistance(Coordinates coordinates) throws IOException {
        double distance = 0.0;

        String url = "https://apis.openapi.sk.com/tmap/routes/distance?version=1&startX=" + coordinates.getStartX() +
                "&startY=" + coordinates.getStartY() +
                "&endX=" + coordinates.getEndX() +
                "&endY=" + coordinates.getEndY() +
                "&reqCoordType=WGS84GEO&callback=function";

        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("accept", "application/json")
                .addHeader("appKey", mapApiKey)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            JsonParser parser = new JsonParser();
            JsonObject jsonObject = parser.parse(response.body().string()).getAsJsonObject();
            int extracted_distance = jsonObject.getAsJsonObject("distanceInfo").get("distance").getAsInt();

            distance = (double) extracted_distance;
        }

        return distance;
    }

    public void updateDistance(String userId, double addDistance) {
        Firestore firestore = FirestoreClient.getFirestore();
        DocumentReference docRef = firestore.collection("users").document(userId);

        ApiFuture<DocumentSnapshot> future = docRef.get();
        ApiFutures.addCallback(future, new ApiFutureCallback<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot document) {
                if (document.exists()) {
                    Double currentDistance = document.getDouble("distance");
                    if (currentDistance == null) currentDistance = 0.0;

                    System.out.println("Current distance for user " + userId + ": " + currentDistance);

                    double newDistance = currentDistance + addDistance;
                    System.out.println("New distance to update for user " + userId + ": " + newDistance);

                    ApiFuture<WriteResult> updateFuture = docRef.update("distance", newDistance);
                    ApiFutures.addCallback(updateFuture, new ApiFutureCallback<WriteResult>() {
                        @Override
                        public void onSuccess(WriteResult result) {
                            System.out.println("Distance updated for user. \nUpdate time: " + result.getUpdateTime());
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            System.err.println("Error updating distance");
                        }
                    }, MoreExecutors.directExecutor());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                System.err.println("Error fetching distance");
            }
        }, MoreExecutors.directExecutor());
    }
}
