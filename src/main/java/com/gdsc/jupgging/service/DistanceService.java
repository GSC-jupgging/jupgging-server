package com.gdsc.jupgging.service;

import com.gdsc.jupgging.domain.Coordinates;
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
}
