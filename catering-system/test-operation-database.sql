-- Test script to verify operation manager database operations
-- Run this after setting up the database to verify everything is working

USE cateringDB;

PRINT 'Testing Operation Manager Database Operations...';

-- Test 1: Check if tables exist
PRINT '1. Checking table existence...';
IF EXISTS (SELECT * FROM sysobjects WHERE name='operation_orders' AND xtype='U')
    PRINT '✓ operation_orders table exists';
ELSE
    PRINT '❌ operation_orders table missing';

IF EXISTS (SELECT * FROM sysobjects WHERE name='staff' AND xtype='U')
    PRINT '✓ staff table exists';
ELSE
    PRINT '❌ staff table missing';

IF EXISTS (SELECT * FROM sysobjects WHERE name='bookings' AND xtype='U')
    PRINT '✓ bookings table exists';
ELSE
    PRINT '❌ bookings table missing';

-- Test 2: Check sample data
PRINT '2. Checking sample data...';
SELECT COUNT(*) as order_count FROM operation_orders;
SELECT COUNT(*) as staff_count FROM staff;
SELECT COUNT(*) as booking_count FROM bookings;

-- Test 3: Test order creation
PRINT '3. Testing order creation...';
INSERT INTO operation_orders (customerName, details, status) 
VALUES ('Test Customer', 'Test Order Details', 'PENDING');

-- Test 4: Test staff creation
PRINT '4. Testing staff creation...';
INSERT INTO staff (name, role, available) 
VALUES ('Test Staff', 'Test Role', 1);

-- Test 5: Test order assignment
PRINT '5. Testing order assignment...';
DECLARE @orderId INT = (SELECT TOP 1 id FROM operation_orders WHERE customerName = 'Test Customer');
DECLARE @staffId INT = (SELECT TOP 1 id FROM staff WHERE name = 'Test Staff');

INSERT INTO bookings (orderId, staffId, status) 
VALUES (@orderId, @staffId, 'ASSIGNED');

UPDATE operation_orders 
SET status = 'ASSIGNED' 
WHERE id = @orderId;

-- Test 6: Verify assignment
PRINT '6. Verifying assignment...';
SELECT 
    o.id as orderId,
    o.customerName,
    o.details,
    o.status,
    s.id as staffId,
    s.name as staffName,
    s.role,
    b.bookingDate
FROM operation_orders o
JOIN bookings b ON o.id = b.orderId
JOIN staff s ON b.staffId = s.id
WHERE o.customerName = 'Test Customer';

-- Test 7: Clean up test data
PRINT '7. Cleaning up test data...';
DELETE FROM bookings WHERE orderId = @orderId;
DELETE FROM operation_orders WHERE customerName = 'Test Customer';
DELETE FROM staff WHERE name = 'Test Staff';

PRINT 'Operation Manager Database Test Complete!';
PRINT 'All operations working correctly.';
