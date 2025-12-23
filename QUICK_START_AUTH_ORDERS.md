# 🚀 Quick Start Guide - Authentication & Orders

## Backend Requirements
✅ Spring Boot backend running on `http://localhost:8080`  
✅ Endpoints available: `/api/v1/auth/login`, `/api/v1/auth/register`, `/api/v1/orders`  

## Login Credentials (Default)
```
Email: admin@nectar.com
Password: darasmos123
```

## Test Flow

### 1️⃣ Test Login
1. Launch app
2. Navigate: Welcome → Sign In → Login
3. Click "Log In" (default credentials pre-filled)
4. ✅ Should redirect to Home screen
5. ✅ Check: User is logged in, tokens saved

### 2️⃣ Test Registration
1. Navigate to Sign Up screen
2. Enter test data:
   - Name: John Doe
   - Phone: 0123456789
   - Email: john@test.com
   - Password: test1234
3. Click "Sign Up"
4. ✅ Should show success and redirect to Login
5. Login with new credentials
6. ✅ Should work

### 3️⃣ Test Orders View
1. Make sure you're logged in
2. Go to Account tab (bottom nav)
3. Click "Orders" menu item
4. ✅ Should show empty state if no orders
5. ✅ Should show list of orders if they exist
6. ✅ Only shows orders created by current user

### 4️⃣ Test Order Creation
1. Add products to cart
2. Go to Cart → "Go to Checkout"
3. Complete payment (Bakong QR already integrated)
4. ✅ Order is created in backend
5. Go to Account → Orders
6. ✅ New order appears in list

## Key Features

### Authentication
- ✅ JWT token-based authentication
- ✅ Access token + Refresh token
- ✅ User info saved locally (name, email, uuid)
- ✅ Auto-logout on token expiry

### Orders
- ✅ Fetch all orders (with auth)
- ✅ Create order (with auth)
- ✅ Filter by current user
- ✅ Display order details (items, total, status, date)

## Files Changed
```
✅ SignUpScreen.kt          - Added gender/dob to registration
✅ LoginScreen.kt           - Using AuthViewModelFactory
✅ NavGraph.kt              - Updated success callbacks
✅ AuthViewModelFactory.kt  - NEW: Factory for AuthViewModel
```

## Files Already Working
```
✅ AuthViewModel.kt         - Login/Register logic
✅ OrderViewModel.kt        - Order creation/fetching
✅ OrdersScreen.kt          - Display & filtering
✅ SessionManager.kt        - Token & user storage
✅ NectarRepository.kt      - API calls
✅ NectarApiService.kt      - API endpoints
```

## Troubleshooting

### Login fails
- Check backend is running: `curl http://localhost:8080/actuator/health`
- Check credentials exist in database
- Check LogCat for error details

### Orders not showing
- Verify user is logged in (check SessionManager)
- Check `audit.createdBy` in backend matches username
- Verify orders exist for current user
- Check LogCat for API errors

### Empty orders list
- Normal if user hasn't created any orders yet
- Create an order through Cart → Payment flow
- Refresh by navigating away and back

## Backend API Endpoints

```
POST /api/v1/auth/register    - Register new user
POST /api/v1/auth/login       - Login user → Returns JWT
POST /api/v1/auth/refresh     - Refresh access token
GET  /api/v1/users/me         - Get current user info
GET  /api/v1/orders           - Get user's orders (requires auth)
POST /api/v1/orders           - Create order (requires auth)
```

## Configuration

### Change Backend URL
Edit `app/src/main/java/com/example/groceries_app/utils/Constants.kt`:
```kotlin
const val BASE_URL = "http://10.0.2.2:8080/"  // Emulator
// const val BASE_URL = "http://192.168.1.100:8080/"  // Physical device
```

## All Done! 🎉

Your app now has:
- ✅ Full authentication (login/register)
- ✅ JWT token management
- ✅ User-specific orders
- ✅ Order creation after payment
- ✅ Consistent UI/UX
- ✅ Error handling
- ✅ Loading states

Ready to test! Start your backend and run the app. 🚀

