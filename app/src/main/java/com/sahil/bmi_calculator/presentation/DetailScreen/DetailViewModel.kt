package com.sahil.bmi_calculator.presentation.DetailScreen

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
class DetailViewModel @Inject constructor(
    private val repoImpl: RepoImpl
): ViewModel(){

    private val _details = MutableStateFlow(DetailState())
    val details = _details.asStateFlow()

    fun Details(userDetails: UserDetails) {
        viewModelScope.launch {
            repoImpl.detail(userDetails).collect { it->
                when(it) {
                    is ResultState.Error -> {
                        _details.value = _details.value.copy(
                            loading = false,
                            error = it.message
                        )
                    }

                    is ResultState.Loading -> {
                        _details.value = _details.value.copy(
                            loading = true,

                        )
                    }

                    is ResultState.Success -> {
                        _details.value = _details.value.copy(
                            loading = false,
                            success = it.data
                        )
                    }
                }
            }
        }
    }
}