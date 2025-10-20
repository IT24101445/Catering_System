# Kitchen Integration Test Guide

## Overview
This document provides step-by-step instructions to test the kitchen module integration with the catering system.

## Prerequisites
1. Ensure the application is running: `mvn spring-boot:run`
2. Database should be accessible and tables created
3. Browser should support JavaScript and AJAX

## Test Steps

### 1. Test Kitchen Login Portal Access
1. Navigate to: `http://localhost:8080/login-portal.html`
2. Verify the "Kitchen" section is visible
3. Click on "Kitchen Portal" button
4. Should redirect to: `http://localhost:8080/kitchen/login`

### 2. Test Kitchen Registration
1. Navigate to: `http://localhost:8080/kitchen/register`
2. Fill out the registration form:
   - First Name: John
   - Last Name: Doe
   - Email: john.doe@kitchen.com
   - Password: password123
   - Position: Head Chef
3. Click "Register"
4. Should show success message and redirect to login page

### 3. Test Kitchen Login
1. Navigate to: `http://localhost:8080/kitchen/login`
2. Use the registered credentials:
   - Email: john.doe@kitchen.com
   - Password: password123
3. Click "Login"
4. Should show success message and redirect to dashboard

### 4. Test Kitchen Dashboard
1. Should be redirected to: `http://localhost:8080/kitchen/dashboard`
2. Verify dashboard elements:
   - Welcome message with staff name
   - Statistics cards (Pending Orders, In Progress, etc.)
   - Navigation menu (Home, Dashboard, Menus, Events, Notifications)
   - Logout button
3. Test navigation links
4. Test logout functionality

### 5. Test Kitchen Navigation
1. Click on "Menus" - should go to `/kitchen/menus`
2. Click on "Events" - should go to `/kitchen/events`
3. Click on "Notifications" - should go to `/kitchen/notifications`
4. Click on "Home" - should go to `/kitchen/home`

### 6. Test Session Management
1. Login to kitchen portal
2. Open new tab and navigate to kitchen dashboard
3. Should maintain login state
4. Test logout and verify session is cleared

## Expected Results

### Successful Integration Indicators:
- ✅ Kitchen login portal accessible from main login portal
- ✅ Kitchen registration form works and creates user
- ✅ Kitchen login authenticates and redirects to dashboard
- ✅ Kitchen dashboard displays with proper navigation
- ✅ Kitchen staff information displayed correctly
- ✅ Navigation between kitchen pages works
- ✅ Logout functionality works
- ✅ Session management works across tabs

### Database Verification:
```sql
-- Check if kitchen staff table exists
SELECT * FROM kitchen_staff;

-- Verify kitchen staff registration
SELECT * FROM kitchen_staff WHERE email = 'john.doe@kitchen.com';
```

## Troubleshooting

### Common Issues:
1. **404 Error on kitchen routes**: Check SecurityConfig has `/kitchen/**` in permitAll()
2. **Login not working**: Verify KitchenController endpoints are correct
3. **Dashboard not loading**: Check if KitchenStaff entity is properly mapped
4. **JavaScript errors**: Verify app.js is loaded and paths are correct

### Debug Steps:
1. Check browser console for JavaScript errors
2. Check application logs for Java errors
3. Verify database connection and table creation
4. Test individual endpoints using Postman/curl

## API Endpoints to Test

### Kitchen Authentication:
- `GET /kitchen/login` - Login page
- `POST /kitchen/login` - Login authentication
- `GET /kitchen/register` - Registration page
- `POST /kitchen/register` - User registration
- `GET /kitchen/logout` - Logout

### Kitchen Dashboard:
- `GET /kitchen/dashboard` - Main dashboard
- `GET /kitchen/home` - Home page
- `GET /kitchen/menus` - Menus page
- `GET /kitchen/events` - Events page
- `GET /kitchen/notifications` - Notifications page

## Test Data

### Sample Kitchen Staff:
- Email: chef@kitchen.com
- Password: chef123
- Position: Head Chef

### Sample Registration:
- First Name: Jane
- Last Name: Smith
- Email: jane.smith@kitchen.com
- Password: jane123
- Position: Sous Chef

## Success Criteria
All test steps should complete without errors, and the kitchen module should be fully integrated with the main catering system login portal.
