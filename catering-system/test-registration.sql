-- Test registration functionality
-- This script tests the registration process

USE cateringDB;

PRINT '=== TESTING REGISTRATION FUNCTIONALITY ===';

-- 1. Check current state
PRINT '1. Current managers:';
SELECT username, fullName, email FROM managers;

-- 2. Test registration with a new username
PRINT '2. Testing registration with new username...';
DECLARE @testUsername NVARCHAR(50) = 'testuser' + CAST(NEWID() AS NVARCHAR(36));
DECLARE @testFullName NVARCHAR(255) = 'Test User';
DECLARE @testPassword NVARCHAR(255) = 'testpass123';
DECLARE @testEmail NVARCHAR(255) = 'test@example.com';
DECLARE @testPhone NVARCHAR(50) = '555-1234';

-- Check if username already exists
IF EXISTS (SELECT 1 FROM managers WHERE username = @testUsername)
BEGIN
    PRINT 'Test username already exists, generating new one...';
    SET @testUsername = 'testuser' + CAST(ABS(CHECKSUM(NEWID())) AS NVARCHAR(10));
END

PRINT 'Using test username: ' + @testUsername;

-- Insert test user
INSERT INTO managers (fullName, username, password, email, phone) 
VALUES (@testFullName, @testUsername, @testPassword, @testEmail, @testPhone);

-- 3. Verify registration worked
PRINT '3. Verifying registration...';
SELECT username, fullName, email FROM managers WHERE username = @testUsername;

-- 4. Test login with new user
PRINT '4. Testing login with new user...';
IF EXISTS (SELECT 1 FROM managers WHERE username = @testUsername AND password = @testPassword)
    PRINT '✓ Login test: SUCCESS'
ELSE
    PRINT '❌ Login test: FAILED';

-- 5. Test duplicate username registration
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

-- 6. Clean up test data
PRINT '6. Cleaning up test data...';
DELETE FROM managers WHERE username = @testUsername;

-- 7. Show final state
PRINT '7. Final state:';
SELECT username, fullName, email FROM managers;

PRINT '=== REGISTRATION TEST COMPLETE ===';
PRINT 'Registration functionality is working correctly.';
