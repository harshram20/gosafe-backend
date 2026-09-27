package com.gosafe.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/location")
public class LocationController {

    private final HttpClient httpClient = HttpClient.newHttpClient();

    @GetMapping("/reverse")
    public ResponseEntity<String> reverseGeocode(@RequestParam Double lat, @RequestParam Double lng) {
        try {
            String url = String.format("https://nominatim.openstreetmap.org/reverse?format=json&lat=%f&lon=%f&accept-language=en", lat, lng);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("User-Agent", "GoSafeApp/1.0 (contact@gosafe.local)")
                    .header("Accept", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return ResponseEntity.ok(response.body());
            }
        } catch (Exception e) {
            // fall through to fallback
        }

        // Mock fallback if rate-limited (429) or Nominatim is offline
        String fallbackJson = String.format(
                "{\"display_name\":\"Current Location (%f, %f), Durgapur, WB, India\", \"lat\":\"%f\", \"lon\":\"%f\"}",
                lat, lng, lat, lng
        );
        return ResponseEntity.ok(fallbackJson);
    }

    @GetMapping("/search")
    public ResponseEntity<String> searchGeocode(@RequestParam String query) {
        try {
            String url = String.format("https://nominatim.openstreetmap.org/search?format=json&q=%s&limit=5&countrycodes=in&accept-language=en",
                    java.net.URLEncoder.encode(query, java.nio.charset.StandardCharsets.UTF_8));
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("User-Agent", "GoSafeApp/1.0 (contact@gosafe.local)")
                    .header("Accept", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return ResponseEntity.ok(response.body());
            }
        } catch (Exception e) {
            // fall through to fallback
        }

        // Mock fallback list if rate-limited or offline, focusing on Durgapur area
        String fallbackJson = "[" +
                "{\"display_name\":\"Bidhannagar, Durgapur, West Bengal, India\", \"lat\":\"23.5166\", \"lon\":\"87.3421\"}," +
                "{\"display_name\":\"City Center Bus Stand, Durgapur, WB, India\", \"lat\":\"23.5332\", \"lon\":\"87.2939\"}," +
                "{\"display_name\":\"Muchipara Junction, Durgapur, WB, India\", \"lat\":\"23.5042\", \"lon\":\"87.3478\"}," +
                "{\"display_name\":\"Benachity Market, Durgapur, WB, India\", \"lat\":\"23.5512\", \"lon\":\"87.2882\"}," +
                "{\"display_name\":\"Durgapur Railway Station, WB, India\", \"lat\":\"23.4962\", \"lon\":\"87.3117\"}" +
                "]";
        return ResponseEntity.ok(fallbackJson);
    }

    @GetMapping("/shops")
    public ResponseEntity<String> getShops(@RequestParam Double lat, @RequestParam Double lng) {
        try {
            // Search all shop nodes within 1.5km (1500m) of the route midpoint coordinates
            String query = String.format("[out:json];node(around:1500,%f,%f)[shop];out;", lat, lng);
            String url = "https://overpass-api.de/api/interpreter?data=" + java.net.URLEncoder.encode(query, java.nio.charset.StandardCharsets.UTF_8);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("User-Agent", "GoSafeApp/1.0 (contact@gosafe.local)")
                    .header("Accept", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return ResponseEntity.ok(response.body());
            }
        } catch (Exception e) {
            // fall through to empty fallback
        }

        // Return empty elements if Overpass API is rate-limited or offline to ensure no fake/mock shops are displayed
        return ResponseEntity.ok("{\"elements\":[]}");
    }
}
