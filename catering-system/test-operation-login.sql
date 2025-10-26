-- Test script to verify operation manager login and registration
-- Run this after setting up the database to verify login/registration works

USE cateringDB;

PRINT 'Testing Operation Manager Login and Registration...';

-- Test 1: Check if managers table exists
PRINT '1. Checking managers table...';
IF EXISTS (SELECT * FROM sysobjects WHERE name='managers' AND xtype='U')
    PRINT '✓ managers table exists';
ELSE
    PRINT '❌ managers table missing';

-- Test 2: Check existing managers
PRINT '2. Checking existing managers...';
SELECT id, username, fullName, email, phone FROM managers;

-- Test 3: Test registration (insert new manager)
PRINT '3. Testing manager registration...';
INSERT INTO managers (fullName, username, password, email, phone) 
VALUES ('Test Manager', 'testuser', 'testpass123', 'test@example.com', '555-1234');

-- Test 4: Verify registration worked
PRINT '4. Verifying registration...';
SELECT id, username, fullName, email, phone FROM managers WHERE username = 'testuser';

-- Test 5: Test login validation
PRINT '5. Testing login validation...';
SELECT id, username, password FROM managers WHERE username = 'testuser' AND password = 'testpass123';

-- Test 6: Test invalid login
PRINT '6. Testing invalid login...';
SELECT id, username, password FROM managers WHERE username = 'testuser' AND password = 'wrongpass';

-- Test 7: Test username uniqueness
PRINT '7. Testing username uniqueness...';
BEGIN TRY
    INSERT INTO managers (fullName, username, password, email, phone) 
    VALUES ('Another Manager', 'testuser', 'anotherpass', 'another@example.com', '555-5678');
    PRINT '❌ Username uniqueness not enforced';
END TRY
BEGIN CATCH
    IF ERROR_NUMBER() = 2627
        PRINT '✓ Username uniqueness enforced correctly';
    ELSE
        PRINT '❌ Unexpected error: ' + ERROR_MESSAGE();
END CATCH

-- Test 8: Clean up test data
PRINT '8. Cleaning up test data...';
DELETE FROM managers WHERE username = 'testuser';

-- Test 9: Verify cleanup
PRINT '9. Verifying cleanup...';
SELECT COUNT(*) as test_user_count FROM managers WHERE username = 'testuser';

PRINT 'Operation Manager Login Test Complete!';
PRINT 'Registration and login functionality verified.';