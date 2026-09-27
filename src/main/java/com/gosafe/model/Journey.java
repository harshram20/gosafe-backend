package com.gosafe.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "journeys")
public class Journey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId; // null for guest journeys

    @Column(name = "origin_name", nullable = false)
    private String originName;

    @Column(name = "origin_lat", nullable = false)
    private Double originLat;

    @Column(name = "origin_lng", nullable = false)
    private Double originLng;

    @Column(name = "destination_name", nullable = false)
    private String destinationName;

    @Column(name = "destination_lat", nullable = false)
    private Double destinationLat;

    @Column(name = "destination_lng", nullable = false)
    private Double destinationLng;

    @Column(name = "route_geometry", columnDefinition = "TEXT")
    private String routeGeometry;

    @Column(name = "duration")
    private Double duration; // minutes

    @Column(name = "distance")
    private Double distance; // km

    @Column(name = "safety_rating")
    private Double safetyRating; // out of 10

    @Column(name = "shops_count")
    private Integer shopsCount;

    @Column(name = "crowd_level")
    private String crowdLevel;

    @Column(name = "traffic_level")
    private String trafficLevel;

    @Column(name = "lighting_level")
    private String lightingLevel;

    @Column(name = "attached_image", columnDefinition = "TEXT")
    private String attachedImage; // Base64 encoded image

    @Column(name = "route_screenshot", columnDefinition = "TEXT")
    private String routeScreenshot; // Base64 encoded screenshot of the route

    @Column(name = "current_lat")
    private Double currentLat;

    @Column(name = "current_lng")
    private Double currentLng;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public Journey() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getOriginName() { return originName; }
    public void setOriginName(String originName) { this.originName = originName; }

    public Double getOriginLat() { return originLat; }
    public void setOriginLat(Double originLat) { this.originLat = originLat; }

    public Double getOriginLng() { return originLng; }
    public void setOriginLng(Double originLng) { this.originLng = originLng; }

    public String getDestinationName() { return destinationName; }
    public void setDestinationName(String destinationName) { this.destinationName = destinationName; }

    public Double getDestinationLat() { return destinationLat; }
    public void setDestinationLat(Double destinationLat) { this.destinationLat = destinationLat; }

    public Double getDestinationLng() { return destinationLng; }
    public void setDestinationLng(Double destinationLng) { this.destinationLng = destinationLng; }

    public String getRouteGeometry() { return routeGeometry; }
    public void setRouteGeometry(String routeGeometry) { this.routeGeometry = routeGeometry; }

    public Double getDuration() { return duration; }
    public void setDuration(Double duration) { this.duration = duration; }

    public Double getDistance() { return distance; }
    public void setDistance(Double distance) { this.distance = distance; }

    public Double getSafetyRating() { return safetyRating; }
    public void setSafetyRating(Double safetyRating) { this.safetyRating = safetyRating; }

    public Integer getShopsCount() { return shopsCount; }
    public void setShopsCount(Integer shopsCount) { this.shopsCount = shopsCount; }

    public String getCrowdLevel() { return crowdLevel; }
    public void setCrowdLevel(String crowdLevel) { this.crowdLevel = crowdLevel; }

    public String getTrafficLevel() { return trafficLevel; }
    public void setTrafficLevel(String trafficLevel) { this.trafficLevel = trafficLevel; }

    public String getLightingLevel() { return lightingLevel; }
    public void setLightingLevel(String lightingLevel) { this.lightingLevel = lightingLevel; }

    public String getAttachedImage() { return attachedImage; }
    public void setAttachedImage(String attachedImage) { this.attachedImage = attachedImage; }

    public String getRouteScreenshot() { return routeScreenshot; }
    public void setRouteScreenshot(String routeScreenshot) { this.routeScreenshot = routeScreenshot; }

    public Double getCurrentLat() { return currentLat; }
    public void setCurrentLat(Double currentLat) { this.currentLat = currentLat; }

    public Double getCurrentLng() { return currentLng; }
    public void setCurrentLng(Double currentLng) { this.currentLng = currentLng; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
