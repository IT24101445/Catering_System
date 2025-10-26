-- Add missing columns to existing managers table
-- Use this if you want to keep existing data

USE cateringDB;

PRINT '=== ADDING MISSING COLUMNS TO MANAGERS TABLE ===';

-- 1. Check if managers table exists
IF EXISTS (SELECT * FROM sysobjects WHERE name='managers' AND xtype='U')
BEGIN
    PRINT '✓ managers table exists';
    
    -- 2. Check current columns
    PRINT '2. Current columns:';
    SELECT COLUMN_NAME, DATA_TYPE, IS_NULLABLE 
    FROM INFORMATION_SCHEMA.COLUMNS 
    WHERE TABLE_NAME = 'managers'
    ORDER BY ORDINAL_POSITION;
    
    -- 3. Add missing columns
    PRINT '3. Adding missing columns...';
    
    -- Add fullName column if it doesn't exist
    IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'managers' AND COLUMN_NAME = 'fullName')
    BEGIN
        ALTER TABLE managers ADD fullName NVARCHAR(255) NOT NULL DEFAULT 'Unknown';
        PRINT '✓ Added fullName column';
    END
    ELSE
    BEGIN
        PRINT '✓ fullName column already exists';
    END
    
    -- Add email column if it doesn't exist
    IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'managers' AND COLUMN_NAME = 'email')
    BEGIN
        ALTER TABLE managers ADD email NVARCHAR(255);
        PRINT '✓ Added email column';
    END
    ELSE
    BEGIN
        PRINT '✓ email column already exists';
    END
    
    -- Add phone column if it doesn't exist
    IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'managers' AND COLUMN_NAME = 'phone')
    BEGIN
        ALTER TABLE managers ADD phone NVARCHAR(50);
        PRINT '✓ Added phone column';
    END
    ELSE
    BEGIN
        PRINT '✓ phone column already exists';
    END
    
    -- Add created_at column if it doesn't exist
    IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'managers' AND COLUMN_NAME = 'created_at')
    BEGIN
        ALTER TABLE managers ADD created_at DATETIME2 DEFAULT GETDATE();
        PRINT '✓ Added created_at column';
    END
    ELSE
    BEGIN
        PRINT '✓ created_at column already exists';
    END
    
    -- Add updated_at column if it doesn't exist
    IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'managers' AND COLUMN_NAME = 'updated_at')
    BEGIN
        ALTER TABLE managers ADD updated_at DATETIME2 DEFAULT GETDATE();
        PRINT '✓ Added updated_at column';
    END
    ELSE
    BEGIN
        PRINT '✓ updated_at column already exists';
    END
    
    -- 4. Update existing records with default values
    PRINT '4. Updating existing records...';
    UPDATE managers SET fullName = 'Administrator' WHERE username = 'admin' AND (fullName IS NULL OR fullName = 'Unknown');
    UPDATE managers SET fullName = 'Operations Manager' WHERE username = 'manager' AND (fullName IS NULL OR fullName = 'Unknown');
    UPDATE managers SET email = 'admin@catering.com' WHERE username = 'admin' AND email IS NULL;
    UPDATE managers SET email = 'manager@catering.com' WHERE username = 'manager' AND email IS NULL;
    UPDATE managers SET phone = '123-456-7890' WHERE username = 'admin' AND phone IS NULL;
    UPDATE managers SET phone = '123-456-7891' WHERE username = 'manager' AND phone IS NULL;
    
    -- 5. Show final table structure
    PRINT '5. Final table structure:';
    SELECT COLUMN_NAME, DATA_TYPE, IS_NULLABLE, CHARACTER_MAXIMUM_LENGTH
    FROM INFORMATION_SCHEMA.COLUMNS 
    WHERE TABLE_NAME = 'managers'
    ORDER BY ORDINAL_POSITION;
    
    -- 6. Show current data
    PRINT '6. Current data:';
    SELECT id, username, password, fullName, email, phone FROM managers;
    
    -- 7. Test registration query
    PRINT '7. Testing registration query...';
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
    
    PRINT '✓ Registration query test successful';
    
END
ELSE
BEGIN
    PRINT '❌ managers table does not exist';
    PRINT 'Please run fix-managers-table-schema.sql first';
END

PRINT '=== MISSING COLUMNS ADDED ===';
PRINT 'The managers table now has all required columns.';
PRINT 'Registration should now work properly.';
