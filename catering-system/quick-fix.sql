-- Quick fix for drivers table column name issue
-- Run this in SQL Server Management Studio or Azure Data Studio

-- Check current table structure
SELECT 
    COLUMN_NAME,
    DATA_TYPE,
    IS_NULLABLE,
    CHARACTER_MAXIMUM_LENGTH
FROM INFORMATION_SCHEMA.COLUMNS 
WHERE TABLE_NAME = 'drivers'
ORDER BY ORDINAL_POSITION;

-- If the table has a 'password' column instead of 'password_hash', rename it:
-- EXEC sp_rename 'drivers.password', 'password_hash', 'COLUMN';

-- Or if you want to start fresh, drop and recreate the table:
-- DROP TABLE IF EXISTS drivers;
-- Then restart the application and let Hibernate create the table with correct structure
