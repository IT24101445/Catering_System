package com.example.catering_system.delivery.models;

import jakarta.persistence.*;

@Entity
@Table(name = "delivery_routes")
public class DeliveryRoute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long routeId;
    private Long deliveryId;
    private String startPoint;
    private String endPoint;
    private double distanceKm;
    private int estimatedTimeMin;
    private Integer actualTimeMin;
    private String routeNotes;

    public DeliveryRoute() {}
    public DeliveryRoute(Long deliveryId, String startPoint, String endPoint, double distanceKm, int estimatedTimeMin) {
        this.deliveryId = deliveryId;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.distanceKm = distanceKm;
        this.estimatedTimeMin = estimatedTimeMin;
    }

    // Getters & Setters
    public Long getRouteId() { return routeId; }
    public void setRouteId(Long id) { this.routeId = id; }
    public Long getDeliveryId() { return deliveryId; }
    public void setDeliveryId(Long id) { this.deliveryId = id; }
    public String getStartPoint() { return startPoint; }
    public void setStartPoint(String point) { this.startPoint = point; }
    public String getEndPoint() { return endPoint; }
    public void setEndPoint(String point) { this.endPoint = point; }
    public double getDistanceKm() { return distanceKm; }
    public void setDistanceKm(double km) { this.distanceKm = km; }
    public int getEstimatedTimeMin() { return estimatedTimeMin; }
    public void setEstimatedTimeMin(int min) { this.estimatedTimeMin = min; }
    public Integer getActualTimeMin() { return actualTimeMin; }
    public void setActualTimeMin(Integer min) { this.actualTimeMin = min; }
    public String getRouteNotes() { return routeNotes; }
    public void setRouteNotes(String notes) { this.routeNotes = notes; }
}