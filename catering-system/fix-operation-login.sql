-- Fix script for operation manager login issues
-- Run this to ensure managers table exists and has sample data

USE cateringDB;

PRINT '=== FIXING OPERATION MANAGER LOGIN ===';

-- 1. Create managers table if it doesn't exist
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='managers' AND xtype='U')
BEGIN
    PRINT 'Creating managers table...';
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
    
    -- Add indexes
    CREATE INDEX IX_managers_Username ON managers(username);
    CREATE INDEX IX_managers_Email ON managers(email);
    
    PRINT '✓ managers table created';
END
ELSE
BEGIN
    PRINT '✓ managers table already exists';
END

-- 2. Clear existing sample data to avoid duplicates
PRINT 'Clearing existing sample data...';
DELETE FROM managers WHERE username IN ('admin', 'manager');

-- 3. Insert sample managers
PRINT 'Inserting sample managers...';
INSERT INTO managers (username, password, fullName, email, phone) VALUES
('admin', 'admin123', 'Administrator', 'admin@catering.com', '123-456-7890'),
('manager', 'manager123', 'Operations Manager', 'manager@catering.com', '123-456-7891');

-- 4. Verify the data was inserted
PRINT 'Verifying sample managers...';
SELECT id, username, password, fullName, email, phone FROM managers;

-- 5. Test login queries
PRINT 'Testing login queries...';

-- Test admin login
SELECT 'Admin login test:' as test_type, 
       CASE WHEN EXISTS(SELECT 1 FROM managers WHERE username = 'admin' AND password = 'admin123') 
            THEN 'SUCCESS' ELSE 'FAILED' END as result;

-- Test manager login  
SELECT 'Manager login test:' as test_type,
       CASE WHEN EXISTS(SELECT 1 FROM managers WHERE username = 'manager' AND password = 'manager123')
            THEN 'SUCCESS' ELSE 'FAILED' END as result;

-- 6. Show final status
PRINT '=== LOGIN FIX COMPLETE ===';
PRINT 'Sample managers added:';
PRINT '- Username: admin, Password: admin123';
PRINT '- Username: manager, Password: manager123';
PRINT 'You can now login with these credentials.';
