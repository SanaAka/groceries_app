package com.example.groceries_app.examples

/**
 * API USAGE EXAMPLES FOR GSSHOP
 *
 * This file contains examples of how to use the Retrofit API in your app.
 *
 * SETUP:
 * 1. Update the BASE_URL in RetrofitClient.kt with your actual API URL
 * 2. Ensure you have internet permission in AndroidManifest.xml:
 *    <uses-permission android:name="android.permission.INTERNET" />
 *
 * USING WITH VIEWMODEL IN COMPOSABLE:
 */

/*
@Composable
fun ProductListScreen(viewModel: ProductViewModel = viewModel()) {
    val productsState by viewModel.productsState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getProducts()
    }

    when (productsState) {
        is Resource.Loading -> {
            CircularProgressIndicator()
        }
        is Resource.Success -> {
            val products = productsState.data?.products ?: emptyList()
            LazyColumn {
                items(products) { product ->
                    Text(text = product.name)
                }
            }
        }
        is Resource.Error -> {
            Text(text = productsState.message ?: "Error")
        }
    }
}
*/

/**
 * DIRECT REPOSITORY USAGE (NOT RECOMMENDED - USE VIEWMODEL INSTEAD):
 */

/*
fun fetchProductsDirectly() {
    val repository = ProductRepository()

    lifecycleScope.launch {
        repository.getProducts().collect { result ->
            when (result) {
                is Resource.Loading -> {
                    // Show loading
                }
                is Resource.Success -> {
                    // Handle success
                    val products = result.data?.products
                }
                is Resource.Error -> {
                    // Handle error
                    val error = result.message
                }
            }
        }
    }
}
*/

/**
 * AUTH EXAMPLE:
 */

/*
@Composable
fun LoginScreen() {
    val repository = AuthRepository()
    val scope = rememberCoroutineScope()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column {
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") }
        )

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") }
        )

        Button(
            onClick = {
                scope.launch {
                    repository.login(email, password).collect { result ->
                        when (result) {
                            is Resource.Loading -> {
                                isLoading = true
                            }
                            is Resource.Success -> {
                                isLoading = false
                                val authResponse = result.data
                                // Save token and navigate
                            }
                            is Resource.Error -> {
                                isLoading = false
                                errorMessage = result.message
                            }
                        }
                    }
                }
            }
        ) {
            Text("Login")
        }

        if (isLoading) CircularProgressIndicator()
        errorMessage?.let { Text(it, color = Color.Red) }
    }
}
*/

/**
 * TESTING WITH DUMMYJSON API:
 *
 * You can test the API implementation using DummyJSON API:
 * 1. In RetrofitClient.kt, uncomment the DummyJSON BASE_URL
 * 2. Use these endpoints:
 *    - GET https://dummyjson.com/products
 *    - GET https://dummyjson.com/products/1
 *    - GET https://dummyjson.com/products/search?q=phone
 *    - POST https://dummyjson.com/auth/login
 *
 * You'll need to adjust the ApiService endpoints to match DummyJSON structure.
 */

/**
 * ERROR HANDLING:
 *
 * All repositories return Flow<Resource<T>> which provides:
 * - Resource.Loading: When API call is in progress
 * - Resource.Success: When API call succeeds (contains data)
 * - Resource.Error: When API call fails (contains error message)
 */

/**
 * CUSTOMIZING RETROFIT:
 *
 * To add custom headers or interceptors:
 * 1. Open RetrofitClient.kt
 * 2. Add interceptors to OkHttpClient.Builder:
 *
 * val authInterceptor = Interceptor { chain ->
 *     val request = chain.request().newBuilder()
 *         .addHeader("Authorization", "Bearer YOUR_TOKEN")
 *         .build()
 *     chain.proceed(request)
 * }
 *
 * private val okHttpClient = OkHttpClient.Builder()
 *     .addInterceptor(authInterceptor)
 *     .addInterceptor(loggingInterceptor)
 *     .build()
 */

