-- Fix managers table schema - add missing columns
-- This script fixes the "Invalid column name 'fullName'" error

USE cateringDB;

PRINT '=== FIXING MANAGERS TABLE SCHEMA ===';

-- 1. Check current table structure
PRINT '1. Checking current table structure...';
SELECT 
    COLUMN_NAME,
    DATA_TYPE,
    IS_NULLABLE,
    CHARACTER_MAXIMUM_LENGTH
FROM INFORMATION_SCHEMA.COLUMNS 
WHERE TABLE_NAME = 'managers'
ORDER BY ORDINAL_POSITION;

-- 2. Drop existing table if it has wrong structure
PRINT '2. Dropping existing managers table...';
IF EXISTS (SELECT * FROM sysobjects WHERE name='managers' AND xtype='U')
BEGIN
    DROP TABLE managers;
    PRINT '✓ Existing managers table dropped';
END
ELSE
BEGIN
    PRINT '✓ No existing managers table found';
END

-- 3. Create managers table with correct structure
PRINT '3. Creating managers table with correct structure...';
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

-- 4. Add indexes for better performance
PRINT '4. Adding indexes...';
CREATE INDEX IX_managers_Username ON managers(username);
CREATE INDEX IX_managers_Email ON managers(email);

-- 5. Insert sample managers
PRINT '5. Inserting sample managers...';
INSERT INTO managers (username, password, fullName, email, phone) VALUES
('admin', 'admin123', 'Administrator', 'admin@catering.com', '123-456-7890'),
('manager', 'manager123', 'Operations Manager', 'manager@catering.com', '123-456-7891');

-- 6. Verify the table structure
PRINT '6. Verifying table structure...';
SELECT 
    COLUMN_NAME,
    DATA_TYPE,
    IS_NULLABLE,
    CHARACTER_MAXIMUM_LENGTH
FROM INFORMATION_SCHEMA.COLUMNS 
WHERE TABLE_NAME = 'managers'
ORDER BY ORDINAL_POSITION;

-- 7. Test the data
PRINT '7. Testing sample data...';
SELECT id, username, password, fullName, email, phone FROM managers;

-- 8. Test registration query (simulate what the application does)
PRINT '8. Testing registration query...';
DECLARE @testFullName NVARCHAR(255) = 'Test User';
DECLARE @testUsername NVARCHAR(50) = 'testuser';
DECLARE @testPassword NVARCHAR(255) = 'testpass123';
DECLARE @testEmail NVARCHAR(255) = 'test@example.com';
DECLARE @testPhone NVARCHAR(50) = '555-1234';

-- Test the exact query the application uses
INSERT INTO managers (fullName, username, password, email, phone) 
VALUES (@testFullName, @testUsername, @testPassword, @testEmail, @testPhone);

-- Verify the test data was inserted
SELECT id, username, password, fullName, email, phone FROM managers WHERE username = @testUsername;

-- Clean up test data
DELETE FROM managers WHERE username = @testUsername;

PRINT '=== MANAGERS TABLE SCHEMA FIXED ===';
PRINT 'The managers table now has the correct structure with all required columns.';
PRINT 'Registration should now work properly.';
