package com.sahil.bmi_calculator.domain.Di.Repo

import com.google.firebase.auth.FirebaseAuth
import com.sahil.bmi_calculator.common.ResultState
import com.sahil.bmi_calculator.data.dto.AccountCred
import com.sahil.bmi_calculator.data.dto.UserDetails
import com.sahil.bmi_calculator.data.dto.WeightHistory
import kotlinx.coroutines.flow.Flow

interface Repo {
    fun signUp(accountCred: AccountCred): Flow<ResultState<String>>

    fun passwordReset(email:String) : Flow<ResultState<String>>

    fun detail(userDetails: UserDetails): Flow<ResultState<String>>
    fun login(accountCred: AccountCred): Flow<ResultState<String>>

    fun googleSignIn(idToken:String, accountCred: AccountCred): Flow<ResultState<String>>

    fun saveWeightRecord( weightHistory: WeightHistory): Flow<ResultState<String>>

    fun getUserDetails():Flow<ResultState<UserDetails>>

    fun updateProfile(userDetails: UserDetails): Flow<ResultState<String>>

//    fun getLastHistory(weightHistory: WeightHistory): Flow<ResultState<WeightHistory>>
    fun getBMIHistory(): Flow<ResultState<List<WeightHistory>>>
}