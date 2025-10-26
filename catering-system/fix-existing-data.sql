-- Fix script for "already exist" errors in operation manager
-- This script safely handles existing data and ensures login works

USE cateringDB;

PRINT '=== FIXING EXISTING DATA CONFLICTS ===';

-- 1. Check current state
PRINT '1. Checking current state...';
IF EXISTS (SELECT * FROM sysobjects WHERE name='managers' AND xtype='U')
BEGIN
    PRINT '✓ managers table exists';
    SELECT COUNT(*) as current_managers FROM managers;
END
ELSE
BEGIN
    PRINT '❌ managers table does not exist';
END

-- 2. Clean up existing sample data to avoid conflicts
PRINT '2. Cleaning up existing sample data...';
DELETE FROM managers WHERE username IN ('admin', 'manager', 'testuser');

-- 3. Ensure managers table exists with correct structure
PRINT '3. Ensuring managers table exists...';
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='managers' AND xtype='U')
BEGIN
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

-- 4. Insert fresh sample data
PRINT '4. Inserting fresh sample data...';
INSERT INTO managers (username, password, fullName, email, phone) VALUES
('admin', 'admin123', 'Administrator', 'admin@catering.com', '123-456-7890'),
('manager', 'manager123', 'Operations Manager', 'manager@catering.com', '123-456-7891');

-- 5. Verify the data
PRINT '5. Verifying sample data...';
SELECT id, username, password, fullName, email, phone FROM managers;

-- 6. Test login queries
PRINT '6. Testing login queries...';

-- Test admin login
IF EXISTS (SELECT 1 FROM managers WHERE username = 'admin' AND password = 'admin123')
    PRINT '✓ Admin login test: SUCCESS'
ELSE
    PRINT '❌ Admin login test: FAILED';

-- Test manager login
IF EXISTS (SELECT 1 FROM managers WHERE username = 'manager' AND password = 'manager123')
    PRINT '✓ Manager login test: SUCCESS'
ELSE
    PRINT '❌ Manager login test: FAILED';

-- 7. Show final status
PRINT '7. Final status...';
SELECT 
    'Total managers:' as status,
    COUNT(*) as count 
FROM managers;

PRINT '=== FIX COMPLETE ===';
PRINT 'You can now login with:';
PRINT '- Username: admin, Password: admin123';
PRINT '- Username: manager, Password: manager123';
