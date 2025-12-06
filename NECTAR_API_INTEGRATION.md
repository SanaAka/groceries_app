# Nectar API Integration Guide

## Overview
Your Android app is now integrated with the nectar-api Spring Boot backend. The integration includes:

- ✅ API models (DTOs) matching your backend
- ✅ Retrofit API service interface
- ✅ Repository layer for data operations
- ✅ Session management for authentication
- ✅ Token handling and refresh

## File Structure

```
app/src/main/java/com/example/groceries_app/
├── data/
│   ├── api/
│   │   ├── NectarApiService.kt       # Retrofit API interface
│   │   └── RetrofitClient.kt         # Retrofit configuration
│   ├── model/
│   │   ├── Auth.kt                   # Auth models
│   │   ├── Product.kt                # Product models
│   │   └── Order.kt                  # Order models
│   └── repository/
│       └── NectarRepository.kt       # Repository layer
├── utils/
│   ├── Constants.kt                  # App constants
│   └── SessionManager.kt             # Session/token management
└── examples/
    └── NectarExampleViewModel.kt     # Usage examples
```

## Setup Instructions

### 1. Configure API Base URL

**For Android Emulator:**
```kotlin
// In RetrofitClient.kt (already configured)
private const val BASE_URL = "http://10.0.2.2:8080/"
```

**For Physical Device:**
Find your computer's IP address and update:
```kotlin
private const val BASE_URL = "http://YOUR_IP_ADDRESS:8080/"
```

On Windows, run in PowerShell:
```powershell
ipconfig
# Look for IPv4 Address (e.g., 192.168.1.100)
```

### 2. Start Your Backend

Make sure your nectar-api is running:
```bash
cd nectar-api
./gradlew bootRun
```

Your API should be accessible at `http://localhost:8080`

### 3. Configure Environment Variables

Set up your nectar-api environment variables (create `.env` or set in system):
```properties
DB_HOST=localhost
DB_PORT=5432
DB_NAME=nectar_db
DB_USERNAME=postgres
DB_PASSWORD=your_password
MINIO_ENDPOINT=http://localhost:9000
MINIO_ACCESS_KEY=minioadmin
MINIO_SECRET_KEY=minioadmin
MINIO_BUCKET_NAME=nectar
APP_BASE_URL=http://localhost:8080
APP_FILE_PATH=/nectar
```

## Usage Examples

### 1. User Registration
```kotlin
val repository = NectarRepository.getInstance()

viewModelScope.launch {
    val request = RegisterRequest(
        username = "john_doe",
        email = "john@example.com",
        password = "securePassword123",
        confirmedPassword = "securePassword123"
    )
    
    repository.register(request)
        .onSuccess { userResponse ->
            // Registration successful
            println("User created: ${userResponse.username}")
        }
        .onFailure { exception ->
            // Handle error
            println("Error: ${exception.message}")
        }
}
```

### 2. User Login
```kotlin
val sessionManager = SessionManager.getInstance(context)

viewModelScope.launch {
    val request = LoginRequest("john_doe", "securePassword123")
    
    repository.login(request)
        .onSuccess { authResponse ->
            // Save tokens
            sessionManager.saveAuthTokens(
                authResponse.accessToken,
                authResponse.refreshToken
            )
            // Navigate to home screen
        }
        .onFailure { exception ->
            // Show error message
        }
}
```

### 3. Fetch Products
```kotlin
viewModelScope.launch {
    repository.getAllProducts()
        .onSuccess { products ->
            // Display products
            products.forEach { product ->
                println("${product.name}: $${product.price}")
            }
        }
        .onFailure { exception ->
            // Handle error
        }
}
```

### 4. Create Order
```kotlin
viewModelScope.launch {
    val token = sessionManager.getAccessToken() ?: return@launch
    
    val orderItems = listOf(
        OrderItemRequest(productUuid = "product-uuid-1", quantity = 2),
        OrderItemRequest(productUuid = "product-uuid-2", quantity = 1)
    )
    
    val request = OrderRequest(
        deliveryDate = "2024-12-10",
        orderItems = orderItems
    )
    
    repository.createOrder(token, request)
        .onSuccess { order ->
            println("Order created: ${order.uuid}")
        }
        .onFailure { exception ->
            // Handle error
        }
}
```

### 5. Get User's Orders
```kotlin
viewModelScope.launch {
    val token = sessionManager.getAccessToken() ?: return@launch
    
    repository.getAllOrders(token)
        .onSuccess { orders ->
            orders.forEach { order ->
                println("Order ${order.uuid}: $${order.totalAmount}")
            }
        }
        .onFailure { exception ->
            // Handle error
        }
}
```

## Available API Endpoints

### Authentication
- `POST /api/v1/auth/register` - Register new user
- `POST /api/v1/auth/login` - Login user
- `POST /api/v1/auth/refresh` - Refresh access token

### Users
- `GET /api/v1/users/me` - Get current user info

### Products
- `GET /api/v1/products` - Get all products
- `GET /api/v1/products/{uuid}` - Get product by UUID
- `POST /api/v1/products` - Create product (requires auth)
- `PUT /api/v1/products/{uuid}` - Update product (requires auth)
- `DELETE /api/v1/products/{uuid}` - Delete product (requires auth)

### Orders
- `GET /api/v1/orders` - Get user's orders (requires auth)
- `POST /api/v1/orders` - Create order (requires auth)

## Testing

### Test with Android Studio
1. Make sure nectar-api is running
2. Open your app in Android Studio
3. Run the app on emulator or device
4. The app will communicate with your backend

### Test API Manually
Use Postman or curl to test your endpoints:

```bash
# Register a user
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "password123",
    "confirmedPassword": "password123"
  }'

# Login
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password123"
  }'
```

## Troubleshooting

### Connection Refused
- Ensure nectar-api is running on port 8080
- For emulator: Use `10.0.2.2` instead of `localhost`
- For device: Use your computer's IP address
- Check firewall settings

### Authentication Errors
- Verify tokens are being saved correctly
- Check token expiration
- Use refresh token endpoint to get new access token

### Network Security Error
If you get "Cleartext HTTP traffic not permitted", add to `AndroidManifest.xml`:
```xml
<application
    android:usesCleartextTraffic="true"
    ...>
```

## Next Steps

1. Create ViewModels for your screens using the repository
2. Add loading states and error handling in your UI
3. Implement product listing and detail screens
4. Add shopping cart functionality
5. Implement order history screen
6. Add user profile management

## Notes

- All network calls are already wrapped in coroutines
- Repository uses `Result<T>` for better error handling
- SessionManager persists tokens across app restarts
- RetrofitClient automatically adds auth tokens to requests
