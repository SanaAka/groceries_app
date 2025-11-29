package com.example.groceries_app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.groceries_app.R
import com.example.groceries_app.ui.theme.GSshopTheme
import com.example.groceries_app.ui.theme.NectarGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationSelectionScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onSubmitClick: (zone: String, area: String) -> Unit = { _, _ -> }
) {
    var selectedZone by remember { mutableStateOf("Banasree") }
    var selectedArea by remember { mutableStateOf("") }
    var zoneExpanded by remember { mutableStateOf(false) }
    var areaExpanded by remember { mutableStateOf(false) }

    val zones = listOf("Banasree", "Dhaka", "Chittagong", "Sylhet", "Rajshahi")
    val areas = listOf("Type 1", "Type 2", "Type 3", "Type 4")

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
            // Back Button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.Black
                    )
                }
            }

            // Map Image
            Image(
                painter = painterResource(id = R.drawable.img_3),
                contentDescription = "Location Map",
                modifier = Modifier
                    .size(200.dp)
                    .padding(vertical = 16.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Title
            Text(
                text = "Select Your Location",
                fontSize = 26.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Subtitle
            Text(
                text = "Switch on your location to stay in tune with\nwhat's happening in your area",
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Your Zone Label
            Text(
                text = "Your Zone",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Gray,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            // Zone Dropdown
            ExposedDropdownMenuBox(
                expanded = zoneExpanded,
                onExpandedChange = { zoneExpanded = it }
            ) {
                OutlinedTextField(
                    value = selectedZone,
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier
                        .menuAnchor(type = MenuAnchorType.PrimaryNotEditable)
                        .fillMaxWidth(),
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "Dropdown",
                            tint = Color.Black
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        disabledContainerColor = Color.White,
                        focusedIndicatorColor = Color(0xFFE2E2E2),
                        unfocusedIndicatorColor = Color(0xFFE2E2E2)
                    ),
                    shape = RoundedCornerShape(8.dp)
                )

                ExposedDropdownMenu(
                    expanded = zoneExpanded,
                    onDismissRequest = { zoneExpanded = false }
                ) {
                    zones.forEach { zone ->
                        DropdownMenuItem(
                            text = { Text(zone) },
                            onClick = {
                                selectedZone = zone
                                zoneExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Your Area Label
            Text(
                text = "Your Area",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Gray,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            // Area Dropdown
            ExposedDropdownMenuBox(
                expanded = areaExpanded,
                onExpandedChange = { areaExpanded = it }
            ) {
                OutlinedTextField(
                    value = selectedArea.ifEmpty { "Types of your area" },
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier
                        .menuAnchor(type = MenuAnchorType.PrimaryNotEditable)
                        .fillMaxWidth(),
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "Dropdown",
                            tint = Color.Black
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        disabledContainerColor = Color.White,
                        focusedIndicatorColor = Color(0xFFE2E2E2),
                        unfocusedIndicatorColor = Color(0xFFE2E2E2),
                        disabledTextColor = Color.Gray,
                        focusedTextColor = if (selectedArea.isEmpty()) Color.Gray else Color.Black,
                        unfocusedTextColor = if (selectedArea.isEmpty()) Color.Gray else Color.Black
                    ),
                    shape = RoundedCornerShape(8.dp)
                )

                ExposedDropdownMenu(
                    expanded = areaExpanded,
                    onDismissRequest = { areaExpanded = false }
                ) {
                    areas.forEach { area ->
                        DropdownMenuItem(
                            text = { Text(area) },
                            onClick = {
                                selectedArea = area
                                areaExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Submit Button
            Button(
                onClick = { onSubmitClick(selectedZone, selectedArea) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(67.dp)
                    .padding(bottom = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = NectarGreen
                ),
                shape = RoundedCornerShape(19.dp)
            ) {
                Text(
                    text = "Submit",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LocationSelectionScreenPreview() {
    GSshopTheme {
        LocationSelectionScreen()
    }
}
