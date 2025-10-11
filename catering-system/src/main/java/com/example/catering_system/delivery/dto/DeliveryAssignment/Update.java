package com.example.catering_system.delivery.dto.DeliveryAssignment;


public class Update {
    private Long orderId;   // optional
    private Long driverId;  // optional
    private String route;   // optional

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }
}