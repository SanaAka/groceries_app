# Quick Start - Bakong Payment Testing

## Prerequisites

1. **Backend Running**: Ensure your Spring Boot backend is running on `http://localhost:8080`
2. **Bakong API Configured**: Your backend should be properly configured with Bakong credentials

## Test the Payment Flow

### Step 1: Add Items to Cart
1. Launch the app
2. Navigate through Home or Explore screens
3. Add products to cart using the "+" button
4. Go to Cart tab

### Step 2: Initiate Payment
1. In Cart screen, click "Go to Checkout" button
2. Review checkout details in the bottom sheet
3. Click "Place Order" button
4. You'll be navigated to the Payment screen

### Step 3: Payment Screen
You should see:
- ✅ Amount to pay (top card)
- ✅ Countdown timer (3:00 minutes)
- ✅ QR Code image (center)
- ✅ Instructions text
- ✅ Waiting indicator

### Step 4: Complete Payment
**Option A - Real Payment:**
1. Scan the QR code with your banking app
2. Complete the payment
3. Wait for automatic detection (polls every 3 seconds)
4. Success screen appears automatically
5. Navigate to Order Accepted screen

**Option B - Test Timeout:**
1. Don't scan the QR code
2. Wait for 3 minutes
3. Timer expires
4. Payment Failed screen appears
5. Click "Try Again" to regenerate QR

### Expected API Calls

When payment screen loads:
```
1. POST /api/v1/bakong/generate-qr
   Body: { "amount": "13.97" }

2. POST /api/v1/bakong/get-qr-image
   Body: { "qr": "...", "md5": "..." }

3. POST /api/v1/bakong/check-transaction (every 3 seconds)
   Body: { "md5": "..." }
```

## Troubleshooting

### QR Code Not Displaying
- Check backend logs for errors
- Verify Bakong API credentials
- Check network connectivity (emulator can access localhost via 10.0.2.2)

### Payment Not Detected
- Ensure backend is returning responseCode: 0 when paid
- Check polling is active (progress bar should be animating)
- Verify md5 hash is consistent

### Network Errors
- Update BASE_URL in `RetrofitClient.kt`:
  - Emulator: `http://10.0.2.2:8080/`
  - Physical Device: `http://YOUR_IP_ADDRESS:8080/`
  - Production: Your actual server URL

### Timer Not Counting Down
- Check that PaymentViewModel is properly initialized
- Verify navigation is passing the amount correctly

## Backend Response Examples

### Successful QR Generation
```json
{
    "data": {
        "qr": "0002010102121511KH12345678930410017kry_sobothty@aclb...",
        "md5": "6c54b659ab23ed6ce6159ce000bb9b2f"
    },
    "khqrstatus": {
        "code": 0,
        "errorCode": null,
        "message": null
    }
}
```

### Transaction Not Found (Still Pending)
```json
{
    "responseCode": 1,
    "responseMessage": "Transaction could not be found. Please check and try again.",
    "errorCode": 1,
    "data": null
}
```

### Payment Successful
```json
{
    "responseCode": 0,
    "responseMessage": "Success",
    "errorCode": null,
    "data": {
        "hash": "ee822082cbb82a908261b862fd7df025a7b4b3752fafa8c9ed32ab96dd985821",
        "fromAccountId": "abaakhppxxx@abaa",
        "toAccountId": "kry_sobothty@aclb",
        "currency": "USD",
        "amount": 0.1,
        "externalRef": "100FT36317296509"
    }
}
```

## UI Verification Checklist

- [ ] Amount displays correctly with $ symbol
- [ ] Timer shows "3:00" initially
- [ ] QR code image is visible and clear
- [ ] Progress bar animates during polling
- [ ] Timer turns red when < 1 minute
- [ ] Success screen shows transaction details
- [ ] Colors match app theme (NectarGreen #53B175)
- [ ] Back button works and cancels payment
- [ ] Retry button regenerates new QR code

## Demo Flow Video Script

1. "Starting from cart with items totaling $13.97"
2. "Clicking 'Go to Checkout' opens the checkout sheet"
3. "Click 'Place Order' to proceed to payment"
4. "Payment screen loads with QR code and 3-minute timer"
5. "Scanning QR code with banking app..."
6. "Payment detected automatically within 3 seconds"
7. "Success screen appears with transaction details"
8. "Navigated to Order Accepted screen"

## Notes

- The payment screen will automatically stop polling when:
  - Payment is successful
  - Payment fails
  - User navigates back
  - 3 minutes timeout expires

- Cart is NOT automatically cleared until order is placed
- You can go back and retry payment multiple times
- Each retry generates a fresh QR code with new md5

---

**Ready to Test!** 🚀

Run your Spring Boot backend and test the complete payment flow.

