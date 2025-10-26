package com.sahil.bmi_calculator.presentation.HomeScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sahil.bmi_calculator.common.ResultState
import com.sahil.bmi_calculator.common.calculate
import com.sahil.bmi_calculator.data.dto.RepoImpl.RepoImpl
import com.sahil.bmi_calculator.data.dto.WeightHistory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repoImpl: RepoImpl
) : ViewModel() {

    private val _user = MutableStateFlow(HomeScreenState())
    val user = _user.asStateFlow()

    private val _bmi = MutableStateFlow(BMI())
    val bmi = _bmi.asStateFlow()

    private var lastWeight: String? = null
    private var isInitialBmiSaved = false

    private val _history = MutableStateFlow(HistoryState())
    val history = _history.asStateFlow()

    init {
        fetchHistory()
        loadLastWeightFromHistory()
        loadUserDetails()
    }

    private fun loadLastWeightFromHistory() {
        viewModelScope.launch {
            val lastEntry = repoImpl.getLastHistory()
            lastWeight = lastEntry?.weight
            isInitialBmiSaved = lastEntry != null
        }
    }

    fun loadUserDetails() {
        viewModelScope.launch {
            repoImpl.getUserDetails().collect { result ->
                when (result) {
                    is ResultState.Error -> _user.value = _user.value.copy(
                        loading = false,
                        error = result.message
                    )

                    is ResultState.Loading -> _user.value = _user.value.copy(
                        loading = true
                    )

                    is ResultState.Success -> {
                        val userData = result.data
                        _user.value = _user.value.copy(
                            loading = false,
                            success = userData
                        )

                        val w = userData.weight
                        val h = userData.height
                        val bmiVal = calculate(w, h)

                        if (bmiVal != null) {
                            _bmi.value = BMI(success = bmiVal)

                            if (!isInitialBmiSaved && w != null) {
                                saveBMI(bmiVal, w)
                                lastWeight = w
                                isInitialBmiSaved = true
                            }

                        } else {
                            _bmi.value = BMI(error = "Invalid data")
                        }
                    }
                }
            }
        }
    }

    fun submitNewWeight(newWeight: String?) {
        val height = _user.value.success?.height

        if (newWeight != null && newWeight != lastWeight) {
            val bmiVal = calculate(newWeight, height)

            if (bmiVal != null) {
                _bmi.value = BMI(success = bmiVal)
                saveBMI(bmiVal, newWeight)
                lastWeight = newWeight
            } else {
                _bmi.value = BMI(error = "Invalid weight or height")
            }
        }
    }

    private fun saveBMI(bmiVal: String, weight: String) {
        viewModelScope.launch {
            val record = WeightHistory(
                weight = weight,
                bmiVal = bmiVal,
                timeStamp = System.currentTimeMillis()
            )

            repoImpl.saveWeightRecord(record).collect { result ->
                when (result) {
                    is ResultState.Error -> _bmi.value = BMI(
                        loader = false,
                        error = result.message
                    )

                    is ResultState.Loading -> _bmi.value = BMI(loader = true)

                    is ResultState.Success -> _bmi.value = BMI(success = bmiVal)
                }
            }
        }
    }


    fun fetchHistory() {
        viewModelScope.launch {
            repoImpl.getBMIHistory().collect { result ->
                when (result) {
                    is ResultState.Loading -> {
                        _history.value = _history.value.copy(loader = true)
                    }

                    is ResultState.Success -> {
                        _history.value = _history.value.copy(
                            loader = false,
                            success = result.data // This should be a List<WeightHistory>
                        )
                    }

                    is ResultState.Error -> {
                        _history.value = _history.value.copy(
                            loader = false,
                            error = result.message
                        )
                    }
                }
            }
        }
    }
}

data class BMI(
    val loader: Boolean = false,
    val success: String? = null,
    val error: String? = null
)

data class HistoryState(
    val loader: Boolean = false,
    val success: List<WeightHistory> = emptyList(),
    val error: String? = null
)