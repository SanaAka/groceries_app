# Bakong Payment Integration Guide

## Overview
This document describes the Bakong payment integration implemented in the Groceries App. The integration provides a complete payment flow with QR code generation, real-time transaction monitoring, and automatic timeout handling.

## Features Implemented

### 1. **QR Code Generation**
- Generates Bakong QR code based on cart total amount
- Displays QR code image for scanning
- Shows amount to be paid prominently

### 2. **Real-time Transaction Monitoring**
- Polls backend every 3 seconds to check payment status
- Automatically detects when payment is completed
- Shows countdown timer (3 minutes)

### 3. **Timeout Handling**
- 3-minute payment window
- Automatic timeout with retry option
- Clear error messages

### 4. **Success & Error States**
- Payment success screen with transaction details
- Payment failed screen with retry button
- Error handling for network issues

## Files Created/Modified

### New Files Created:

1. **`data/model/Bakong.kt`**
   - Bakong API request/response models
   - `BakongQrRequest`, `BakongQrResponse`, `BakongTransactionResponse`, etc.

2. **`viewmodel/PaymentViewModel.kt`**
   - Manages payment state machine
   - Handles QR generation, polling, and timeout
   - Exposes `PaymentState` and `timeRemaining` flows

3. **`ui/screens/PaymentScreen.kt`**
   - Complete payment UI with QR code display
   - Timer countdown display
   - Success/failure screens
   - Consistent with app's color scheme (NectarGreen)

### Modified Files:

1. **`data/api/NectarApiService.kt`**
   - Added 3 Bakong endpoints:
     - `POST /api/v1/bakong/generate-qr`
     - `POST /api/v1/bakong/get-qr-image`
     - `POST /api/v1/bakong/check-transaction`

2. **`navigation/Screen.kt`**
   - Added `Payment` screen route with amount parameter

3. **`navigation/NavGraph.kt`**
   - Added Payment screen navigation
   - Updated Cart screen to navigate to payment
   - Success flows to OrderAccepted screen

4. **`ui/screens/CartScreen.kt`**
   - Added `onNavigateToPayment` callback parameter
   - Passes amount to payment screen

5. **`ui/screens/CheckoutBottomSheet.kt`**
   - Added `onNavigateToPayment` callback parameter
   - "Place Order" button now navigates to payment

## Payment Flow

```
Cart Screen
    ↓
[Go to Checkout] Button
    ↓
Checkout Bottom Sheet
    ↓
[Place Order] Button
    ↓
Payment Screen (with QR Code)
    ↓ (3-minute polling)
    ├─→ Payment Success → Order Accepted Screen
    └─→ Payment Failed/Timeout → Retry or Go Back
```

## Backend Endpoints Used

### 1. Generate QR Code String
```
POST http://localhost:8080/api/v1/bakong/generate-qr
Body: { "amount": "0.1" }

Response:
{
    "data": {
        "qr": "0002010102...",
        "md5": "6c54b659ab23ed6ce6159ce000bb9b2f"
    },
    "khqrstatus": { "code": 0, ... }
}
```

### 2. Get QR Code Image
```
POST http://localhost:8080/api/v1/bakong/get-qr-image
Body: { "qr": "0002010102...", "md5": "..." }

Response: PNG Image (Binary)
```

### 3. Check Transaction Status
```
POST http://localhost:8080/api/v1/bakong/check-transaction
Body: { "md5": "..." }

Response (Pending):
{
    "responseCode": 1,
    "responseMessage": "Transaction could not be found...",
    "errorCode": 1,
    "data": null
}

Response (Success):
{
    "responseCode": 0,
    "responseMessage": "Success",
    "errorCode": null,
    "data": {
        "hash": "ee822082...",
        "fromAccountId": "abaakhppxxx@abaa",
        "toAccountId": "kry_sobothty@aclb",
        "currency": "USD",
        "amount": 0.1,
        ...
    }
}
```

## UI/UX Features

### Color Scheme
- Primary color: `NectarGreen` (#53B175) - consistent with app design
- Error states: Red for critical messages
- Success: Green checkmark icon
- Neutral: Gray for secondary text

### Payment Screen Components

1. **Amount Display Card**
   - Light gray background
   - Large green amount text
   - Clean, centered layout

2. **Timer Display**
   - Countdown format: "M:SS"
   - Changes to red when < 60 seconds
   - Bold, visible text

3. **QR Code Card**
   - White card with shadow
   - Square aspect ratio
   - Centered QR image
   - Padding for scanning clarity

4. **Progress Indicator**
   - Linear progress bar
   - Shows active polling
   - NectarGreen color

5. **Success View**
   - Large green checkmark
   - Transaction details card
   - Thank you message

6. **Error/Failed View**
   - Clear error icon (emoji)
   - Descriptive message
   - Retry button (NectarGreen)

## Timeout & Polling Logic

- **Polling Interval**: 3 seconds
- **Total Timeout**: 180 seconds (3 minutes)
- **Countdown Timer**: Updates every second
- **Auto-stop**: Polling stops on success, failure, or timeout

### State Machine

```
Idle
  ↓
Loading (Generating QR)
  ↓
QrGenerated (Polling active)
  ↓
  ├─→ PaymentSuccess (Poll detected payment)
  ├─→ PaymentFailed (Timeout or error)
  └─→ Error (Network/API error)
```

## Testing Checklist

- [ ] QR code generates with correct amount
- [ ] QR image displays properly
- [ ] Timer counts down correctly
- [ ] Payment detection works when QR is scanned
- [ ] Timeout triggers after 3 minutes
- [ ] Retry button regenerates QR
- [ ] Success screen shows transaction details
- [ ] Navigation flows work correctly
- [ ] Back button cancels payment
- [ ] UI matches app design (colors, fonts, spacing)

## Configuration

Update the backend URL in `RetrofitClient.kt` if needed:
```kotlin
private const val BASE_URL = "http://10.0.2.2:8080/"  // Android Emulator
// or
private const val BASE_URL = "http://YOUR_IP:8080/"   // Physical Device
```

## Error Handling

The integration handles:
- Network failures (retry available)
- API errors (clear error messages)
- Timeout scenarios (retry button)
- Invalid QR generation (error state)
- Missing transaction data (graceful degradation)

## Future Enhancements

Possible improvements:
- Order creation after successful payment
- Payment history tracking
- Multiple payment methods
- Promo code application
- Delivery address selection
- Email receipt on success
- Push notifications for payment confirmation

---

**Integration Complete** ✅

All files have been created and integrated. The payment flow is ready to test with your Spring Boot backend running on `localhost:8080`.

