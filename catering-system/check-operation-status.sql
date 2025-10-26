-- Check current status of operation manager login
-- Run this to see what's currently in the database

USE cateringDB;

PRINT '=== OPERATION MANAGER STATUS CHECK ===';

-- 1. Check if managers table exists
PRINT '1. Checking managers table...';
IF EXISTS (SELECT * FROM sysobjects WHERE name='managers' AND xtype='U')
BEGIN
    PRINT '✓ managers table exists';
    
    -- 2. Check table structure
    PRINT '2. Table structure:';
    SELECT 
        COLUMN_NAME,
        DATA_TYPE,
        IS_NULLABLE
    FROM INFORMATION_SCHEMA.COLUMNS 
    WHERE TABLE_NAME = 'managers'
    ORDER BY ORDINAL_POSITION;
    
    -- 3. Check current data
    PRINT '3. Current managers:';
    SELECT id, username, password, fullName, email, phone FROM managers;
    
    -- 4. Test login queries
    PRINT '4. Testing login queries...';
    
    -- Test admin
    IF EXISTS (SELECT 1 FROM managers WHERE username = 'admin' AND password = 'admin123')
        PRINT '✓ admin/admin123: WORKS'
    ELSE
        PRINT '❌ admin/admin123: FAILED';
    
    -- Test manager
    IF EXISTS (SELECT 1 FROM managers WHERE username = 'manager' AND password = 'manager123')
        PRINT '✓ manager/manager123: WORKS'
    ELSE
        PRINT '❌ manager/manager123: FAILED';
    
    -- 5. Check for duplicate usernames
    PRINT '5. Checking for duplicate usernames...';
    SELECT username, COUNT(*) as count 
    FROM managers 
    GROUP BY username 
    HAVING COUNT(*) > 1;
    
    -- 6. Check for empty passwords
    PRINT '6. Checking for empty passwords...';
    SELECT username, password 
    FROM managers 
    WHERE password IS NULL OR password = '';
    
END
ELSE
BEGIN
    PRINT '❌ managers table does not exist';
    PRINT 'You need to run fix-existing-data.sql first';
END

PRINT '=== STATUS CHECK COMPLETE ===';
