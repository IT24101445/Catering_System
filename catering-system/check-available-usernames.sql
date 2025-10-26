-- Check available usernames for registration
-- Run this to see what usernames are already taken

USE cateringDB;

PRINT '=== CHECKING AVAILABLE USERNAMES ===';

-- 1. Show all existing usernames
PRINT '1. Existing usernames:';
SELECT username, fullName, email, created_at FROM managers ORDER BY username;

-- 2. Show count of existing users
PRINT '2. Total registered managers:';
SELECT COUNT(*) as total_managers FROM managers;

-- 3. Show sample usernames that are available
PRINT '3. Suggested available usernames:';
SELECT 'admin' as suggested_username, 
       CASE WHEN EXISTS(SELECT 1 FROM managers WHERE username = 'admin') 
            THEN 'TAKEN' ELSE 'AVAILABLE' END as status
UNION ALL
SELECT 'manager', 
       CASE WHEN EXISTS(SELECT 1 FROM managers WHERE username = 'manager') 
            THEN 'TAKEN' ELSE 'AVAILABLE' END
UNION ALL
SELECT 'testuser', 
       CASE WHEN EXISTS(SELECT 1 FROM managers WHERE username = 'testuser') 
            THEN 'TAKEN' ELSE 'AVAILABLE' END
UNION ALL
SELECT 'newuser', 
       CASE WHEN EXISTS(SELECT 1 FROM managers WHERE username = 'newuser') 
            THEN 'TAKEN' ELSE 'AVAILABLE' END
UNION ALL
SELECT 'demo', 
       CASE WHEN EXISTS(SELECT 1 FROM managers WHERE username = 'demo') 
            THEN 'TAKEN' ELSE 'AVAILABLE' END;

-- 4. Show login credentials for existing users
PRINT '4. Existing login credentials:';
SELECT username, password, fullName FROM managers;

PRINT '=== USERNAME CHECK COMPLETE ===';
PRINT 'Use a username that shows "AVAILABLE" for registration.';
