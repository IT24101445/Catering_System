-- Complete Operation Manager Database Setup
-- This script creates all missing tables for the operation manager module
-- Run this script in your SQL Server database

USE cateringDB; -- Replace with your actual database name

PRINT 'Starting Operation Manager Database Setup...';

-- 1. Create managers table
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='managers' AND xtype='U')
BEGIN
    CREATE TABLE managers (
        id INT IDENTITY(1,1) PRIMARY KEY,
        username NVARCHAR(50) NOT NULL UNIQUE,
        password NVARCHAR(255) NOT NULL,
        fullName NVARCHAR(255) NOT NULL,
        email NVARCHAR(255),
        phone NVARCHAR(50),
        created_at DATETIME2 DEFAULT GETDATE(),
        updated_at DATETIME2 DEFAULT GETDATE()
    );
    
    -- Add indexes for better performance
    CREATE INDEX IX_managers_Username ON managers(username);
    CREATE INDEX IX_managers_Email ON managers(email);
    
    PRINT '✓ managers table created successfully';
END
ELSE
BEGIN
    PRINT '✓ managers table already exists';
END

-- 3. Create operation_orders table
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='operation_orders' AND xtype='U')
BEGIN
    CREATE TABLE operation_orders (
        id INT IDENTITY(1,1) PRIMARY KEY,
        customerName NVARCHAR(255) NOT NULL,
        details NVARCHAR(MAX),
        status NVARCHAR(50) DEFAULT 'PENDING',
        created_at DATETIME2 DEFAULT GETDATE(),
        updated_at DATETIME2 DEFAULT GETDATE()
    );
    
    -- Add indexes for better performance
    CREATE INDEX IX_operation_orders_Status ON operation_orders(status);
    CREATE INDEX IX_operation_orders_CustomerName ON operation_orders(customerName);
    
    PRINT '✓ operation_orders table created successfully';
END
ELSE
BEGIN
    PRINT '✓ operation_orders table already exists';
END

-- 4. Create staff table
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='staff' AND xtype='U')
BEGIN
    CREATE TABLE staff (
        id INT IDENTITY(1,1) PRIMARY KEY,
        name NVARCHAR(255) NOT NULL,
        role NVARCHAR(100),
        available BIT DEFAULT 1,
        created_at DATETIME2 DEFAULT GETDATE(),
        updated_at DATETIME2 DEFAULT GETDATE()
    );
    
    -- Add indexes for better performance
    CREATE INDEX IX_staff_Available ON staff(available);
    CREATE INDEX IX_staff_Role ON staff(role);
    
    PRINT '✓ staff table created successfully';
END
ELSE
BEGIN
    PRINT '✓ staff table already exists';
END

-- 5. Create bookings table
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='bookings' AND xtype='U')
BEGIN
    CREATE TABLE bookings (
        id INT IDENTITY(1,1) PRIMARY KEY,
        orderId INT NOT NULL,
        staffId INT NOT NULL,
        bookingDate DATETIME2 DEFAULT GETDATE(),
        status NVARCHAR(50) DEFAULT 'ASSIGNED',
        created_at DATETIME2 DEFAULT GETDATE(),
        FOREIGN KEY (orderId) REFERENCES operation_orders(id) ON DELETE CASCADE,
        FOREIGN KEY (staffId) REFERENCES staff(id) ON DELETE CASCADE
    );
    
    -- Add indexes for better performance
    CREATE INDEX IX_bookings_OrderId ON bookings(orderId);
    CREATE INDEX IX_bookings_StaffId ON bookings(staffId);
    CREATE INDEX IX_bookings_Status ON bookings(status);
    
    PRINT '✓ bookings table created successfully';
END
ELSE
BEGIN
    PRINT '✓ bookings table already exists';
END

-- 6. Add sample manager data
IF NOT EXISTS (SELECT * FROM managers WHERE username = 'admin')
BEGIN
    INSERT INTO managers (username, password, fullName, email, phone) VALUES
    ('admin', 'admin123', 'Administrator', 'admin@catering.com', '123-456-7890'),
    ('manager', 'manager123', 'Operations Manager', 'manager@catering.com', '123-456-7891');
    
    PRINT '✓ Sample managers added to managers table';
END
ELSE
BEGIN
    PRINT '✓ Sample managers already exist in managers table';
END

-- 7. Add sample data to operation_orders table
IF NOT EXISTS (SELECT * FROM operation_orders WHERE customerName = 'ABC Corporation')
BEGIN
    INSERT INTO operation_orders (customerName, details, status) VALUES
    ('ABC Corporation', 'Corporate lunch for 50 people', 'PENDING'),
    ('XYZ Events', 'Wedding reception catering', 'PENDING'),
    ('Tech Startup', 'Office party catering', 'PENDING');
    
    PRINT '✓ Sample orders added to operation_orders table';
END
ELSE
BEGIN
    PRINT '✓ Sample orders already exist in operation_orders table';
END

-- 8. Add sample data to staff table
IF NOT EXISTS (SELECT * FROM staff WHERE name = 'John Smith')
BEGIN
    INSERT INTO staff (name, role, available) VALUES
    ('John Smith', 'Chef', 1),
    ('Jane Doe', 'Server', 1),
    ('Mike Johnson', 'Kitchen Assistant', 1),
    ('Sarah Wilson', 'Event Coordinator', 1),
    ('David Brown', 'Catering Manager', 1);
    
    PRINT '✓ Sample staff added to staff table';
END
ELSE
BEGIN
    PRINT '✓ Sample staff already exist in staff table';
END

-- 9. Add sample booking data
IF NOT EXISTS (SELECT * FROM bookings WHERE orderId = 1)
BEGIN
    INSERT INTO bookings (orderId, staffId, status) VALUES
    (1, 1, 'ASSIGNED'),
    (2, 2, 'ASSIGNED');
    
    -- Update corresponding orders to ASSIGNED status
    UPDATE operation_orders SET status = 'ASSIGNED' WHERE id IN (1, 2);
    
    PRINT '✓ Sample bookings added to bookings table';
END
ELSE
BEGIN
    PRINT '✓ Sample bookings already exist in bookings table';
END

PRINT 'Operation Manager Database Setup Complete!';
PRINT 'All tables created and sample data inserted successfully.';
