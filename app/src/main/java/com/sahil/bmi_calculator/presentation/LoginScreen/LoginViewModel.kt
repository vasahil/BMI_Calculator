package com.sahil.bmi_calculator.presentation.LoginScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sahil.bmi_calculator.common.ResultState
import com.sahil.bmi_calculator.data.dto.AccountCred
import com.sahil.bmi_calculator.data.dto.RepoImpl.RepoImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repoImpl: RepoImpl
) : ViewModel() {

    private val _login = MutableStateFlow(LoginState())
    val login = _login.asStateFlow()

    private val _reset = MutableStateFlow(LoginState())
    val reset = _reset.asStateFlow()



    fun loginUser(accountCred: AccountCred) {
        viewModelScope.launch {
            repoImpl.login(accountCred).collect { it ->
                when (it) {
                    is ResultState.Error -> {
                        _login.value = _login.value.copy(
                            loading = false,
                            error = it.message
                        )
                    }

                    is ResultState.Loading -> {
                        _login.value = _login.value.copy(
                            loading = true
                        )
                    }

                    is ResultState.Success -> {
                        _login.value = _login.value.copy(
                            loading = false,
                            success = it.data
                        )
                    }
                }
            }
        }
    }


    fun passReset(email: String) {
        viewModelScope.launch {
            repoImpl.passwordReset(email).collect { it ->
                when (it) {
                    is ResultState.Error -> {
                        _reset.value = _reset.value.copy(
                            loading = false,
                            error = it.message
                        )
                    }

                    is ResultState.Loading -> {
                        _reset.value = _reset.value.copy(
                            loading = true,
                        )
                    }

                    is ResultState.Success -> {
                        _reset.value = _reset.value.copy(
                            loading = false,
                            success = it.data
                        )
                    }
                }
            }
        }
    }




}