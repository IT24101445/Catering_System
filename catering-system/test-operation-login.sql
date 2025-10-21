-- Test script for operation login functionality
-- Run this to verify the operation login setup

-- Check if managers table exists
IF EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'managers')
BEGIN
    PRINT 'managers table exists';
    
    -- Check if there are any managers
    SELECT COUNT(*) as manager_count FROM managers;
    
    -- Show all managers
    SELECT id, username, full_name, email FROM managers;
    
    -- Test login credentials
    SELECT 'Testing admin login...' as test;
    SELECT * FROM managers WHERE username = 'admin' AND password = 'admin123';
    
    SELECT 'Testing manager login...' as test;
    SELECT * FROM managers WHERE username = 'manager' AND password = 'manager123';
END
ELSE
BEGIN
    PRINT 'ERROR: managers table does not exist!';
    PRINT 'Please run create-operation-tables.sql first';
END

-- Check if staff table exists
IF EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'staff')
BEGIN
    PRINT 'staff table exists';
    SELECT COUNT(*) as staff_count FROM staff;
END
ELSE
BEGIN
    PRINT 'WARNING: staff table does not exist';
END

-- Check if operation_orders table exists
IF EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'operation_orders')
BEGIN
    PRINT 'operation_orders table exists';
    SELECT COUNT(*) as order_count FROM operation_orders;
END
ELSE
BEGIN
    PRINT 'WARNING: operation_orders table does not exist';
END
