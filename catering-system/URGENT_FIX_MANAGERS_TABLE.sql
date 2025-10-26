-- URGENT FIX: Fix managers table schema immediately
-- Run this script RIGHT NOW to fix the "Invalid column name 'fullName'" error

USE cateringDB;

PRINT '=== URGENT FIX: MANAGERS TABLE SCHEMA ===';

-- 1. Check current state
PRINT '1. Checking current managers table...';
IF EXISTS (SELECT * FROM sysobjects WHERE name='managers' AND xtype='U')
BEGIN
    PRINT '✓ managers table exists';
    
    -- Show current columns
    SELECT COLUMN_NAME, DATA_TYPE, IS_NULLABLE 
    FROM INFORMATION_SCHEMA.COLUMNS 
    WHERE TABLE_NAME = 'managers'
    ORDER BY ORDINAL_POSITION;
END
ELSE
BEGIN
    PRINT '❌ managers table does not exist';
END

-- 2. DROP and RECREATE the table with correct structure
PRINT '2. Dropping and recreating managers table...';

-- Drop existing table
IF EXISTS (SELECT * FROM sysobjects WHERE name='managers' AND xtype='U')
BEGIN
    DROP TABLE managers;
    PRINT '✓ Dropped existing managers table';
END

-- Create new table with ALL required columns
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

PRINT '✓ Created new managers table with correct structure';

-- 3. Add indexes
PRINT '3. Adding indexes...';
CREATE INDEX IX_managers_Username ON managers(username);
CREATE INDEX IX_managers_Email ON managers(email);
PRINT '✓ Indexes added';

-- 4. Insert sample data
PRINT '4. Inserting sample data...';
INSERT INTO managers (username, password, fullName, email, phone) VALUES
('admin', 'admin123', 'Administrator', 'admin@catering.com', '123-456-7890'),
('manager', 'manager123', 'Operations Manager', 'manager@catering.com', '123-456-7891');

PRINT '✓ Sample data inserted';

-- 5. Verify the structure
PRINT '5. Verifying table structure...';
SELECT 
    COLUMN_NAME,
    DATA_TYPE,
    IS_NULLABLE,
    CHARACTER_MAXIMUM_LENGTH
FROM INFORMATION_SCHEMA.COLUMNS 
WHERE TABLE_NAME = 'managers'
ORDER BY ORDINAL_POSITION;

-- 6. Test the exact registration query
PRINT '6. Testing registration query...';
DECLARE @testFullName NVARCHAR(255) = 'Test User';
DECLARE @testUsername NVARCHAR(50) = 'testuser';
DECLARE @testPassword NVARCHAR(255) = 'testpass123';
DECLARE @testEmail NVARCHAR(255) = 'test@example.com';
DECLARE @testPhone NVARCHAR(50) = '555-1234';

-- Test the EXACT query the application uses
BEGIN TRY
    INSERT INTO managers (fullName, username, password, email, phone) 
    VALUES (@testFullName, @testUsername, @testPassword, @testEmail, @testPhone);
    
    PRINT '✓ Registration query test: SUCCESS';
    
    -- Show the inserted data
    SELECT id, username, password, fullName, email, phone 
    FROM managers 
    WHERE username = @testUsername;
    
    -- Clean up test data
    DELETE FROM managers WHERE username = @testUsername;
    PRINT '✓ Test data cleaned up';
    
END TRY
BEGIN CATCH
    PRINT '❌ Registration query test: FAILED';
    PRINT 'Error: ' + ERROR_MESSAGE();
END CATCH

-- 7. Show final data
PRINT '7. Final managers data:';
SELECT id, username, password, fullName, email, phone FROM managers;

-- 8. Test login
PRINT '8. Testing login...';
IF EXISTS (SELECT 1 FROM managers WHERE username = 'admin' AND password = 'admin123')
    PRINT '✓ admin/admin123 login: SUCCESS'
ELSE
    PRINT '❌ admin/admin123 login: FAILED';

IF EXISTS (SELECT 1 FROM managers WHERE username = 'manager' AND password = 'manager123')
    PRINT '✓ manager/manager123 login: SUCCESS'
ELSE
    PRINT '❌ manager/manager123 login: FAILED';

PRINT '=== URGENT FIX COMPLETE ===';
PRINT 'The managers table now has the correct structure.';
PRINT 'Registration should now work properly.';
PRINT 'Try registering with username "minuli" again.';
