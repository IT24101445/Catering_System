# Test Script for Driver Assignment System

## Testing the Create by Name Functionality

### 1. First, create some test data:

#### Create a Driver:
```bash
curl -X POST http://localhost:8080/api/drivers \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john.doe@example.com",
    "password": "password123",
    "status": "AVAILABLE"
  }'
```

#### Create an Order:
```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "customerName": "Jane Smith",
    "pickupAddress": "123 Main St, City",
    "dropoffAddress": "456 Oak Ave, City"
  }'
```

### 2. Test the repository methods:

#### Test driver lookup:
```bash
curl http://localhost:8080/api/test/drivers/by-name/John%20Doe
```

#### Test order lookup:
```bash
curl http://localhost:8080/api/test/orders/by-customer/Jane%20Smith
```

### 3. Test assignment creation by name:

```bash
curl -X POST http://localhost:8080/api/assignments/by-name \
  -H "Content-Type: application/json" \
  -d '{
    "customerName": "Jane Smith",
    "driverName": "John Doe",
    "route": "Take Main St to Oak Ave"
  }'
```

### 4. Check if assignment was created:

```bash
curl http://localhost:8080/api/assignments
```

### 5. Check if notification was created:

```bash
curl http://localhost:8080/api/notifications/driver/1
```

## Common Issues and Solutions:

1. **"Order not found"**: Make sure the customer name matches exactly (case insensitive)
2. **"Driver not found"**: Make sure the driver name matches exactly (case insensitive)
3. **"Driver not available"**: Make sure the driver status is "AVAILABLE"
4. **Circular dependency**: The @Lazy annotation should resolve this

## Debug Information:

The system will log:
- Assignment creation attempts
- Repository lookup results
- Any errors that occur

Check the console output for detailed error messages.
