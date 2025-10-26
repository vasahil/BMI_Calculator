package com.sahil.bmi_calculator.presentation.ProfileScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sahil.bmi_calculator.common.ResultState
import com.sahil.bmi_calculator.data.dto.RepoImpl.RepoImpl
import com.sahil.bmi_calculator.data.dto.UserDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repoImpl: RepoImpl
): ViewModel() {

    private val _updateUser = MutableStateFlow(ProfileState())
    val updateUser = _updateUser.asStateFlow()

    fun updateUser(userDetails: UserDetails){
        viewModelScope.launch {
            repoImpl.updateProfile(userDetails).collect { it->

                when(it) {
                    is ResultState.Error -> {
                        _updateUser.value = _updateUser.value.copy(
                            loading = false,
                            error = it.message
                        )
                    }

                    is ResultState.Loading -> {
                        _updateUser.value = _updateUser.value.copy(
                            loading = true,
                        )
                    }

                    is ResultState.Success -> {
                        _updateUser.value = _updateUser.value.copy(
                            loading = false,
                            success = it.data
                        )
                    }
                }
            }
        }
    }
}