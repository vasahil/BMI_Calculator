package com.sahil.bmi_calculator.presentation.DetailScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Accessibility
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.Height
import androidx.compose.material.icons.filled.MonitorWeight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Wc
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.sahil.bmi_calculator.data.dto.UserDetails
import com.sahil.bmi_calculator.presentation.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(viewModel: DetailViewModel = hiltViewModel(), navController: NavController){

    val state = viewModel.details.collectAsState()

    var name by rememberSaveable { mutableStateOf("") }
    var lastName by rememberSaveable { mutableStateOf("") }
    var age by rememberSaveable { mutableStateOf("") }
    var height by rememberSaveable { mutableStateOf("") }
    var weight by rememberSaveable { mutableStateOf("") }
    var gender by rememberSaveable { mutableStateOf("") }
    var showError by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Personal Details",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Enter your information",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("First Name") },
                        placeholder = { Text("Enter your first name") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                        singleLine = true,
                        isError = showError && name.isEmpty(),
                        supportingText = if (showError && name.isEmpty()) {
                            { Text("First name is required") }
                        } else null,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                        )
                    )

                    OutlinedTextField(
                        value = lastName,
                        onValueChange = { lastName = it },
                        label = { Text("Last Name") },
                        placeholder = { Text("Enter your last name") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = age,
                        onValueChange = { if (it.all { char -> char.isDigit() }) age = it },
                        label = { Text("Age") },
                        placeholder = { Text("Enter your age") },
                        leadingIcon = { Icon(Icons.Default.Cake, contentDescription = null) },
                        singleLine = true,
                        isError = showError && age.isEmpty(),
                        supportingText = if (showError && age.isEmpty()) {
                            { Text("Age is required") }
                        } else null,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = height,
                        onValueChange = { if (it.isEmpty() || it.matches(Regex("^\\d*\\.?\\d*$"))) height = it },
                        label = { Text("Height (cm)") },
                        placeholder = { Text("Enter your height") },
                        leadingIcon = { Icon(Icons.Default.Height, contentDescription = null) },
                        singleLine = true,
                        isError = showError && height.isEmpty(),
                        supportingText = if (showError && height.isEmpty()) {
                            { Text("Height is required") }
                        } else null,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = weight,
                        onValueChange = { if (it.isEmpty() || it.matches(Regex("^\\d*\\.?\\d*$"))) weight = it },
                        label = { Text("Weight (kg)") },
                        placeholder = { Text("Enter your weight") },
                        leadingIcon = { Icon(Icons.Default.MonitorWeight, contentDescription = null) },
                        singleLine = true,
                        isError = showError && weight.isEmpty(),
                        supportingText = if (showError && weight.isEmpty()) {
                            { Text("Weight is required") }
                        } else null,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text(
                        text = "Gender",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 4.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = gender == "Male",
                            onClick = { gender = "Male" },
                            label = { Text("Male") },
                            leadingIcon = if (gender == "Male") {
                                { Icon(Icons.Default.Wc, contentDescription = null) }
                            } else null,
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        )
                        FilterChip(
                            selected = gender == "Female",
                            onClick = { gender = "Female" },
                            label = { Text("Female") },
                            leadingIcon = if (gender == "Female") {
                                { Icon(Icons.Default.Wc, contentDescription = null) }
                            } else null,
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        )
                        FilterChip(
                            selected = gender == "Other",
                            onClick = { gender = "Other" },
                            label = { Text("Other") },
                            leadingIcon = if (gender == "Other") {
                                { Icon(Icons.Default.Accessibility, contentDescription = null) }
                            } else null,
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        )
                    }

                    if (showError && gender.isEmpty()) {
                        Text(
                            text = "Please select a gender",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    if (name.isNotEmpty() && age.isNotEmpty() && height.isNotEmpty() &&
                        weight.isNotEmpty() && gender.isNotEmpty()) {
                        viewModel.Details(
                            userDetails = UserDetails(
                                name = name,
                                lastName = lastName,
                                age = age,
                                weight = weight,
                                height = height,
                                gender = gender
                            )
                        )
                        navController.navigate(Routes.Home.route)
                    } else {
                        showError = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            ) {
                Text(
                    text = "Calculate BMI",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}