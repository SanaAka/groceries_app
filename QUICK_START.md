# Nectar API - Quick Start Guide

## ✅ Setup Complete!

Your nectar-api backend is now running and integrated with your Android app.

## Running Services

### Backend API
- **URL**: http://localhost:8080
- **Status**: ✅ Running
- **Command**: `cd nectar-api && .\gradlew bootRun`

### Database (PostgreSQL)
- **Host**: localhost:5432
- **Database**: nectardb
- **Username**: postgres
- **Password**: postgres
- **Status**: ✅ Running in Docker

### MinIO (File Storage)
- **API**: http://localhost:9000
- **Console**: http://localhost:9001
- **Username**: dara
- **Password**: dara1234
- **Status**: ✅ Running in Docker

## Available Endpoints

### Authentication
```
POST /api/v1/auth/register - Register new user
POST /api/v1/auth/login    - Login user
POST /api/v1/auth/refresh  - Refresh access token
```

### Users
```
GET /api/v1/users/me - Get current user info (requires auth)
```

### Products
```
GET    /api/v1/products      - Get all products
GET    /api/v1/products/{id} - Get product by ID
POST   /api/v1/products      - Create product (requires auth)
PUT    /api/v1/products/{id} - Update product (requires auth)
DELETE /api/v1/products/{id} - Delete product (requires auth)
```

### Orders
```
GET  /api/v1/orders - Get user's orders (requires auth)
POST /api/v1/orders - Create order (requires auth)
```

## Quick Test

### 1. Register a User
```powershell
$body = @{
    username = "testuser"
    email = "test@example.com"
    password = "password123"
    confirmedPassword = "password123"
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8080/api/v1/auth/register" `
    -Method POST -Body $body -ContentType "application/json"
```

### 2. Login
```powershell
$body = @{
    username = "testuser"
    password = "password123"
} | ConvertTo-Json

$response = Invoke-RestMethod -Uri "http://localhost:8080/api/v1/auth/login" `
    -Method POST -Body $body -ContentType "application/json"

$token = $response.accessToken
```

### 3. Get Products
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/v1/products" -Method GET
```

## Android App Configuration

Your app is configured to connect to the backend:
- **Base URL**: `http://10.0.2.2:8080/` (for Android Emulator)
- **Location**: `app/src/main/java/com/example/groceries_app/data/api/RetrofitClient.kt`

### For Physical Device
Update the base URL to your computer's IP:
```kotlin
private const val BASE_URL = "http://YOUR_IP:8080/"
```

Find your IP:
```powershell
ipconfig
# Look for IPv4 Address
```

## Managing Services

### Start Docker Services
```powershell
cd nectar-api
docker-compose up -d
```

### Stop Docker Services
```powershell
cd nectar-api
docker-compose down
```

### View Docker Logs
```powershell
docker-compose logs -f
```

### Stop Backend API
Press `Ctrl+C` in the terminal running `gradlew bootRun`

## Troubleshooting

### Backend won't start
1. Check if PostgreSQL is running: `docker ps`
2. Check environment variables in `.env` file
3. Verify database name matches: `nectardb`

### Android app can't connect
1. Ensure backend is running on port 8080
2. For emulator: Use `10.0.2.2` instead of `localhost`
3. For device: Use your computer's IP address
4. Check firewall settings

### Database connection failed
```powershell
# Restart Docker services
cd nectar-api
docker-compose restart
```

## Initial Data

The backend automatically creates 20 sample products on first run!

## Next Steps

1. ✅ Backend running
2. ✅ Android app integrated
3. **Test the app**: Run your Android app and test the API integration
4. **Build features**: Use the `NectarRepository` in your ViewModels
5. **Check examples**: See `examples/NectarExampleViewModel.kt`

## File Reference

- **API Service**: `app/src/main/java/com/example/groceries_app/data/api/NectarApiService.kt`
- **Repository**: `app/src/main/java/com/example/groceries_app/data/repository/NectarRepository.kt`
- **Models**: `app/src/main/java/com/example/groceries_app/data/model/`
- **Session Manager**: `app/src/main/java/com/example/groceries_app/utils/SessionManager.kt`

---

**Your groceries app is ready to use! 🚀**
