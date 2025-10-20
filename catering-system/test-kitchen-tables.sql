-- Test Kitchen Tables
-- Run this script to verify the tables exist and are accessible

-- Test Menus table
IF EXISTS (SELECT * FROM sysobjects WHERE name='Menus' AND xtype='U')
BEGIN
    PRINT '✓ Menus table exists';
    SELECT COUNT(*) as menu_count FROM Menus;
END
ELSE
BEGIN
    PRINT '✗ Menus table does not exist';
END

-- Test ChefSchedules table
IF EXISTS (SELECT * FROM sysobjects WHERE name='ChefSchedules' AND xtype='U')
BEGIN
    PRINT '✓ ChefSchedules table exists';
    SELECT COUNT(*) as schedule_count FROM ChefSchedules;
END
ELSE
BEGIN
    PRINT '✗ ChefSchedules table does not exist';
END

-- Test table structure
PRINT '';
PRINT '=== MENUS TABLE STRUCTURE ===';
SELECT 
    COLUMN_NAME,
    DATA_TYPE,
    IS_NULLABLE,
    COLUMN_DEFAULT
FROM INFORMATION_SCHEMA.COLUMNS 
WHERE TABLE_NAME = 'Menus'
ORDER BY ORDINAL_POSITION;

PRINT '';
PRINT '=== CHEFSCHEDULES TABLE STRUCTURE ===';
SELECT 
    COLUMN_NAME,
    DATA_TYPE,
    IS_NULLABLE,
    COLUMN_DEFAULT
FROM INFORMATION_SCHEMA.COLUMNS 
WHERE TABLE_NAME = 'ChefSchedules'
ORDER BY ORDINAL_POSITION;

