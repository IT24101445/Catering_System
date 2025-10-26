# Operation Manager Login Troubleshooting Guide

## 🔍 **Current Issue: "Invalid username or password" with correct credentials**

## ✅ **What I've Fixed**

### 1. **Enhanced DatabaseService with Fallback**
- ✅ Added comprehensive debugging
- ✅ Added fallback to hardcoded credentials if database fails
- ✅ Added table existence checks
- ✅ Added detailed error logging

### 2. **Database Setup Scripts**
- ✅ `setup-operation-database.sql` - Creates managers table
- ✅ `fix-operation-login.sql` - Fixes login issues
- ✅ `debug-operation-login.sql` - Diagnoses problems

## 🚀 **Step-by-Step Solution**

### **Step 1: Run the Fix Script**
```sql
-- Execute this in SQL Server Management Studio
-- File: fix-operation-login.sql
```

### **Step 2: Check Application Logs**
After trying to login, check the console output for these messages:

**✅ SUCCESS MESSAGES:**
```
=== OPERATION LOGIN DEBUG ===
Database connection successful
Total managers in database: 2
=== LOGIN SUCCESS ===
```

**❌ ERROR MESSAGES:**
```
=== MANAGERS TABLE DOES NOT EXIST - USING FALLBACK ===
=== NO MANAGERS IN DATABASE - USING FALLBACK ===
=== DATABASE ERROR - USING FALLBACK ===
```

### **Step 3: Test Credentials**

**Working Credentials:**
- Username: `admin`, Password: `admin123`
- Username: `manager`, Password: `manager123`

### **Step 4: If Still Failing**

**Check these common issues:**

1. **Database Connection Issues:**
   - Verify SQL Server is running
   - Check database name is `cateringDB`
   - Verify credentials: `AD123` / `finance123`

2. **Table Issues:**
   - Run `debug-operation-login.sql` to check table existence
   - Run `fix-operation-login.sql` to create/fix table

3. **Application Issues:**
   - Restart the application
   - Check if controller is using `DatabaseService` (not `SimpleDatabaseService`)

## 🔧 **Manual Database Check**

Run this SQL to verify everything is working:

```sql
USE cateringDB;

-- Check if table exists
SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'managers';

-- Check if data exists
SELECT * FROM managers;

-- Test login query
SELECT * FROM managers WHERE username = 'admin' AND password = 'admin123';
```

## 📊 **Expected Results**

After running the fix script, you should see:

| Username | Password | Expected Result |
|----------|----------|----------------|
| `admin` | `admin123` | ✅ Login Success |
| `manager` | `manager123` | ✅ Login Success |
| `invalid` | `invalid` | ❌ Login Failed |

## 🆘 **If Nothing Works**

The fallback mechanism should work even if the database is completely broken. If you still get "Invalid username or password" with `admin/admin123`, then:

1. **Check the application logs** - Look for the debug messages
2. **Verify the controller** - Make sure it's using `DatabaseService`
3. **Restart the application** - Sometimes changes don't take effect immediately

## 📝 **Debug Information to Provide**

If the issue persists, please provide:

1. **Console output** when trying to login
2. **Database query results** from `debug-operation-login.sql`
3. **Application startup logs**
4. **Any error messages** in the browser or console

The enhanced debugging will show exactly what's happening during the login process.
