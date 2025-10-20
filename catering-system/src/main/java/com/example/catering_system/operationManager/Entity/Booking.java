package com.example.catering_system.operationManager.Entity;

public class Booking {
    private int id;
    private int orderId;
    private int staffId;
    private String bookingDate;
    private boolean confirmed;

    public Booking() {}

    public Booking(int id, int orderId, int staffId, String bookingDate, boolean confirmed) {
        this.id = id;
        this.orderId = orderId;
        this.staffId = staffId;
        this.bookingDate = bookingDate;
        this.confirmed = confirmed;
    }

    // getter and setter method
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getStaffId() {
        return staffId;
    }

    public void setStaffId(int staffId) {
        this.staffId = staffId;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }
}
