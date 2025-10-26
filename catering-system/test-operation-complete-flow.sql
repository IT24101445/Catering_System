-- Complete test for operation manager registration and login
-- This script tests the entire flow from registration to login

USE cateringDB;

PRINT '=== COMPLETE OPERATION MANAGER FLOW TEST ===';

-- 1. Check database state
PRINT '1. Checking database state...';
IF EXISTS (SELECT * FROM sysobjects WHERE name='managers' AND xtype='U')
BEGIN
    PRINT '✓ managers table exists';
    
    -- Show table structure
    SELECT 
        COLUMN_NAME,
        DATA_TYPE,
        IS_NULLABLE,
        CHARACTER_MAXIMUM_LENGTH
    FROM INFORMATION_SCHEMA.COLUMNS 
    WHERE TABLE_NAME = 'managers'
    ORDER BY ORDINAL_POSITION;
    
    -- Show current data
    PRINT 'Current managers:';
    SELECT id, username, password, fullName, email, phone FROM managers;
    
END
ELSE
BEGIN
    PRINT '❌ managers table does not exist';
    PRINT 'Please run URGENT_FIX_MANAGERS_TABLE.sql first';
    RETURN;
END

-- 2. Test registration with new user
PRINT '2. Testing registration with new user...';
DECLARE @testFullName NVARCHAR(255) = 'Test User';
DECLARE @testUsername NVARCHAR(50) = 'testuser' + CAST(ABS(CHECKSUM(NEWID())) AS NVARCHAR(10));
DECLARE @testPassword NVARCHAR(255) = 'testpass123';
DECLARE @testEmail NVARCHAR(255) = 'test@example.com';
DECLARE @testPhone NVARCHAR(50) = '555-1234';

PRINT 'Using test username: ' + @testUsername;

-- Test username availability check (what the app does first)
DECLARE @usernameExists BIT = 0;
SELECT @usernameExists = 1 FROM managers WHERE username = @testUsername;
IF @usernameExists = 1
BEGIN
    PRINT '❌ Username already exists (unexpected)';
    SET @testUsername = @testUsername + '2';
    PRINT 'Using alternative username: ' + @testUsername;
END

-- Test registration (exact query the app uses)
BEGIN TRY
    INSERT INTO managers (fullName, username, password, email, phone) 
    VALUES (@testFullName, @testUsername, @testPassword, @testEmail, @testPhone);
    
    PRINT '✓ Registration test: SUCCESS';
    
    -- Verify the data was inserted correctly
    SELECT id, username, password, fullName, email, phone 
    FROM managers 
    WHERE username = @testUsername;
    
END TRY
BEGIN CATCH
    PRINT '❌ Registration test: FAILED';
    PRINT 'Error: ' + ERROR_MESSAGE();
    RETURN;
END CATCH

-- 3. Test login with newly registered user
PRINT '3. Testing login with newly registered user...';
IF EXISTS (SELECT 1 FROM managers WHERE username = @testUsername AND password = @testPassword)
BEGIN
    PRINT '✓ Login test with new user: SUCCESS';
    
    -- Show the manager details
    SELECT id, username, password, fullName, email, phone 
    FROM managers 
    WHERE username = @testUsername;
END
ELSE
BEGIN
    PRINT '❌ Login test with new user: FAILED';
END

-- 4. Test login with existing sample users
PRINT '4. Testing login with existing sample users...';

-- Test admin login
IF EXISTS (SELECT 1 FROM managers WHERE username = 'admin' AND password = 'admin123')
    PRINT '✓ admin/admin123 login: SUCCESS'
ELSE
    PRINT '❌ admin/admin123 login: FAILED';

-- Test manager login
IF EXISTS (SELECT 1 FROM managers WHERE username = 'manager' AND password = 'manager123')
    PRINT '✓ manager/manager123 login: SUCCESS'
ELSE
    PRINT '❌ manager/manager123 login: FAILED';

-- 5. Test duplicate username prevention
PRINT '5. Testing duplicate username prevention...';
BEGIN TRY
    INSERT INTO managers (fullName, username, password, email, phone) 
    VALUES ('Another User', @testUsername, 'anotherpass', 'another@example.com', '555-5678');
    PRINT '❌ Duplicate username was allowed (this should not happen)';
END TRY
BEGIN CATCH
    IF ERROR_NUMBER() = 2627
        PRINT '✓ Duplicate username correctly prevented'
    ELSE
        PRINT '❌ Unexpected error: ' + ERROR_MESSAGE();
END CATCH

-- 6. Test invalid login
PRINT '6. Testing invalid login...';
IF EXISTS (SELECT 1 FROM managers WHERE username = 'nonexistent' AND password = 'wrongpass')
    PRINT '❌ Invalid login was accepted (this should not happen)'
ELSE
    PRINT '✓ Invalid login correctly rejected';

-- 7. Show final state
PRINT '7. Final database state:';
SELECT 
    'Total managers:' as status,
    COUNT(*) as count 
FROM managers;

SELECT id, username, password, fullName, email, phone FROM managers ORDER BY id;

-- 8. Clean up test data
PRINT '8. Cleaning up test data...';
DELETE FROM managers WHERE username = @testUsername;
PRINT '✓ Test data cleaned up';

-- 9. Final verification
PRINT '9. Final verification...';
SELECT 
    'Final manager count:' as status,
    COUNT(*) as count 
FROM managers;

PRINT '=== COMPLETE FLOW TEST FINISHED ===';
PRINT 'If all tests passed, both registration and login are working correctly.';
PRINT 'You can now:';
PRINT '1. Register new users through the web interface';
PRINT '2. Login with existing users (admin/admin123, manager/manager123)';
PRINT '3. Login with newly registered users';
