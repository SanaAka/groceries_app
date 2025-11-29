package com.example.groceries_app.examples

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.groceries_app.data.network.Resource
import com.example.groceries_app.viewmodel.AuthViewModel
import com.example.groceries_app.viewmodel.ProductViewModel

/**
 * Example screen showing how to fetch and display products using the API
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExampleProductListScreen(
    viewModel: ProductViewModel = viewModel()
) {
    val productsState by viewModel.productsState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getProducts(page = 1, limit = 20)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Products") }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val state = productsState) {
                is Resource.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is Resource.Success -> {
                    val products = state.data?.products ?: emptyList()

                    if (products.isEmpty()) {
                        Text(
                            text = "No products found",
                            modifier = Modifier.align(Alignment.Center)
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(products) { product ->
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    elevation = CardDefaults.cardElevation(2.dp)
                                ) {
                                    Column(
                                        modifier = Modifier.padding(16.dp)
                                    ) {
                                        Text(
                                            text = product.productName,
                                            style = MaterialTheme.typography.titleMedium
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = product.description ?: "No description available",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = "$${product.price}",
                                            style = MaterialTheme.typography.titleLarge,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                is Resource.Error -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = state.message ?: "Unknown error",
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { viewModel.getProducts() }) {
                            Text("Retry")
                        }
                    }
                }
            }
        }
    }
}

/**
 * Example login screen showing how to use AuthViewModel
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExampleLoginScreen(
    viewModel: AuthViewModel = viewModel(),
    onLoginSuccess: (String) -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val loginState by viewModel.loginState.collectAsState()

    LaunchedEffect(loginState) {
        if (loginState is Resource.Success) {
            val token = (loginState as Resource.Success).data?.token
            token?.let { onLoginSuccess(it) }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Login") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (email.isNotBlank() && password.isNotBlank()) {
                        viewModel.login(email, password)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = loginState !is Resource.Loading
            ) {
                if (loginState is Resource.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Login")
                }
            }

            if (loginState is Resource.Error) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = (loginState as Resource.Error).message ?: "Login failed",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

