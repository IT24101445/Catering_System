-- Create all missing tables for the kitchen module
-- Run this script to set up the complete kitchen database schema

-- 1. Create ChefSchedules table
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
    
    -- Add indexes
    CREATE INDEX IX_ChefSchedules_EventId ON ChefSchedules(event_id);
    CREATE INDEX IX_ChefSchedules_ChefId ON ChefSchedules(chef_id);
    CREATE INDEX IX_ChefSchedules_Status ON ChefSchedules(status);
    
    PRINT 'ChefSchedules table created successfully';
END
ELSE
BEGIN
    PRINT 'ChefSchedules table already exists';
END

-- 2. Create Menus table
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='Menus' AND xtype='U')
BEGIN
    CREATE TABLE Menus (
        menu_id INT IDENTITY(1,1) PRIMARY KEY,
        name NVARCHAR(255) NOT NULL,
        status NVARCHAR(50) DEFAULT 'draft',
        event_id INT,
        created_at DATETIME2 DEFAULT GETDATE(),
        updated_at DATETIME2 DEFAULT GETDATE()
    );
    
    -- Add indexes
    CREATE INDEX IX_Menus_Status ON Menus(status);
    CREATE INDEX IX_Menus_EventId ON Menus(event_id);
    
    PRINT 'Menus table created successfully';
END
ELSE
BEGIN
    PRINT 'Menus table already exists';
END

-- 3. Add sample data to ChefSchedules
IF EXISTS (SELECT * FROM ChefSchedules)
BEGIN
    PRINT 'ChefSchedules table has data';
END
ELSE
BEGIN
    INSERT INTO ChefSchedules (event_id, chef_id, plan_text, status) VALUES
    (1, 1, 'Prepare appetizers and main course for wedding reception', 'confirmed'),
    (2, 1, 'Set up kitchen equipment and prepare ingredients', 'draft'),
    (1, 2, 'Handle dessert preparation and plating', 'confirmed');
    
    PRINT 'Sample data added to ChefSchedules';
END

-- 4. Add sample data to Menus
IF EXISTS (SELECT * FROM Menus)
BEGIN
    PRINT 'Menus table has data';
END
ELSE
BEGIN
    INSERT INTO Menus (name, status, event_id) VALUES
    ('Wedding Reception Menu', 'confirmed', 1),
    ('Corporate Lunch Menu', 'draft', 2),
    ('Birthday Party Menu', 'confirmed', 1);
    
    PRINT 'Sample data added to Menus';
END

PRINT 'Kitchen module database setup completed successfully!';
