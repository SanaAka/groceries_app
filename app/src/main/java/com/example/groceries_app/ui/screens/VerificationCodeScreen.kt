package com.example.groceries_app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.groceries_app.ui.theme.GSshopTheme
import com.example.groceries_app.ui.theme.NectarGreen

@Composable
fun VerificationCodeScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onVerifyClick: (String) -> Unit = {},
    onResendClick: () -> Unit = {}
) {
    var code by remember { mutableStateOf("") }

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
                .padding(24.dp)
        ) {
            // Back Button
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Title
            Text(
                text = "Enter your 4-digit code",
                fontSize = 26.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Code Label
            Text(
                text = "Code",
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Code Display (4 dashes)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(4) { index ->
                    Text(
                        text = if (index < code.length) code[index].toString() else "-",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Normal,
                        color = if (index < code.length) Color.Black else Color.Gray
                    )
                    if (index < 3) {
                        Spacer(modifier = Modifier.width(16.dp))
                    }
                }
            }

            // Divider
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color(0xFFE2E2E2))
            )

            Spacer(modifier = Modifier.weight(1f))

            // Resend Code Link
            TextButton(
                onClick = onResendClick,
                modifier = Modifier.align(Alignment.Start)
            ) {
                Text(
                    text = "Resend Code",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal,
                    color = NectarGreen
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Custom Numeric Keypad
            NumericKeypad(
                onNumberClick = { number ->
                    if (code.length < 4) {
                        code += number
                    }
                },
                onDeleteClick = {
                    if (code.isNotEmpty()) {
                        code = code.dropLast(1)
                    }
                }
            )

            Spacer(modifier = Modifier.height(24.dp))
        }

        // Floating Action Button (Next/Verify)
        FloatingActionButton(
            onClick = { onVerifyClick(code) },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp)
                .size(64.dp),
            containerColor = NectarGreen,
            shape = CircleShape
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Verify",
                tint = Color.White,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

@Composable
fun VerificationKeypad(
    onNumberClick: (String) -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Row 1: 1, 2, 3
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            VerificationKeypadButton(number = "1", letters = "", onClick = onNumberClick)
            VerificationKeypadButton(number = "2", letters = "ABC", onClick = onNumberClick)
            VerificationKeypadButton(number = "3", letters = "DEF", onClick = onNumberClick)
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Row 2: 4, 5, 6
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            VerificationKeypadButton(number = "4", letters = "GHI", onClick = onNumberClick)
            VerificationKeypadButton(number = "5", letters = "JKL", onClick = onNumberClick)
            VerificationKeypadButton(number = "6", letters = "MNO", onClick = onNumberClick)
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Row 3: 7, 8, 9
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            VerificationKeypadButton(number = "7", letters = "PQRS", onClick = onNumberClick)
            VerificationKeypadButton(number = "8", letters = "TUV", onClick = onNumberClick)
            VerificationKeypadButton(number = "9", letters = "WXYZ", onClick = onNumberClick)
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Row 4: +*#, 0, Delete
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // Plus/Asterisk/Hash button
            VerificationKeypadButton(number = "+✱#", letters = "", onClick = { /* Optional */ })

            // Zero button
            VerificationKeypadButton(number = "0", letters = "", onClick = onNumberClick)

            // Delete button
            Column(
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp)
                    .padding(horizontal = 4.dp)
                    .background(Color.White, RoundedCornerShape(8.dp))
                    .border(1.dp, Color(0xFFE2E2E2), RoundedCornerShape(8.dp))
                    .clickable { onDeleteClick() },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "⌫",
                    fontSize = 24.sp,
                    color = Color.Black
                )
            }
        }
    }
}

@Composable
fun RowScope.VerificationKeypadButton(
    number: String,
    letters: String,
    onClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .weight(1f)
            .height(56.dp)
            .padding(horizontal = 4.dp)
            .background(Color.White, RoundedCornerShape(8.dp))
            .border(1.dp, Color(0xFFE2E2E2), RoundedCornerShape(8.dp))
            .clickable { onClick(number) },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = number,
            fontSize = 24.sp,
            fontWeight = FontWeight.Normal,
            color = Color.Black
        )
        if (letters.isNotEmpty()) {
            Text(
                text = letters,
                fontSize = 10.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Gray,
                letterSpacing = 1.sp
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun VerificationCodeScreenPreview() {
    GSshopTheme {
        VerificationCodeScreen()
    }
}

