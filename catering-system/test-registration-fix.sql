-- Test registration after fixing the schema
-- This script verifies that registration will work

USE cateringDB;

PRINT '=== TESTING REGISTRATION AFTER SCHEMA FIX ===';

-- 1. Check if managers table exists and has correct structure
PRINT '1. Checking table structure...';
IF EXISTS (SELECT * FROM sysobjects WHERE name='managers' AND xtype='U')
BEGIN
    PRINT '✓ managers table exists';
    
    -- Check for required columns
    DECLARE @hasFullName BIT = 0;
    DECLARE @hasUsername BIT = 0;
    DECLARE @hasPassword BIT = 0;
    DECLARE @hasEmail BIT = 0;
    DECLARE @hasPhone BIT = 0;
    
    SELECT @hasFullName = 1 FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'managers' AND COLUMN_NAME = 'fullName';
    SELECT @hasUsername = 1 FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'managers' AND COLUMN_NAME = 'username';
    SELECT @hasPassword = 1 FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'managers' AND COLUMN_NAME = 'password';
    SELECT @hasEmail = 1 FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'managers' AND COLUMN_NAME = 'email';
    SELECT @hasPhone = 1 FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'managers' AND COLUMN_NAME = 'phone';
    
    PRINT 'Column check:';
    PRINT 'fullName: ' + CASE WHEN @hasFullName = 1 THEN '✓' ELSE '❌' END;
    PRINT 'username: ' + CASE WHEN @hasUsername = 1 THEN '✓' ELSE '❌' END;
    PRINT 'password: ' + CASE WHEN @hasPassword = 1 THEN '✓' ELSE '❌' END;
    PRINT 'email: ' + CASE WHEN @hasEmail = 1 THEN '✓' ELSE '❌' END;
    PRINT 'phone: ' + CASE WHEN @hasPhone = 1 THEN '✓' ELSE '❌' END;
    
    -- 2. Test registration with a unique username
    PRINT '2. Testing registration...';
    DECLARE @testFullName NVARCHAR(255) = 'Test User';
    DECLARE @testUsername NVARCHAR(50) = 'testuser' + CAST(ABS(CHECKSUM(NEWID())) AS NVARCHAR(10));
    DECLARE @testPassword NVARCHAR(255) = 'testpass123';
    DECLARE @testEmail NVARCHAR(255) = 'test@example.com';
    DECLARE @testPhone NVARCHAR(50) = '555-1234';
    
    PRINT 'Using test username: ' + @testUsername;
    
    -- Test the exact INSERT query the application uses
    BEGIN TRY
        INSERT INTO managers (fullName, username, password, email, phone) 
        VALUES (@testFullName, @testUsername, @testPassword, @testEmail, @testPhone);
        
        PRINT '✓ Registration test: SUCCESS';
        
        -- Verify the data was inserted correctly
        SELECT id, username, password, fullName, email, phone 
        FROM managers 
        WHERE username = @testUsername;
        
        -- Clean up test data
        DELETE FROM managers WHERE username = @testUsername;
        PRINT '✓ Test data cleaned up';
        
    END TRY
    BEGIN CATCH
        PRINT '❌ Registration test: FAILED';
        PRINT 'Error: ' + ERROR_MESSAGE();
    END CATCH
    
    -- 3. Test login with existing users
    PRINT '3. Testing login with existing users...';
    
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
    
    -- 4. Show current managers
    PRINT '4. Current managers:';
    SELECT id, username, password, fullName, email, phone FROM managers;
    
END
ELSE
BEGIN
    PRINT '❌ managers table does not exist';
    PRINT 'Please run fix-managers-table-schema.sql first';
END

PRINT '=== REGISTRATION TEST COMPLETE ===';
PRINT 'If all tests passed, registration should now work in the application.';
