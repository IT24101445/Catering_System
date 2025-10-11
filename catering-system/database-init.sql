-- Database initialization script for catering system
-- Run this script to create the necessary tables

-- Create drivers table
CREATE TABLE IF NOT EXISTS drivers (
    driver_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(320) NOT NULL UNIQUE,
    name VARCHAR(120) NOT NULL,
    status VARCHAR(40) NOT NULL DEFAULT 'AVAILABLE',
    password_hash VARCHAR(255) NOT NULL
);

-- Create orders table
CREATE TABLE IF NOT EXISTS orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_name VARCHAR(160) NOT NULL,
    pickup_address VARCHAR(500) NOT NULL,
    dropoff_address VARCHAR(500) NOT NULL
);

-- Create deliveries table
CREATE TABLE IF NOT EXISTS deliveries (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    pickup_address VARCHAR(500) NOT NULL,
    dropoff_address VARCHAR(500) NOT NULL,
    status VARCHAR(40) NOT NULL DEFAULT 'PENDING',
    directions_url VARCHAR(1000)
);

-- Create delivery_assignments table
CREATE TABLE IF NOT EXISTS delivery_assignments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    route VARCHAR(500),
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    driver_id BIGINT NOT NULL,
    order_id BIGINT NOT NULL,
    FOREIGN KEY (driver_id) REFERENCES drivers(driver_id),
    FOREIGN KEY (order_id) REFERENCES orders(id)
);

-- Create notifications table
CREATE TABLE IF NOT EXISTS notifications (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    driver_id BIGINT NOT NULL,
    message VARCHAR(500) NOT NULL,
    type VARCHAR(50),
    is_read BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (driver_id) REFERENCES drivers(driver_id)
);

-- Create delivery_supervisors table
CREATE TABLE IF NOT EXISTS delivery_supervisors (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(320) NOT NULL UNIQUE,
    name VARCHAR(120) NOT NULL,
    password_hash VARCHAR(255) NOT NULL
);

-- Insert default supervisor if not exists
INSERT IGNORE INTO delivery_supervisors (email, name, password_hash) 
VALUES ('admin@catering.com', 'Admin Supervisor', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8');

-- Insert some sample data for testing
INSERT IGNORE INTO drivers (email, name, status, password_hash) 
VALUES 
    ('john.doe@example.com', 'John Doe', 'AVAILABLE', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8'),
    ('jane.smith@example.com', 'Jane Smith', 'AVAILABLE', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8');

INSERT IGNORE INTO orders (customer_name, pickup_address, dropoff_address) 
VALUES 
    ('Alice Johnson', '123 Main St, City', '456 Oak Ave, City'),
    ('Bob Wilson', '789 Pine St, City', '321 Elm St, City');

INSERT IGNORE INTO deliveries (pickup_address, dropoff_address, status) 
VALUES 
    ('123 Main St, City', '456 Oak Ave, City', 'PENDING'),
    ('789 Pine St, City', '321 Elm St, City', 'PENDING');
