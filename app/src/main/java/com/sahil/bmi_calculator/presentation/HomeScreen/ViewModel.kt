
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

    private var _history = MutableStateFlow(HistoryState())
    var history = _history.asStateFlow()

    init {
        fetchHistory()
        loadLastWeightFromHistory()
        loadUserDetails()
    }

    private fun loadLastWeightFromHistory() {
        viewModelScope.launch {
            val lastEntry = repoImpl.getLastHistory()
            lastWeight = lastEntry?.weight
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

                            // ✅ REMOVED automatic BMI saving on load
                            // Only calculate and display, don't save
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

        // ✅ Check if weight actually changed
        if (newWeight != null && newWeight != lastWeight && height != null) {
            val bmiVal = calculate(newWeight, height)

            if (bmiVal != null) {
                // ✅ Update UI state immediately
                _bmi.value = BMI(success = bmiVal)
                val updatedUser = _user.value.success?.copy(weight = newWeight)
                _user.value = _user.value.copy(success = updatedUser)

                // ✅ Save to Firestore (both history and main document)
                viewModelScope.launch {
                    // Save to history
                    saveBMI(bmiVal, newWeight)

                    // Update main user document
                    repoImpl.updateUserWeight(newWeight).collect { }
                }

                lastWeight = newWeight

                // ✅ Refresh history after saving
                fetchHistory()
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
                    is ResultState.Error -> {
                        println("Failed to save BMI: ${result.message}")
                    }
                    is ResultState.Loading -> { }
                    is ResultState.Success -> {
                        // Successfully saved
                    }
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
                            success = result.data.sortedBy { it.timeStamp }
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





//package com.sahil.bmi_calculator.presentation.HomeScreen
//
//
//
//
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.sahil.bmi_calculator.common.ResultState
//import com.sahil.bmi_calculator.common.calculate
//import com.sahil.bmi_calculator.data.dto.RepoImpl.RepoImpl
//import com.sahil.bmi_calculator.data.dto.WeightHistory
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//
//@HiltViewModel
//class HomeViewModel @Inject constructor(
//    private val repoImpl: RepoImpl
//) : ViewModel() {
//
//    private val _user = MutableStateFlow(HomeScreenState())
//    val user = _user.asStateFlow()
//
//    private val _bmi = MutableStateFlow(BMI())
//    val bmi = _bmi.asStateFlow()
//
//    private var lastWeight: String? = null
//    private var isInitialBmiSaved = false
//
//    private val _history = MutableStateFlow(HistoryState())
//    val history = _history.asStateFlow()
//
//    init {
//        fetchHistory()
//        loadLastWeightFromHistory()
//        loadUserDetails()
//    }
//
//
//    private fun loadLastWeightFromHistory() {
//        viewModelScope.launch {
//            val lastEntry = repoImpl.getLastHistory()
//            lastWeight = lastEntry?.weight
//            isInitialBmiSaved = lastEntry != null // ✅ So we don't store BMI again on restart
//        }
//    }
////    private fun loadLastWeightFromHistory() {
////        viewModelScope.launch {
////            val lastEntry = repoImpl.getLastHistory()
////            lastWeight = lastEntry?.weight
////            isInitialBmiSaved = lastEntry != null
////        }
////    }
//
//    fun loadUserDetails() {
//        viewModelScope.launch {
//            repoImpl.getUserDetails().collect { result ->
//                when (result) {
//                    is ResultState.Error -> _user.value = _user.value.copy(
//                        loading = false,
//                        error = result.message
//                    )
//
//                    is ResultState.Loading -> _user.value = _user.value.copy(
//                        loading = true
//                    )
//
//                    is ResultState.Success -> {
//                        val userData = result.data
//                        _user.value = _user.value.copy(
//                            loading = false,
//                            success = userData
//                        )
//
//                        val w = userData.weight
//                        val h = userData.height
//                        val bmiVal = calculate(w, h)
//
//                        if (bmiVal != null) {
//                            _bmi.value = BMI(success = bmiVal)
//
//                            // ✅ Save initial only if no history exists
//                            if (!isInitialBmiSaved && w != null) {
//                                saveBMI(bmiVal, w)
//                                lastWeight = w
//                                isInitialBmiSaved = true
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//
//    fun submitNewWeight(newWeight: String?) {
//        val height = _user.value.success?.height ?: return
//
//        if (newWeight != null && newWeight != lastWeight) {
//            viewModelScope.launch {
//                repoImpl.updateUserWeight(newWeight).collect { result ->
//                    if (result is ResultState.Success) {
//                        val bmiVal = calculate(newWeight, height)
//                        if (bmiVal != null) {
//                            saveBMI(bmiVal, newWeight)    // ✅ Save history only once
//                            lastWeight = newWeight
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//
////    fun submitNewWeight(newWeight: String?) {
////        val height = _user.value.success?.height
////
////        if (newWeight != null && newWeight != lastWeight) {
////            val bmiVal = calculate(newWeight, height)
////
////            if (bmiVal != null) {
////                _bmi.value = BMI(success = bmiVal)
////                saveBMI(bmiVal, newWeight)
////                lastWeight = newWeight
////            } else {
////                _bmi.value = BMI(error = "Invalid weight or height")
////            }
////        }
////    }
//
//    private fun saveBMI(bmiVal: String, weight: String) {
//        viewModelScope.launch {
//            val record = WeightHistory(
//                weight = weight,
//                bmiVal = bmiVal,
//                timeStamp = System.currentTimeMillis()
//            )
//
//            repoImpl.saveWeightRecord(record).collect { result ->
//                when (result) {
//                    is ResultState.Error -> _bmi.value = BMI(
//                        loader = false,
//                        error = result.message
//                    )
//
//                    is ResultState.Loading -> _bmi.value = BMI(loader = true)
//
//                    is ResultState.Success -> _bmi.value = BMI(success = bmiVal)
//                }
//            }
//        }
//    }
//
//
//    fun fetchHistory() {
//        viewModelScope.launch {
//            repoImpl.getBMIHistory().collect { result ->
//                when (result) {
//                    is ResultState.Loading -> {
//                        _history.value = _history.value.copy(loader = true)
//                    }
//
//                    is ResultState.Success -> {
//                        _history.value = _history.value.copy(
//                            loader = false,
//                            success = result.data // This should be a List<WeightHistory>
//                        )
//                    }
//
//                    is ResultState.Error -> {
//                        _history.value = _history.value.copy(
//                            loader = false,
//                            error = result.message
//                        )
//                    }
//                }
//            }
//        }
//    }
//
////    private fun updateUserWeight(weight: String) {
////        viewModelScope.launch {
////            repoImpl.updateUserWeight(weight).collect {
////                loadUserDetails() // Refresh UI state after update
////            }
////        }
////    }
//
//}
//
//data class BMI(
//    val loader: Boolean = false,
//    val success: String? = null,
//    val error: String? = null
//)
//
//data class HistoryState(
//    val loader: Boolean = false,
//    val success: List<WeightHistory> = emptyList(),
//    val error: String? = null
//)