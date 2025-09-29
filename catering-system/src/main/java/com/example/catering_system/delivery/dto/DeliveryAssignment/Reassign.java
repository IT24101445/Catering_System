package com.example.catering_system.delivery.dto.DeliveryAssignment;


public class Reassign {
    private Long newDriverId;
    private String note; // optional reason

    public Long getNewDriverId() {
        return newDriverId;
    }

    public void setNewDriverId(Long newDriverId) {
        this.newDriverId = newDriverId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}