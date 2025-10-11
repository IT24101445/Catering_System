# Driver Signup and Management System

## New Features Added

### 1. Driver Signup System
- **Driver Registration Page**: `/driver/signup`
- **Self-Registration**: Drivers can create their own accounts
- **Automatic Status**: New drivers are automatically set to "AVAILABLE" status
- **Password Security**: Passwords are hashed using SHA256

### 2. Enhanced Navigation
- **Home Page**: Updated with Driver Signup and Driver Login links
- **Driver Login Page**: Added signup link for new drivers
- **Cross-linking**: Easy navigation between signup and login

### 3. Supervisor Integration
- **Automatic Visibility**: New drivers appear immediately in supervisor's driver list
- **Real-time Updates**: Supervisor can see all registered drivers
- **Status Management**: Supervisor can manage driver statuses

## User Workflow

### For New Drivers:
1. **Signup**: Visit `/driver/signup` to create account
2. **Login**: Use credentials to login at `/driver/login`
3. **Dashboard**: View and manage delivery assignments
4. **Accept/Decline**: Handle delivery assignments
5. **Track Progress**: Monitor delivery status

### For Supervisors:
1. **View Drivers**: All registered drivers appear in supervisor dashboard
2. **Assign Deliveries**: Create assignments for available drivers
3. **Monitor Status**: Track which deliveries are accepted/completed
4. **Manage Drivers**: Update driver information and status

## Technical Implementation

### Backend Changes:
- **Driver Model**: Added password hash field
- **Driver Service**: Added authentication and password handling
- **Driver Controller**: Added login endpoint
- **Page Controller**: Added signup route

### Frontend Changes:
- **Driver Signup Page**: Complete registration form with validation
- **Driver Login Page**: Enhanced with signup links
- **Driver Dashboard**: Welcome message and improved UX
- **Navigation**: Updated across all pages

### Security Features:
- **Password Hashing**: SHA256 encryption for passwords
- **Input Validation**: Client and server-side validation
- **Session Management**: Secure login/logout functionality

## API Endpoints

### Driver Management:
- `POST /api/drivers` - Create new driver (signup)
- `POST /api/drivers/login` - Driver authentication
- `GET /api/drivers` - List all drivers (supervisor view)
- `PUT /api/drivers/{id}` - Update driver information

### Assignment Management:
- `GET /api/assignments?driverId={id}` - Get driver's assignments
- `POST /api/assignments/{id}/accept` - Accept delivery
- `POST /api/assignments/{id}/decline` - Decline delivery
- `POST /api/assignments/{id}/start` - Start delivery
- `POST /api/assignments/{id}/complete` - Complete delivery

## Status Flow:
1. **PENDING** - Assignment created, waiting for driver response
2. **ACCEPTED** - Driver accepted the assignment
3. **IN_PROGRESS** - Driver started the delivery
4. **COMPLETED** - Delivery completed successfully
5. **DECLINED** - Driver declined the assignment

## Testing the System:

1. **Start Application**: Run `./mvnw spring-boot:run`
2. **Driver Signup**: Visit `http://localhost:8080/driver/signup`
3. **Create Account**: Fill out registration form
4. **Supervisor Login**: Visit `http://localhost:8080/supervisor/login`
5. **View Drivers**: Check that new driver appears in driver list
6. **Create Assignment**: Assign delivery to new driver
7. **Driver Login**: Login with new credentials
8. **Accept Assignment**: Accept the delivery assignment
9. **Monitor Status**: Check supervisor dashboard for status updates

The system now provides a complete driver onboarding and management experience!
