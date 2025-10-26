package com.sahil.bmi_calculator.presentation.SignUpScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sahil.bmi_calculator.common.ResultState
import com.sahil.bmi_calculator.data.dto.AccountCred
import com.sahil.bmi_calculator.data.dto.RepoImpl.RepoImpl
import com.sahil.bmi_calculator.presentation.LoginScreen.LoginState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val repoImpl: RepoImpl
): ViewModel(){

    private val _signUp = MutableStateFlow(SignUpState())
    val signUp = _signUp.asStateFlow()

    private val _googleSignIn = MutableStateFlow(SignUpState())
    val googleSignIn = _googleSignIn.asStateFlow()

    fun signUp(accountCred: AccountCred) {
        viewModelScope.launch {
            repoImpl.signUp(accountCred).collect {
                when(it) {
                    is ResultState.Error -> {
                        _signUp.value = _signUp.value.copy(
                            loading = false,
                            error = it.message
                        )
                    }

                    is ResultState.Loading -> {
                        _signUp.value = _signUp.value.copy(
                            loading = true
                        )
                    }

                    is ResultState.Success -> {
                        _signUp.value = _signUp.value.copy(
                            loading = false,
                            success = it.data
                        )
                    }


                }
            }
        }
    }


    fun googleSignIn(idToken: String, accountCred: AccountCred) {
        viewModelScope.launch {
            repoImpl.googleSignIn(idToken, accountCred).collect { it ->
                when (it) {
                    is ResultState.Error -> {
                        _googleSignIn.value = _googleSignIn.value.copy(
                            loading = false,
                            error = it.message
                        )
                    }

                    is ResultState.Loading -> {
                        _googleSignIn.value = _googleSignIn.value.copy(
                            loading = true,
                        )
                    }

                    is ResultState.Success -> {
                        _googleSignIn.value = _googleSignIn.value.copy(
                            loading = false,
                            success = it.data
                        )
                    }
                }

            }
        }
    }
}