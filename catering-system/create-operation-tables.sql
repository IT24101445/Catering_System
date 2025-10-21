-- Create Operation Manager Tables
-- Run this script to create the necessary tables for operation management

-- Create managers table
CREATE TABLE IF NOT EXISTS managers (
    id INT IDENTITY(1,1) PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100),
    email VARCHAR(100),
    phone VARCHAR(20),
    created_at DATETIME2 DEFAULT GETDATE()
);

-- Create staff table
CREATE TABLE IF NOT EXISTS staff (
    id INT IDENTITY(1,1) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    role VARCHAR(50) NOT NULL,
    available BIT DEFAULT 1,
    created_at DATETIME2 DEFAULT GETDATE()
);

-- Create orders table for operation management
CREATE TABLE IF NOT EXISTS operation_orders (
    id INT IDENTITY(1,1) PRIMARY KEY,
    customer_name VARCHAR(100) NOT NULL,
    details TEXT,
    status VARCHAR(50) DEFAULT 'PENDING',
    created_at DATETIME2 DEFAULT GETDATE()
);

-- Create assigned_orders table
CREATE TABLE IF NOT EXISTS assigned_orders (
    id INT IDENTITY(1,1) PRIMARY KEY,
    order_id INT NOT NULL,
    staff_id INT NOT NULL,
    assigned_at DATETIME2 DEFAULT GETDATE(),
    status VARCHAR(50) DEFAULT 'ASSIGNED',
    FOREIGN KEY (order_id) REFERENCES operation_orders(id),
    FOREIGN KEY (staff_id) REFERENCES staff(id)
);

-- Insert default operation manager
INSERT INTO managers (username, password, full_name, email, phone) VALUES
('admin', 'admin123', 'Operation Manager', 'admin@catering.com', '123-456-7890'),
('manager', 'manager123', 'Operations Manager', 'manager@catering.com', '123-456-7891');

-- Insert sample staff
INSERT INTO staff (name, role, available) VALUES
('John Smith', 'Chef', 1),
('Jane Doe', 'Server', 1),
('Mike Johnson', 'Kitchen Assistant', 1),
('Sarah Wilson', 'Event Coordinator', 1);

-- Insert sample orders
INSERT INTO operation_orders (customer_name, details, status) VALUES
('ABC Corporation', 'Corporate lunch for 50 people', 'PENDING'),
('XYZ Events', 'Wedding reception catering', 'PENDING'),
('Tech Startup', 'Office party catering', 'PENDING');

PRINT 'Operation management tables created successfully!';
