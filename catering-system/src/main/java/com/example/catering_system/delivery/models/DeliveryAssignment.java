package com.example.catering_system.delivery.models;


import jakarta.persistence.*;

@Entity
@Table(
        name = "delivery_assignments",
        indexes = {
                @Index(name = "idx_assignment_driver", columnList = "driver_id"),
                @Index(name = "idx_assignment_order", columnList = "order_id")
        }
)
public class DeliveryAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Simple textual representation of the route; extend later if needed
    @Column(nullable = false, length = 500)
    private String route;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_assignment_driver"))
    private Driver driver;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_assignment_order"))
    private Order order;

    @Column(nullable = false, length = 20)
    private String status = "PENDING"; // PENDING, ACCEPTED, DECLINED, IN_PROGRESS, COMPLETED

    @Column(name = "created_at")
    private java.time.LocalDateTime createdAt = java.time.LocalDateTime.now();

    @Column(name = "completed_at")
    private java.time.LocalDateTime completedAt;

    public DeliveryAssignment() {
    }

    public DeliveryAssignment(Long id, String route, Driver driver, Order order, String status) {
        this.id = id;
        this.route = route;
        this.driver = driver;
        this.order = order;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public java.time.LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(java.time.LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public java.time.LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(java.time.LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }
}