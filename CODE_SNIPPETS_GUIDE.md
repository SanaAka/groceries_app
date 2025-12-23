# Key Code Snippets - Quick Reference

## 1️⃣ Using the Payment Flow in Your Code

### In NavGraph (Already Implemented)
```kotlin
// Cart Screen - Navigate to payment with amount
CartScreen(
    cartItems = cartItems,
    onNavigateToPayment = { amount ->
        navController.navigate(Screen.Payment.createRoute(amount))
    }
)

// Payment Screen - Handle success/back
PaymentScreen(
    amount = amount,
    onPaymentSuccess = {
        navController.navigate(Screen.OrderAccepted.route) {
            popUpTo("cart") { inclusive = true }
        }
    },
    onBack = { navController.navigateUp() }
)
```

### Using PaymentViewModel Manually
```kotlin
val paymentViewModel: PaymentViewModel = viewModel()
val paymentState by paymentViewModel.paymentState.collectAsState()
val timeRemaining by paymentViewModel.timeRemaining.collectAsState()

// Generate QR code
LaunchedEffect(amount) {
    paymentViewModel.generateQrCode(amount)
}

// Handle states
when (val state = paymentState) {
    is PaymentState.Loading -> { /* Show loading */ }
    is PaymentState.QrGenerated -> { 
        // Display QR: state.qrImage (Bitmap)
        // Display timer: timeRemaining (seconds)
    }
    is PaymentState.PaymentSuccess -> { 
        // Access: state.transactionData
    }
    is PaymentState.PaymentFailed -> { 
        // Show: state.message
    }
    is PaymentState.Error -> { 
        // Show: state.message
    }
}
```

## 2️⃣ Bakong API Models

### Request Models
```kotlin
// Step 1: Generate QR
val request = BakongQrRequest(amount = "10.50")

// Step 2: Get QR Image
val imageRequest = BakongQrImageRequest(
    qr = "0002010102...",
    md5 = "abc123..."
)

// Step 3: Check Transaction
val checkRequest = BakongTransactionCheckRequest(md5 = "abc123...")
```

### Response Models
```kotlin
// QR Generation Response
data class BakongQrResponse(
    val data: BakongQrData,        // qr string + md5
    val khqrstatus: KhqrStatus     // code, errorCode, message
)

// Transaction Check Response
data class BakongTransactionResponse(
    val responseCode: Int,         // 0 = success, 1 = not found
    val responseMessage: String,
    val errorCode: Int?,
    val data: BakongTransactionData?  // null if not paid yet
)

// Transaction Data (when paid)
data class BakongTransactionData(
    val hash: String,
    val fromAccountId: String,
    val toAccountId: String,
    val currency: String,
    val amount: Double,
    val externalRef: String?,
    // ... more fields
)
```

## 3️⃣ API Service Usage

### In Your ViewModel or Repository
```kotlin
import com.example.groceries_app.data.api.RetrofitClient

class MyViewModel : ViewModel() {
    private val api = RetrofitClient.nectarApi
    
    suspend fun processPayment(amount: Double) {
        try {
            // Step 1: Generate QR
            val qrRequest = BakongQrRequest(amount = amount.toString())
            val qrResponse = api.generateBakongQr(qrRequest)
            
            if (qrResponse.isSuccessful) {
                val qrData = qrResponse.body()?.data
                val md5 = qrData?.md5
                
                // Step 2: Get QR Image
                val imageRequest = BakongQrImageRequest(
                    qr = qrData?.qr ?: "",
                    md5 = md5 ?: ""
                )
                val imageResponse = api.getBakongQrImage(imageRequest)
                val imageBytes = imageResponse.body()?.bytes()
                
                // Step 3: Poll for transaction
                while (true) {
                    val checkRequest = BakongTransactionCheckRequest(md5 = md5 ?: "")
                    val txResponse = api.checkBakongTransaction(checkRequest)
                    
                    if (txResponse.body()?.responseCode == 0) {
                        // Payment successful!
                        break
                    }
                    
                    delay(3000) // Wait 3 seconds
                }
            }
        } catch (e: Exception) {
            // Handle error
        }
    }
}
```

## 4️⃣ Customizing the Payment UI

### Changing Colors
```kotlin
// In PaymentScreen.kt, find these colors:

// Success green
color = NectarGreen  // Change to your color

// Error red  
color = Color.Red    // Change to your error color

// Background
containerColor = Color.White  // Change to your background

// Timer warning
color = if (timeRemaining < 60) Color.Red else Color(0xFF181725)
```

### Changing Timeout Duration
```kotlin
// In PaymentViewModel.kt

// Change from 180 seconds (3 min) to your preference
private val _timeRemaining = MutableStateFlow(300)  // 5 minutes
```

### Changing Polling Interval
```kotlin
// In PaymentViewModel.kt, in startTransactionPolling()

// Change from 3 seconds to your preference
delay(5000)  // Poll every 5 seconds
```

## 5️⃣ Extending the Payment Flow

### Add Order Creation After Payment
```kotlin
// In PaymentScreen.kt, in LaunchedEffect(paymentState)

LaunchedEffect(paymentState) {
    if (paymentState is PaymentState.PaymentSuccess) {
        val txData = (paymentState as PaymentState.PaymentSuccess).transactionData
        
        // Create order
        val orderRequest = OrderRequest(
            orderNumber = "ORD-${System.currentTimeMillis()}",
            totalPrice = BigDecimal.valueOf(txData.amount),
            status = Status.PAID,
            items = cartItems.map { ... }
        )
        
        orderViewModel.createOrder(orderRequest, token)
        
        delay(2000)
        onPaymentSuccess()
    }
}
```

### Add Email Receipt
```kotlin
// In PaymentViewModel.kt, after payment success

if (result.responseCode == 0 && result.data != null) {
    _paymentState.value = PaymentState.PaymentSuccess(result.data)
    
    // Send receipt email
    sendReceiptEmail(result.data)
    
    stopPolling()
    break
}
```

### Clear Cart After Payment
```kotlin
// In NavGraph.kt, Payment screen

PaymentScreen(
    amount = amount,
    onPaymentSuccess = {
        // Clear cart before navigating
        cartViewModel.clearCart()
        
        navController.navigate(Screen.OrderAccepted.route) {
            popUpTo("cart") { inclusive = true }
        }
    }
)
```

## 6️⃣ Error Handling Examples

### Custom Error Messages
```kotlin
// In PaymentViewModel.kt

catch (e: Exception) {
    val errorMessage = when (e) {
        is java.net.UnknownHostException -> 
            "No internet connection. Please check your network."
        is java.net.SocketTimeoutException -> 
            "Request timeout. Please try again."
        else -> 
            "Error: ${e.message ?: "Unknown error"}"
    }
    _paymentState.value = PaymentState.Error(errorMessage)
}
```

### Retry Logic with Exponential Backoff
```kotlin
// In PaymentViewModel.kt

private suspend fun pollWithBackoff(md5: String) {
    var retryDelay = 3000L
    var attempt = 0
    
    while (attempt < 60) {  // 60 attempts max
        try {
            val response = api.checkBakongTransaction(
                BakongTransactionCheckRequest(md5)
            )
            
            if (response.body()?.responseCode == 0) {
                // Success!
                break
            }
            
            attempt++
            delay(retryDelay)
            
            // Exponential backoff (max 10 seconds)
            retryDelay = minOf(retryDelay * 1.5, 10000).toLong()
            
        } catch (e: Exception) {
            // Log but continue
            delay(retryDelay)
        }
    }
}
```

## 7️⃣ Configuration for Different Environments

### Development
```kotlin
// RetrofitClient.kt
private const val BASE_URL = "http://10.0.2.2:8080/"  // Emulator
```

### Staging
```kotlin
// RetrofitClient.kt
private const val BASE_URL = "https://staging-api.yourapp.com/"
```

### Production
```kotlin
// RetrofitClient.kt
private const val BASE_URL = "https://api.yourapp.com/"
```

### Dynamic Configuration
```kotlin
object ApiConfig {
    val BASE_URL: String = when (BuildConfig.BUILD_TYPE) {
        "debug" -> "http://10.0.2.2:8080/"
        "staging" -> "https://staging-api.yourapp.com/"
        else -> "https://api.yourapp.com/"
    }
}
```

## 8️⃣ Testing Helpers

### Mock Payment Success
```kotlin
// For UI testing without backend
class MockPaymentViewModel : PaymentViewModel() {
    override fun generateQrCode(amount: Double) {
        _paymentState.value = PaymentState.Loading
        
        viewModelScope.launch {
            delay(2000)
            
            // Mock success
            val mockData = BakongTransactionData(
                hash = "mock_hash",
                fromAccountId = "test@bank",
                toAccountId = "merchant@bank",
                currency = "USD",
                amount = amount,
                // ... other fields
            )
            
            _paymentState.value = PaymentState.PaymentSuccess(mockData)
        }
    }
}
```

### Manual QR Test
```kotlin
// Test with hardcoded QR string
val testQr = "0002010102121511KH12345678930410017kry_sobothty@aclb..."
val testMd5 = "abc123def456"

PaymentScreen(
    amount = 10.0,
    // Override with test data
    paymentViewModel = PaymentViewModel().apply {
        _paymentState.value = PaymentState.QrGenerated(
            BakongQrData(testQr, testMd5),
            null  // Or provide test bitmap
        )
    }
)
```

---

## 🔧 Quick Customization Checklist

- [ ] Update `BASE_URL` in RetrofitClient.kt
- [ ] Adjust timeout duration (default: 3 min)
- [ ] Adjust polling interval (default: 3 sec)
- [ ] Customize colors to match theme
- [ ] Add order creation logic
- [ ] Add email/SMS receipt
- [ ] Configure error messages
- [ ] Add analytics tracking
- [ ] Add logging for debugging

---

**Need Help?**  
Refer to `BAKONG_PAYMENT_INTEGRATION.md` for detailed documentation  
Refer to `PAYMENT_TESTING_GUIDE.md` for testing instructions

