# Order Management Integration

## Overview
Integrated complete order management system with the payment flow. Orders are automatically created after successful Bakong payment and displayed in the Account section.

## Features Implemented

### ✅ 1. **Order Creation After Payment**
- Automatically creates order when payment is successful
- Order status set to `PAID` after Bakong payment confirmation
- Includes all cart items with product details
- Generates unique order number: `ORD-{timestamp}`

### ✅ 2. **Orders Screen**
- Displays all orders for the logged-in user
- Filters orders by `createdBy` in audit (matches username)
- Beautiful card-based UI matching app design
- Shows order details: number, date, items, total, status

### ✅ 3. **Order Status Badges**
- **PENDING** - Orange badge
- **PAID** - Green badge (NectarGreen)
- **SHIPPED** - Blue badge
- **COMPLETED** - Dark green badge
- **CANCELLED** - Red badge

### ✅ 4. **Navigation Integration**
- Orders accessible from Account screen
- "Orders" menu item navigates to Orders list
- Requires authentication (token-based)

## API Endpoints Used

### 1. Get All Orders
```
GET http://localhost:8080/api/v1/orders
Headers: Authorization: Bearer {token}

Response:
{
    "orders": [
        {
            "uuid": "8ec0d51b-1718-48af-8d4b-4920ffc54ce0",
            "orderNumber": "ORD-20251112-1001",
            "totalPrice": 10.75,
            "status": "PAID",
            "items": [
                {
                    "uuid": "5cf9a57c-915b-4a16-b4ec-fd13f4670336",
                    "productName": "Apple",
                    "quantity": 2,
                    "price": 1.50
                }
            ],
            "audit": {
                "createdBy": "Kong Sisovandara",
                "updatedBy": "Kong Sisovandara",
                "createdAt": "2025-12-23T09:27:10.154292Z",
                "updatedAt": "2025-12-23T09:27:10.154292Z"
            }
        }
    ]
}
```

### 2. Create Order
```
POST http://localhost:8080/api/v1/orders
Headers: Authorization: Bearer {token}

Body:
{
    "orderNumber": "ORD-1735034400000",
    "totalPrice": 10.75,
    "status": "PAID",
    "items": [
        {
            "productUuid": "8098f700-38f9-49b9-80c6-5afb33cbb39f",
            "quantity": 2,
            "price": 1.50
        }
    ]
}

Response:
{
    "uuid": "5ecea30c-db2f-423a-8cf6-8cc6bed4cb61",
    "orderNumber": "ORD-1735034400000",
    "totalPrice": 10.75,
    "status": "PAID",
    "items": [
        {
            "uuid": "5cf9a57c-915b-4a16-b4ec-fd13f4670336",
            "productName": "Apple",
            "quantity": 2,
            "price": 1.50
        }
    ],
    "audit": {
        "createdBy": "Kong Sisovandara",
        "updatedBy": "Kong Sisovandara",
        "createdAt": "2025-12-23T09:28:05.861317066Z",
        "updatedAt": "2025-12-23T09:28:05.861317066Z"
    }
}
```

## Complete Flow

### Payment to Order Flow
```
1. User adds items to cart
   ↓
2. Clicks "Go to Checkout"
   ↓
3. Clicks "Place Order"
   ↓
4. Navigates to Payment Screen
   ↓
5. QR code generated for amount
   ↓
6. User scans QR and completes payment
   ↓
7. Payment detected (Bakong API)
   ↓
8. ✅ Order created automatically (POST /api/v1/orders)
   - Status: PAID
   - Items: All cart items
   - Total: Payment amount
   ↓
9. Cart cleared
   ↓
10. Navigate to Order Accepted screen
```

### View Orders Flow
```
1. User goes to Account tab
   ↓
2. Clicks "Orders" menu item
   ↓
3. Loads orders (GET /api/v1/orders with token)
   ↓
4. Filters by createdBy = current username
   ↓
5. Displays user's orders only
```

## Files Created/Modified

### New Files:

1. **`ui/screens/OrdersScreen.kt`**
   - Complete orders list UI
   - OrderCard component
   - StatusBadge component
   - Date formatting utility
   - Empty state, error state, loading state
   - User-specific order filtering

### Modified Files:

1. **`navigation/Screen.kt`**
   - Added `Orders` screen route

2. **`navigation/NavGraph.kt`**
   - Added Orders screen composable
   - Updated Payment screen to accept cart items
   - Updated Account screen to navigate to Orders
   - Cart cleared after successful payment

3. **`ui/screens/PaymentScreen.kt`**
   - Accepts cart items parameter
   - Creates order after successful payment
   - Order status set to PAID
   - Integrated with OrderViewModel

## UI Components

### Orders Screen Layout

```
┌─────────────────────────────────┐
│  ← My Orders                    │ TopAppBar
├─────────────────────────────────┤
│                                 │
│  ┌─────────────────────────┐   │
│  │ ORD-1735034400000  [PAID]│   │ Order Card
│  │ Dec 23, 2025 at 09:28 AM│   │
│  ├─────────────────────────┤   │
│  │ 2x Apple        $1.50   │   │ Items
│  │ 1x Banana       $0.50   │   │
│  ├─────────────────────────┤   │
│  │ Total          $10.75   │   │ Total
│  └─────────────────────────┘   │
│                                 │
│  ┌─────────────────────────┐   │
│  │ Another Order...        │   │
│  └─────────────────────────┘   │
│                                 │
└─────────────────────────────────┘
```

### Status Badge Colors

| Status    | Background  | Text Color | Display   |
|-----------|-------------|------------|-----------|
| PENDING   | #FFF3E0     | #FF8F00    | Pending   |
| PAID      | #E8F5E9     | #53B175    | Paid      |
| SHIPPED   | #E3F2FD     | #1976D2    | Shipped   |
| COMPLETED | #E8F5E9     | #2E7D32    | Completed |
| CANCELLED | #FFEBEE     | #C62828    | Cancelled |

## Order Filtering Logic

Orders are filtered by comparing:
```kotlin
order.audit?.createdBy == currentUserName
```

Where:
- `currentUserName` = Username from SessionManager
- `createdBy` = Username from order's audit metadata

This ensures each user only sees their own orders.

## Security

✅ **Token-Based Authentication**
- All order API calls require Bearer token
- Token obtained from SessionManager
- Automatically included in API requests

✅ **User Isolation**
- Frontend filters orders by createdBy
- Backend should also validate user ownership
- Each user sees only their orders

## Order Data Model

```kotlin
data class OrderResponse(
    val uuid: String,
    val orderNumber: String,
    val totalPrice: BigDecimal,
    val status: Status,
    val items: List<OrderItemResponse>,
    val audit: AuditResponse?
)

data class OrderItemResponse(
    val uuid: String,
    val productName: String,
    val quantity: Int,
    val price: BigDecimal
)

enum class Status {
    PENDING, PAID, SHIPPED, COMPLETED, CANCELLED
}
```

## Testing Guide

### Test Order Creation After Payment

1. **Login** with test account (`admin@nectar.com`)
2. **Add items to cart**
3. **Go to checkout** and click "Place Order"
4. **Complete payment** via QR code
5. **Wait for success** screen
6. **Check backend** - Order should be created with status PAID
7. **Navigate to Account → Orders**
8. **Verify** new order appears in list

### Test Orders Display

1. **Login** with account that has orders
2. **Go to Account tab**
3. **Click "Orders" menu item**
4. **Should see list** of user's orders
5. **Verify filtering** - only shows orders by that user
6. **Check order details** - number, date, items, total, status

### Test Empty State

1. **Login** with new account (no orders)
2. **Go to Account → Orders**
3. **Should see empty state**:
   - Shopping cart emoji 🛒
   - "No Orders Yet" message
   - Helpful text

### Test Error State

1. **Disable backend**
2. **Go to Account → Orders**
3. **Should see error state**:
   - Error emoji ❌
   - "Failed to load orders" message
   - Error details

## UI/UX Features

### Orders Screen
- ✅ Clean card-based design
- ✅ Color-coded status badges
- ✅ Formatted dates (e.g., "Dec 23, 2025 at 09:28 AM")
- ✅ Item list with quantities and prices
- ✅ Shows up to 3 items, then "+ X more items"
- ✅ Empty state for no orders
- ✅ Error state for failed load
- ✅ NectarGreen accent color

### Order Card
- ✅ Order number prominently displayed
- ✅ Status badge in top-right
- ✅ Creation date
- ✅ Dividers for visual separation
- ✅ Item breakdown
- ✅ Total in green (NectarGreen)
- ✅ White background with shadow

## What Happens After Payment

1. **Payment Success Detected** (via Bakong API polling)
2. **Order Created** automatically:
   ```kotlin
   OrderRequest(
       orderNumber = "ORD-{timestamp}",
       totalPrice = paymentAmount,
       status = Status.PAID,
       items = cartItems.map { ... }
   )
   ```
3. **Cart Cleared** - Items removed from cart
4. **Navigation** - User taken to Order Accepted screen
5. **Order Saved** - Visible in Account → Orders

## Configuration

No additional configuration needed. The system uses:
- Existing RetrofitClient for API calls
- Existing SessionManager for token
- Existing OrderViewModel for order operations
- Existing navigation system

## Future Enhancements

Possible improvements:
- Order tracking (track shipment status)
- Order details screen (tap to see full details)
- Reorder functionality (add order items back to cart)
- Order cancellation (for pending orders)
- Order history filtering (by date, status)
- Export order receipt as PDF
- Push notifications for order updates

---

## Summary

✅ **Order Creation** - Automatic after payment  
✅ **Order Display** - In Account → Orders  
✅ **User Filtering** - By createdBy in audit  
✅ **Status Badges** - Color-coded and clear  
✅ **Token Authentication** - Secure API access  
✅ **UI Consistency** - Matches app design  
✅ **Empty/Error States** - User-friendly  

**Status**: ✅ **COMPLETE & READY TO TEST**

The order management system is fully integrated with the payment flow!

