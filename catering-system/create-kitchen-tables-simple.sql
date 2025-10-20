-- Simple Kitchen Tables Creation
-- Run this script to create the missing tables

-- Create Menus table
CREATE TABLE Menus (
    menu_id INT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(255) NOT NULL,
    status NVARCHAR(50) DEFAULT 'draft',
    event_id INT DEFAULT 0,
    created_at DATETIME2 DEFAULT GETDATE(),
    updated_at DATETIME2 DEFAULT GETDATE()
);

-- Create ChefSchedules table
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
CREATE INDEX IX_Menus_Status ON Menus(status);
CREATE INDEX IX_Menus_EventId ON Menus(event_id);
CREATE INDEX IX_ChefSchedules_EventId ON ChefSchedules(event_id);
CREATE INDEX IX_ChefSchedules_ChefId ON ChefSchedules(chef_id);

PRINT 'Kitchen tables created successfully!';

