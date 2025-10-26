package com.sahil.bmi_calculator.presentation.LoginScreen

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.sahil.bmi_calculator.R
import com.sahil.bmi_calculator.data.dto.AccountCred
import com.sahil.bmi_calculator.presentation.SignUpScreen.SignUpViewModel
import com.sahil.bmi_calculator.presentation.navigation.Routes

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    navController: NavController,
    viewModel1: SignUpViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val state = viewModel.login.collectAsStateWithLifecycle()
    val googleLoginIn = viewModel1.googleSignIn.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current

    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(context.getString(R.string.default_web_client_id))
        .requestEmail()
        .build()

    val googleSignInClient = GoogleSignIn.getClient(context, gso)

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            account?.idToken?.let { idToken ->
                viewModel1.googleSignIn(
                    idToken,
                    AccountCred(
                        email = account.email ?: "",
                        password = ""
                    )
                )
            }
        } catch (e: ApiException) {
            Toast.makeText(context, "Google sign-in failed: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    // Theme-aware background
    val isDarkTheme = androidx.compose.foundation.isSystemInDarkTheme()
    val backgroundGradient = if (isDarkTheme) {
        Brush.verticalGradient(
            colors = listOf(
                MaterialTheme.colorScheme.surface,
                MaterialTheme.colorScheme.background
            )
        )
    } else {
        Brush.verticalGradient(
            colors = listOf(
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f),
                MaterialTheme.colorScheme.surface
            )
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient)
    ) {
        if (state.value.loading || googleLoginIn.value.loading) {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        } else if (state.value.error != null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Card(
                    modifier = Modifier.padding(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = state.value.error ?: "Unknown error",
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            textAlign = TextAlign.Center
                        )

                    }
                }
            }
        } else if (state.value.success != null || googleLoginIn.value.success != null) {
            LaunchedEffect(Unit) {
                navController.navigate(Routes.Home.route) {
                    popUpTo(Routes.Login.route) { inclusive = true }
                }
            }
        } else {
            LoginContent(
                onLogin = { email, password ->
                    viewModel.loginUser(AccountCred(email = email, password = password))
                    Toast.makeText(context, "Logging in...", Toast.LENGTH_SHORT).show()
                },
                onForgotPassword = { email ->
                    if (email.isNotEmpty()) {
                        viewModel.passReset(email)
                        Toast.makeText(context, "Password reset email sent", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Enter your email first", Toast.LENGTH_SHORT).show()
                    }
                },
                onGoogleSignIn = {
                    val signInIntent = googleSignInClient.signInIntent
                    launcher.launch(signInIntent)
                },
                onNavigateToSignUp = {
                    navController.navigate(Routes.SingUp.route)
                },
                focusManager = focusManager
            )
        }
    }
}

@Composable
private fun LoginContent(
    onLogin: (String, String) -> Unit,
    onForgotPassword: (String) -> Unit,
    onGoogleSignIn: () -> Unit,
    onNavigateToSignUp: () -> Unit,
    focusManager: androidx.compose.ui.focus.FocusManager
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    var emailError by rememberSaveable { mutableStateOf<String?>(null) }
    var passwordError by rememberSaveable { mutableStateOf<String?>(null) }

    fun validateEmail(email: String): Boolean {
        return if (email.isEmpty()) {
            emailError = "Email is required"
            false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailError = "Invalid email format"
            false
        } else {
            emailError = null
            true
        }
    }

    fun validatePassword(password: String): Boolean {
        return if (password.isEmpty()) {
            passwordError = "Password is required"
            false
        } else if (password.length < 6) {
            passwordError = "Password must be at least 6 characters"
            false
        } else {
            passwordError = null
            true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Header
        Text(
            text = "Welcome Back",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = "Log in to continue",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Email Field
        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                if (emailError != null) validateEmail(it)
            },
            label = { Text("Email") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = "Email Icon"
                )
            },
            isError = emailError != null,
            supportingText = {
                emailError?.let { Text(it) }
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(12.dp)
        )

        // Password Field
        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                if (passwordError != null) validatePassword(it)
            },
            label = { Text("Password") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Password Icon"
                )
            },
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = if (passwordVisible) "Hide password" else "Show password"
                    )
                }
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            isError = passwordError != null,
            supportingText = {
                passwordError?.let { Text(it) }
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus() }
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            shape = RoundedCornerShape(12.dp)
        )

        // Forgot Password
        TextButton(
            onClick = { onForgotPassword(email) },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(
                "Forgot Password?",
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Login Button
        Button(
            onClick = {
                val isEmailValid = validateEmail(email)
                val isPasswordValid = validatePassword(password)

                if (isEmailValid && isPasswordValid) {
                    onLogin(email, password)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Log In", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Sign Up Link
        Row(
            modifier = Modifier.padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Don't have an account?",
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
            TextButton(onClick = onNavigateToSignUp) {
                Text("Sign Up", fontWeight = FontWeight.SemiBold)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Divider with "OR"
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HorizontalDivider(modifier = Modifier.weight(1f))
            Text(
                text = "OR",
                modifier = Modifier.padding(horizontal = 16.dp),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                fontSize = 14.sp
            )
            HorizontalDivider(modifier = Modifier.weight(1f))
        }

        // Google Sign In Button
        OutlinedButton(
            onClick = onGoogleSignIn,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.onSurface
            )
        ) {
            Text("Continue with Google", fontSize = 16.sp)
        }
    }
}