package com.example.catering_system.delivery.dto.DeliveryAssignment;


public class Complete {
    private Double latitude;   // optional
    private Double longitude;  // optional
    private String note;       // optional

    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}