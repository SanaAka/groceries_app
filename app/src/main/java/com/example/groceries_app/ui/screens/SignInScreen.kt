package com.example.groceries_app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.groceries_app.R
import com.example.groceries_app.ui.theme.GSshopTheme
import com.example.groceries_app.ui.theme.NectarGreen

@Composable
fun SignInScreen(
    modifier: Modifier = Modifier,
    onGoogleSignIn: () -> Unit = {},
    onFacebookSignIn: () -> Unit = {},
    onEmailSignIn: () -> Unit = {},
    onSignUpClick: () -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Top Image Section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_2),
                contentDescription = "Groceries",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        // Content Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(top = 32.dp),
            horizontalAlignment = Alignment.Start
        ) {
            // Title
            Text(
                text = "Get your groceries",
                fontSize = 28.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black,
                lineHeight = 32.sp
            )

            Text(
                text = "with nectar",
                fontSize = 28.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black,
                lineHeight = 32.sp
            )

            Spacer(modifier = Modifier.height(24.dp))



            Spacer(modifier = Modifier.height(24.dp))

            // Google Button
            Button(
                onClick = onGoogleSignIn,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(67.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF5383EC)
                ),
                shape = RoundedCornerShape(19.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "G",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "Continue with Google",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Facebook Button
            Button(
                onClick = onFacebookSignIn,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(67.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4A66AC)
                ),
                shape = RoundedCornerShape(19.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "f",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Serif
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "Continue with Facebook",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Email Login Button
            Button(
                onClick = onEmailSignIn,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(67.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = NectarGreen
                ),
                shape = RoundedCornerShape(19.dp)
            ) {
                Text(
                    text = "Continue with Email",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Sign Up Link Text
            val signUpText = buildAnnotatedString {
                withStyle(style = SpanStyle(color = Color.Black, fontSize = 14.sp)) {
                    append("Don't have an account? ")
                }
                withStyle(style = SpanStyle(color = NectarGreen, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)) {
                    append("Sign Up")
                }
            }

            Text(
                text = signUpText,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSignUpClick() },
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SignInScreenPreview() {
    GSshopTheme {
        SignInScreen()
    }
}

