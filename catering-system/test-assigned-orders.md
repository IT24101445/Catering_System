# Test Assigned Orders Functionality

## How to Test:

1. **Start the application**:
   ```bash
   cd catering-system
   .\mvnw.cmd spring-boot:run
   ```

2. **Login to Operation Manager**:
   - Go to: http://localhost:8080/operation/login
   - Username: `admin`
   - Password: `admin123`

3. **Check Dashboard**:
   - You should see 2 pending orders: "ABC Corporation" and "XYZ Events"
   - You should see 3 available staff members

4. **Assign an Order**:
   - In the dashboard, assign "ABC Corporation" order to "John Smith"
   - This should create an assigned order

5. **View Assigned Orders**:
   - Click on "Assigned Orders" link
   - You should now see the assigned order in the table with:
     - Order ID: 1
     - Customer Name: ABC Corporation
     - Order Details: Corporate lunch for 50 people
     - Staff Name: John Smith
     - Staff Role: Chef
     - Status: ASSIGNED

## Expected Results:

- ✅ **Sample Data**: 1 pre-assigned order should be visible
- ✅ **Order Assignment**: New assignments should appear in the table
- ✅ **Full Details**: All order and staff information should be displayed
- ✅ **Status Updates**: Orders should show as "ASSIGNED" status

## Debug Information:

The console should show:
```
=== ASSIGNED ORDERS DEBUG ===
Getting assigned orders: 1
Returning 1 populated assigned orders
Found 1 assigned orders
Order: 1 - ABC Corporation - John Smith
```
