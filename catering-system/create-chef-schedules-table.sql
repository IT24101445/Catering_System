-- Create ChefSchedules table for kitchen module
-- This table stores chef schedules for events

CREATE TABLE ChefSchedules (
    schedule_id INT IDENTITY(1,1) PRIMARY KEY,
    event_id INT NOT NULL,
    chef_id INT NOT NULL,
    plan_text NVARCHAR(MAX),
    status NVARCHAR(50) DEFAULT 'draft',
    created_at DATETIME2 DEFAULT GETDATE(),
    updated_at DATETIME2 DEFAULT GETDATE()
);

-- Add some sample data for testing
INSERT INTO ChefSchedules (event_id, chef_id, plan_text, status) VALUES
(1, 1, 'Prepare appetizers and main course for wedding reception', 'confirmed'),
(2, 1, 'Set up kitchen equipment and prepare ingredients', 'draft'),
(1, 2, 'Handle dessert preparation and plating', 'confirmed');

-- Create index for better performance
CREATE INDEX IX_ChefSchedules_EventId ON ChefSchedules(event_id);
CREATE INDEX IX_ChefSchedules_ChefId ON ChefSchedules(chef_id);
CREATE INDEX IX_ChefSchedules_Status ON ChefSchedules(status);
