package com.sahil.bmi_calculator.presentation.ProfileScreen

import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Height
import androidx.compose.material.icons.filled.MonitorWeight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Wc
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.sahil.bmi_calculator.data.dto.UserDetails
import com.sahil.bmi_calculator.presentation.HomeScreen.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel(),
    viewModel1: HomeViewModel = hiltViewModel()
) {
    val state = viewModel.updateUser.collectAsStateWithLifecycle()
    val user = viewModel1.user.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    var name by rememberSaveable { mutableStateOf(user.value.success?.name ?: "") }
    var lastName by rememberSaveable { mutableStateOf(user.value.success?.lastName ?: "") }
    var weight by rememberSaveable { mutableStateOf(user.value.success?.weight ?: "") }
    var height by rememberSaveable { mutableStateOf(user.value.success?.height ?: "") }
    var age by rememberSaveable { mutableStateOf(user.value.success?.age ?: "") }
    var gender by rememberSaveable { mutableStateOf(user.value.success?.gender ?: "") }
    var hasChanges by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(user.value.success) {
        user.value.success?.let {
            name = it.name ?: ""
            lastName = it.lastName
            weight = it.weight ?: ""
            height = it.height ?: ""
            age = it.age ?: ""
            gender = it.gender
        }
    }

    LaunchedEffect(state.value.success) {
        if (state.value.success != null) {
            snackbarHostState.showSnackbar("Profile updated successfully!")
            hasChanges = false
        }
    }

    LaunchedEffect(state.value.success) {
        if(state.value.success != null){
            viewModel1.loadUserDetails()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                },
                title = {
                    Text(
                        "Edit Profile",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // Profile Avatar Section
            Surface(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape),
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profile",
                        modifier = Modifier.size(60.dp),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            Text(
                text = "${user.value.success?.name ?: "User"} ${user.value.success?.lastName ?: ""}".trim(),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Personal Information Card
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
                    Text(
                        text = "Personal Information",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    OutlinedTextField(
                        value = name,
                        onValueChange = {
                            name = it
                            hasChanges = true
                        },
                        label = { Text("First Name") },
                        placeholder = { Text("Enter your first name") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary
                        )
                    )

                    OutlinedTextField(
                        value = lastName,
                        onValueChange = {
                            lastName = it
                            hasChanges = true
                        },
                        label = { Text("Last Name") },
                        placeholder = { Text("Enter your last name") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = age,
                        onValueChange = {
                            if (it.all { char -> char.isDigit() } || it.isEmpty()) {
                                age = it
                                hasChanges = true
                            }
                        },
                        label = { Text("Age") },
                        placeholder = { Text("Enter your age") },
                        leadingIcon = { Icon(Icons.Default.Cake, contentDescription = null) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
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
                            onClick = {
                                gender = "Male"
                                hasChanges = true
                            },
                            label = { Text("Male") },
                            leadingIcon = if (gender == "Male") {
                                { Icon(Icons.Default.CheckCircle, contentDescription = null) }
                            } else null,
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        )
                        FilterChip(
                            selected = gender == "Female",
                            onClick = {
                                gender = "Female"
                                hasChanges = true
                            },
                            label = { Text("Female") },
                            leadingIcon = if (gender == "Female") {
                                { Icon(Icons.Default.CheckCircle, contentDescription = null) }
                            } else null,
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        )
                        FilterChip(
                            selected = gender == "Other",
                            onClick = {
                                gender = "Other"
                                hasChanges = true
                            },
                            label = { Text("Other") },
                            leadingIcon = if (gender == "Other") {
                                { Icon(Icons.Default.CheckCircle, contentDescription = null) }
                            } else null,
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        )
                    }
                }
            }

            // Body Measurements Card
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
                    Text(
                        text = "Body Measurements",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    OutlinedTextField(
                        value = height,
                        onValueChange = {
                            if (it.isEmpty() || it.matches(Regex("^\\d*\\.?\\d*$"))) {
                                height = it
                                hasChanges = true
                            }
                        },
                        label = { Text("Height") },
                        placeholder = { Text("Enter height in cm") },
                        leadingIcon = { Icon(Icons.Default.Height, contentDescription = null) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.fillMaxWidth(),
                        suffix = { Text("cm") }
                    )

                    OutlinedTextField(
                        value = weight,
                        onValueChange = {
                            if (it.isEmpty() || it.matches(Regex("^\\d*\\.?\\d*$"))) {
                                weight = it
                                hasChanges = true
                            }
                        },
                        label = { Text("Weight") },
                        placeholder = { Text("Enter weight in kg") },
                        leadingIcon = { Icon(Icons.Default.MonitorWeight, contentDescription = null) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.fillMaxWidth(),
                        suffix = { Text("kg") }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Save Button
            Button(
                onClick = {
                    val oldWeight = user.value.success?.weight
                    val newWeight = weight
                    viewModel.updateUser(
                        userDetails = UserDetails(
                            name = name,
                            lastName = lastName,
                            age = age,
                            weight = newWeight,
                            height = height,
                            gender = gender
                        )
                    )
                    if (newWeight != oldWeight) {
                        viewModel1.submitNewWeight(newWeight)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                enabled = hasChanges && name.isNotEmpty() && age.isNotEmpty() &&
                        height.isNotEmpty() && weight.isNotEmpty() && gender.isNotEmpty()
            ) {
                Icon(
                    imageVector = Icons.Default.Save,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = if (state.value.loading) "Saving..." else "Save Changes",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            if (!hasChanges) {
                Text(
                    text = "No changes to save",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}