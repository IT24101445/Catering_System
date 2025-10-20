-- Complete Kitchen Module Database Setup
-- This script creates all missing tables for the kitchen module
-- Run this script in your SQL Server database

USE cateringDB; -- Replace with your actual database name

PRINT 'Starting Kitchen Module Database Setup...';

-- 1. Create Menus table
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='Menus' AND xtype='U')
BEGIN
    CREATE TABLE Menus (
        menu_id INT IDENTITY(1,1) PRIMARY KEY,
        name NVARCHAR(255) NOT NULL,
        status NVARCHAR(50) DEFAULT 'draft',
        event_id INT DEFAULT 0,
        created_at DATETIME2 DEFAULT GETDATE(),
        updated_at DATETIME2 DEFAULT GETDATE()
    );
    
    -- Add indexes for better performance
    CREATE INDEX IX_Menus_Status ON Menus(status);
    CREATE INDEX IX_Menus_EventId ON Menus(event_id);
    CREATE INDEX IX_Menus_Name ON Menus(name);
    
    PRINT '✓ Menus table created successfully';
END
ELSE
BEGIN
    PRINT '✓ Menus table already exists';
END

-- 2. Create ChefSchedules table
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='ChefSchedules' AND xtype='U')
BEGIN
    CREATE TABLE ChefSchedules (
        schedule_id INT IDENTITY(1,1) PRIMARY KEY,
        event_id INT NOT NULL,
        chef_id INT NOT NULL,
        plan_text NVARCHAR(MAX),
        status NVARCHAR(50) DEFAULT 'draft',
        created_at DATETIME2 DEFAULT GETDATE(),
        updated_at DATETIME2 DEFAULT GETDATE()
    );
    
    -- Add indexes for better performance
    CREATE INDEX IX_ChefSchedules_EventId ON ChefSchedules(event_id);
    CREATE INDEX IX_ChefSchedules_ChefId ON ChefSchedules(chef_id);
    CREATE INDEX IX_ChefSchedules_Status ON ChefSchedules(status);
    
    PRINT '✓ ChefSchedules table created successfully';
END
ELSE
BEGIN
    PRINT '✓ ChefSchedules table already exists';
END

-- 3. Add sample data to Menus table
IF NOT EXISTS (SELECT * FROM Menus WHERE name = 'Wedding Reception Menu')
BEGIN
    INSERT INTO Menus (name, status, event_id) VALUES
    ('Wedding Reception Menu', 'confirmed', 1),
    ('Corporate Lunch Menu', 'draft', 2),
    ('Birthday Party Menu', 'confirmed', 1),
    ('Anniversary Dinner Menu', 'draft', 3),
    ('Holiday Party Menu', 'confirmed', 2);
    
    PRINT '✓ Sample menu data added successfully';
END
ELSE
BEGIN
    PRINT '✓ Sample menu data already exists';
END

-- 4. Add sample data to ChefSchedules table
IF NOT EXISTS (SELECT * FROM ChefSchedules WHERE plan_text LIKE '%wedding reception%')
BEGIN
    INSERT INTO ChefSchedules (event_id, chef_id, plan_text, status) VALUES
    (1, 1, 'Prepare appetizers and main course for wedding reception. Start prep at 2 PM, serve at 6 PM.', 'confirmed'),
    (2, 1, 'Set up kitchen equipment and prepare ingredients for corporate lunch. Focus on healthy options.', 'draft'),
    (1, 2, 'Handle dessert preparation and plating for wedding. Chocolate cake and fruit platters.', 'confirmed'),
    (3, 1, 'Birthday party menu preparation. Kids-friendly food with colorful presentation.', 'draft'),
    (2, 2, 'Corporate lunch backup chef. Assist with final preparations and service.', 'confirmed');
    
    PRINT '✓ Sample schedule data added successfully';
END
ELSE
BEGIN
    PRINT '✓ Sample schedule data already exists';
END

-- 5. Verify table creation
PRINT '';
PRINT '=== VERIFICATION ===';
PRINT 'Menus table record count: ' + CAST((SELECT COUNT(*) FROM Menus) AS VARCHAR(10));
PRINT 'ChefSchedules table record count: ' + CAST((SELECT COUNT(*) FROM ChefSchedules) AS VARCHAR(10));

-- 6. Show sample data
PRINT '';
PRINT '=== SAMPLE MENUS ===';
SELECT TOP 3 menu_id, name, status, event_id FROM Menus ORDER BY menu_id DESC;

PRINT '';
PRINT '=== SAMPLE SCHEDULES ===';
SELECT TOP 3 schedule_id, event_id, chef_id, status, LEFT(plan_text, 50) + '...' as plan_preview FROM ChefSchedules ORDER BY schedule_id DESC;

PRINT '';
PRINT '🎉 Kitchen Module Database Setup Completed Successfully!';
PRINT 'You can now use the kitchen module with full functionality.';

