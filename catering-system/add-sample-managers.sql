-- Simple script to just add sample managers
-- Use this if you're getting "already exist" errors

USE cateringDB;

PRINT '=== ADDING SAMPLE MANAGERS ===';

-- 1. Remove existing sample managers to avoid conflicts
PRINT '1. Removing existing sample managers...';
DELETE FROM managers WHERE username IN ('admin', 'manager');

-- 2. Add fresh sample managers
PRINT '2. Adding sample managers...';
INSERT INTO managers (username, password, fullName, email, phone) VALUES
('admin', 'admin123', 'Administrator', 'admin@catering.com', '123-456-7890'),
('manager', 'manager123', 'Operations Manager', 'manager@catering.com', '123-456-7891');

-- 3. Verify they were added
PRINT '3. Verifying sample managers...';
SELECT id, username, password, fullName FROM managers WHERE username IN ('admin', 'manager');

-- 4. Test login
PRINT '4. Testing login...';
SELECT 
    CASE WHEN EXISTS(SELECT 1 FROM managers WHERE username = 'admin' AND password = 'admin123')
         THEN 'SUCCESS' ELSE 'FAILED' END as admin_test,
    CASE WHEN EXISTS(SELECT 1 FROM managers WHERE username = 'manager' AND password = 'manager123')
         THEN 'SUCCESS' ELSE 'FAILED' END as manager_test;

PRINT '=== SAMPLE MANAGERS ADDED ===';
PRINT 'You can now login with:';
PRINT '- Username: admin, Password: admin123';
PRINT '- Username: manager, Password: manager123';
