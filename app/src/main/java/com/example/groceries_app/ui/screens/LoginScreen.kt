package com.example.groceries_app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.groceries_app.R
import com.example.groceries_app.ui.theme.GSshopTheme
import com.example.groceries_app.ui.theme.NectarGreen

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onLoginClick: (email: String, password: String) -> Unit = { _, _ -> },
    onBackClick: () -> Unit = {},
    onForgotPasswordClick: () -> Unit = {},
    onSignupClick: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFFFF5F0),
                        Color(0xFFFFF0F5),
                        Color(0xFFF5F0FF),
                        Color(0xFFE8E8E8)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(48.dp))

            // Carrot Icon
            Image(
                painter = painterResource(id = R.drawable.img_4),
                contentDescription = "Carrot Logo",
                modifier = Modifier
                    .size(80.dp)
                    .padding(16.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Title
            Text(
                text = "Loging",
                fontSize = 26.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Subtitle
            Text(
                text = "Enter your emails and password",
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Gray,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Email Label
            Text(
                text = "Email",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Gray,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            // Email TextField
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(
                        text = "imshuvo97@gmail.com",
                        color = Color.Gray
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
                ),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    disabledContainerColor = Color.White,
                    focusedIndicatorColor = Color(0xFFE2E2E2),
                    unfocusedIndicatorColor = Color(0xFFE2E2E2)
                ),
                shape = RoundedCornerShape(8.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Password Label
            Text(
                text = "Password",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Gray,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            // Password TextField
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(
                        text = "••••••••",
                        color = Color.Gray
                    )
                },
                visualTransformation = if (passwordVisible)
                    VisualTransformation.None
                else
                    PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                ),
                trailingIcon = {
                    TextButton(onClick = { passwordVisible = !passwordVisible }) {
                        Text(
                            text = if (passwordVisible) "👁" else "🙈",
                            fontSize = 20.sp
                        )
                    }
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    disabledContainerColor = Color.White,
                    focusedIndicatorColor = Color(0xFFE2E2E2),
                    unfocusedIndicatorColor = Color(0xFFE2E2E2)
                ),
                shape = RoundedCornerShape(8.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Forgot Password
            Text(
                text = "Forgot Password?",
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Black,
                modifier = Modifier
                    .align(Alignment.End)
                    .clickable { onForgotPasswordClick() }
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Log In Button
            Button(
                onClick = { onLoginClick(email, password) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(67.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = NectarGreen
                ),
                shape = RoundedCornerShape(19.dp)
            ) {
                Text(
                    text = "Log In",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Sign Up Text
            val annotatedText = buildAnnotatedString {
                withStyle(style = SpanStyle(color = Color.Black, fontSize = 14.sp)) {
                    append("Don't have an account? ")
                }
                withStyle(style = SpanStyle(color = NectarGreen, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)) {
                    append("Signup")
                }
            }

            Text(
                text = annotatedText,
                modifier = Modifier.clickable { onSignupClick() },
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginScreenPreview() {
    GSshopTheme {
        LoginScreen()
    }
}

