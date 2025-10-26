-- Debug script to check operation manager login issues
-- Run this to diagnose why login is failing even with correct credentials

USE cateringDB;

PRINT '=== OPERATION MANAGER LOGIN DEBUG ===';

-- 1. Check if managers table exists
PRINT '1. Checking managers table existence...';
IF EXISTS (SELECT * FROM sysobjects WHERE name='managers' AND xtype='U')
BEGIN
    PRINT '✓ managers table exists';
    
    -- 2. Check table structure
    PRINT '2. Checking table structure...';
    SELECT 
        COLUMN_NAME,
        DATA_TYPE,
        IS_NULLABLE,
        CHARACTER_MAXIMUM_LENGTH
    FROM INFORMATION_SCHEMA.COLUMNS 
    WHERE TABLE_NAME = 'managers'
    ORDER BY ORDINAL_POSITION;
    
    -- 3. Check if table has any data
    PRINT '3. Checking table data...';
    SELECT COUNT(*) as manager_count FROM managers;
    
    -- 4. Show all managers
    PRINT '4. Showing all managers...';
    SELECT id, username, password, fullName, email, phone FROM managers;
    
    -- 5. Test specific login query
    PRINT '5. Testing login query for admin...';
    SELECT id, username, password FROM managers WHERE username = 'admin' AND password = 'admin123';
    
    -- 6. Test login query for manager
    PRINT '6. Testing login query for manager...';
    SELECT id, username, password FROM managers WHERE username = 'manager' AND password = 'manager123';
    
    -- 7. Check for any data type issues
    PRINT '7. Checking data types...';
    SELECT 
        username,
        LEN(username) as username_length,
        password,
        LEN(password) as password_length
    FROM managers;
    
END
ELSE
BEGIN
    PRINT '❌ managers table does not exist!';
    PRINT 'You need to run setup-operation-database.sql first';
END

-- 8. Check database connection settings
PRINT '8. Checking database connection...';
SELECT 
    DB_NAME() as current_database,
    USER_NAME() as current_user,
    @@SERVERNAME as server_name;

PRINT '=== DEBUG COMPLETE ===';
