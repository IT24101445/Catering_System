package com.example.catering_system.delivery.models;


import jakarta.persistence.*;

@Entity
@Table(name = "deliveries")
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pickup_address", nullable = false, length = 500)
    private String pickupAddress;

    @Column(name = "dropoff_address", nullable = false, length = 500)
    private String dropoffAddress;

    // Suggested values: "PENDING", "ASSIGNED", "IN_PROGRESS", "COMPLETED", "CANCELLED"
    @Column(nullable = false, length = 40)
    private String status = "PENDING";

    // Proof of delivery (simple reference/path or signature blob key)
    @Column(name = "pod_signature_ref")
    private String proofOfDeliverySignatureRef;

    public Delivery() {
    }

    public Delivery(Long id, String pickupAddress, String dropoffAddress, String status) {
        this.id = id;
        this.pickupAddress = pickupAddress;
        this.dropoffAddress = dropoffAddress;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPickupAddress() {
        return pickupAddress;
    }

    public void setPickupAddress(String pickupAddress) {
        this.pickupAddress = pickupAddress;
    }

    public String getDropoffAddress() {
        return dropoffAddress;
    }

    public void setDropoffAddress(String dropoffAddress) {
        this.dropoffAddress = dropoffAddress;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProofOfDeliverySignatureRef() {
        return proofOfDeliverySignatureRef;
    }

    public void setProofOfDeliverySignatureRef(String proofOfDeliverySignatureRef) {
        this.proofOfDeliverySignatureRef = proofOfDeliverySignatureRef;
    }
}
