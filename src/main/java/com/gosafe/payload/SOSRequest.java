package com.gosafe.payload;

import jakarta.validation.constraints.NotNull;

public class SOSRequest {
    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;

    private String message;
    private String guestEmergencyName;
    private String guestEmergencyPhone;

    // Getters and Setters
    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getGuestEmergencyName() { return guestEmergencyName; }
    public void setGuestEmergencyName(String guestEmergencyName) { this.guestEmergencyName = guestEmergencyName; }

    public String getGuestEmergencyPhone() { return guestEmergencyPhone; }
    public void setGuestEmergencyPhone(String guestEmergencyPhone) { this.guestEmergencyPhone = guestEmergencyPhone; }
}