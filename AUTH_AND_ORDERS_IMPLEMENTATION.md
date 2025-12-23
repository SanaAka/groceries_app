# Authentication and Orders Implementation Guide

## Overview
This document describes the complete authentication and order management implementation integrated with the Spring Boot backend API.

## Authentication System

### 1. Registration (Sign Up)
**Endpoint:** `POST http://localhost:8080/api/v1/auth/register`

**Request Body:**
```json
{
  "phoneNumber": "0173493045",
  "email": "darakong@gmail.com",
  "password": "daraboy1234",
  "name": "Kong Sisovandara",
  "gender": "MALE",
  "dob": "2000-05-15"
}
```

**Response:**
```json
{
  "uuid": "23fee0a1-7afa-43bc-adf5-93e96e233f32",
  "phoneNumber": "0173493045",
  "email": "darakong@gmail.com",
  "name": "Kong Sisovandara",
  "profileImage": null,
  "gender": "MALE",
  "dob": "2000-05-15",
  "roles": ["USER"],
  "audit": {
    "createdBy": "SYSTEM",
    "createdAt": "2025-12-23T09:02:47.999543306Z"
  }
}
```

**Implementation:**
- Screen: `SignUpScreen.kt`
- ViewModel: `AuthViewModel.kt`
- Default values: gender="MALE", dob="2000-01-01"
- After successful registration, user is redirected to login screen

### 2. Login
**Endpoint:** `POST http://localhost:8080/api/v1/auth/login`

**Request Body:**
```json
{
  "email": "admin@nectar.com",
  "password": "darasmos123"
}
```

**Response:**
```json
{
  "tokenType": "Bearer",
  "accessToken": "eyJraWQiOiIzYzRmNGQ0Yi0zOTBkLTRlYzgtYjliZS1hYTk5MThhMGMzNTQi...",
  "refreshToken": "eyJraWQiOiJjMWVjMzU4MC1iNDM4LTRkMDktOWY1NC1kNGMwZGUzMjBhZWEi..."
}
```

**JWT Token Payload:**
```json
{
  "sub": "darakong@gmail.com",
  "phoneNumber": "0173493045",
  "scope": "ROLE_USER",
  "roles": "USER",
  "iss": "self",
  "name": "Kong Sisovandara",
  "exp": 1766484346,
  "iat": 1766480746,
  "uuid": "23fee0a1-7afa-43bc-adf5-93e96e233f32",
  "email": "darakong@gmail.com"
}
```

**Implementation:**
- Screen: `LoginScreen.kt`
- ViewModel: `AuthViewModel.kt`
- Default placeholder: email="admin@nectar.com", password="darasmos123"
- After successful login:
  1. Access and refresh tokens are saved to SharedPreferences
  2. User info (uuid, name, email) is fetched from `/api/v1/users/me` and saved
  3. User is redirected to home screen

**SessionManager Storage:**
- `KEY_ACCESS_TOKEN`: JWT access token
- `KEY_REFRESH_TOKEN`: JWT refresh token
- `KEY_USER_UUID`: User's unique identifier
- `KEY_USERNAME`: User's name (used for filtering orders)
- `KEY_EMAIL`: User's email
- `KEY_IS_LOGGED_IN`: Boolean flag

### 3. Token Refresh
**Endpoint:** `POST http://localhost:8080/api/v1/auth/refresh`

**Request Body:**
```json
{
  "refreshToken": "eyJraWQiOiI1ZmI5YzVlNS1jZTdlLTQxMjctOTc2Mi02NTE4NTJmY2U4YTQi..."
}
```

**Response:**
```json
{
  "tokenType": "Bearer",
  "accessToken": "new_access_token...",
  "refreshToken": "new_refresh_token..."
}
```

## Order Management

### 1. Get All Orders
**Endpoint:** `GET http://localhost:8080/api/v1/orders`

**Headers:**
```
Authorization: Bearer {accessToken}
```

**Response:**
```json
{
  "orders": [
    {
      "uuid": "bf8899ec-ef1f-45bc-9468-c743cd515ac8",
      "orderNumber": "ORD-20251112-1001",
      "totalPrice": 10.75,
      "status": "PENDING",
      "items": [],
      "audit": {
        "createdBy": "Kong Sisovandara",
        "updatedBy": "Kong Sisovandara",
        "createdAt": "2025-12-23T09:55:26.187510Z",
        "updatedAt": "2025-12-23T09:55:26.187510Z"
      }
    }
  ]
}
```

**Implementation:**
- Screen: `OrdersScreen.kt`
- ViewModel: `OrderViewModel.kt`
- **Filtering:** Orders are filtered by comparing `order.audit.createdBy` with the logged-in user's name from SessionManager
- Only orders created by the current user are displayed
- Empty state shown when no orders exist
- Error state shown on API failure

### 2. Create Order
**Endpoint:** `POST http://localhost:8080/api/v1/orders`

**Headers:**
```
Authorization: Bearer {accessToken}
```

**Request Body:**
```json
{
  "orderNumber": "ORD-20251112-1001",
  "totalPrice": 10.75,
  "status": "PENDING",
  "items": [
    {
      "productUuid": "0d7d5407-dec6-46de-9e38-100723715cc1",
      "quantity": 2,
      "price": 1.50
    }
  ]
}
```

**Response:**
```json
{
  "uuid": "bf8899ec-ef1f-45bc-9468-c743cd515ac8",
  "orderNumber": "ORD-20251112-1001",
  "totalPrice": 10.75,
  "status": "PENDING",
  "items": [
    {
      "uuid": "b310665b-00ce-4be1-9569-1c0b7c7d5c13",
      "productName": "Apple",
      "quantity": 2,
      "price": 1.50
    }
  ],
  "audit": {
    "createdBy": "Kong Sisovandara",
    "updatedBy": "Kong Sisovandara",
    "createdAt": "2025-12-23T09:55:26.187510231Z",
    "updatedAt": "2025-12-23T09:55:26.187510231Z"
  }
}
```

**Implementation:**
- Called from `PaymentScreen.kt` after successful payment
- The backend automatically sets `createdBy` to the authenticated user's name from the JWT token
- Order is created with items from cart
- After creation, cart is cleared and user is shown success screen

## Key Files

### Data Models
- `Auth.kt`: LoginRequest, RegisterRequest, AuthResponse, UserResponse
- `Order.kt`: OrderRequest, OrderResponse, OrderItemRequest, OrderItemResponse, Status enum
- `User.kt`: User data classes

### ViewModels
- `AuthViewModel.kt`: Handles login, registration, logout
- `AuthViewModelFactory.kt`: Factory to inject SessionManager
- `OrderViewModel.kt`: Handles order creation and fetching

### Screens
- `LoginScreen.kt`: Login UI with email/password
- `SignUpScreen.kt`: Registration UI with name, phone, email, password
- `OrdersScreen.kt`: Displays user's orders filtered by name
- `PaymentScreen.kt`: Handles payment and order creation

### Utilities
- `SessionManager.kt`: Manages authentication tokens and user data
- `Constants.kt`: API configuration (BASE_URL, SharedPreferences keys)

### API Layer
- `NectarApiService.kt`: Retrofit API interface
- `RetrofitClient.kt`: Retrofit configuration
- `NectarRepository.kt`: Repository pattern for API calls

## Navigation Flow

1. **First Launch:**
   - Splash → Welcome → SignIn → Login

2. **Registration:**
   - SignUp → (success) → Login → (success) → Home

3. **Login:**
   - Login → (success) → Home

4. **Viewing Orders:**
   - Account Tab → My Orders → OrdersScreen
   - Shows only orders where `audit.createdBy` matches logged-in user's name

5. **Creating Order:**
   - Cart → Payment → (success) → OrderAccepted
   - Order is created with authenticated user's token
   - Backend sets `createdBy` automatically

## Configuration

### Base URL
Change in `Constants.kt`:
- Android Emulator: `http://10.0.2.2:8080/`
- Physical Device: `http://YOUR_IP:8080/`
- Production: Your actual server URL

### Test Credentials
Default login credentials (hardcoded in LoginScreen):
- Email: `admin@nectar.com`
- Password: `darasmos123`

## Security Notes

1. **Token Management:**
   - Access tokens stored in SharedPreferences
   - Tokens automatically added to API requests via RetrofitClient
   - Refresh token mechanism available for token renewal

2. **User Identification:**
   - User's name from JWT payload is used for order filtering
   - UUID used for user identification in API calls
   - Email used for login authentication

3. **Authorization:**
   - All order endpoints require Bearer token
   - Backend validates JWT and extracts user info
   - Orders automatically tagged with authenticated user

## Testing

1. **Registration:**
   - Fill in all required fields (name, phone, email, password)
   - Verify success message and redirect to login

2. **Login:**
   - Use test credentials or registered account
   - Verify token storage and redirect to home
   - Check SessionManager has saved user info

3. **Orders:**
   - Create order through cart/payment flow
   - Navigate to Account → My Orders
   - Verify only your orders are displayed
   - Test with multiple users to ensure proper filtering

## Troubleshooting

### Login Issues
- Check backend is running on port 8080
- Verify BASE_URL in Constants.kt matches your setup
- Check LogCat for API error responses
- Verify credentials match backend user

### Order Filtering Issues
- Ensure SessionManager has saved username after login
- Check `audit.createdBy` field in API response
- Verify username matches exactly (case-sensitive)

### Token Expiration
- Access tokens expire after 1 hour
- Implement refresh token logic if needed
- User may need to re-login if both tokens expire

