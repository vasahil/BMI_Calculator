package com.sahil.bmi_calculator.data.dto.RepoImpl



import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.sahil.bmi_calculator.common.ResultState
import com.sahil.bmi_calculator.data.dto.AccountCred
import com.sahil.bmi_calculator.data.dto.UserDetails
import com.sahil.bmi_calculator.data.dto.WeightHistory
import com.sahil.bmi_calculator.domain.Di.Repo.Repo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RepoImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore
) : Repo {

    override fun signUp(accountCred: AccountCred): Flow<ResultState<String>> = flow {
        emit(ResultState.Loading)

        try {
            val authResult = firebaseAuth.createUserWithEmailAndPassword(
                accountCred.email,
                accountCred.password
            ).await()

            val uid = authResult.user?.uid ?: throw Exception("UID is null")

            firebaseFirestore.collection("USER").document(uid)
                .set(accountCred).await()

            emit(ResultState.Success("Signup Successful"))
        } catch (e: Exception) {
            emit(ResultState.Error(e.message ?: "Signup Error"))
        }
    }

    override fun login(accountCred: AccountCred): Flow<ResultState<String>> = flow {
        emit(ResultState.Loading)
        try {
            firebaseAuth.signInWithEmailAndPassword(accountCred.email, accountCred.password).await()
            emit(ResultState.Success("Login Successful"))
        } catch (e: Exception) {
            emit(ResultState.Error(e.message ?: "Login Error"))
        }
    }

    override fun passwordReset(email: String): Flow<ResultState<String>> = flow {
        emit(ResultState.Loading)
        try {
            firebaseAuth.sendPasswordResetEmail(email).await()
            emit(ResultState.Success("Reset email sent"))
        } catch (e: Exception) {
            emit(ResultState.Error(e.message ?: "Reset failed"))
        }
    }

    override fun detail(userDetails: UserDetails): Flow<ResultState<String>> = flow {
        emit(ResultState.Loading)
        try {
            val uid = firebaseAuth.currentUser?.uid ?: throw Exception("User not logged in")
            firebaseFirestore.collection("USER").document(uid).set(userDetails).await()
            emit(ResultState.Success("Details Saved"))
        } catch (e: Exception) {
            emit(ResultState.Error(e.message ?: "Error Saving Details"))
        }
    }

    override fun updateProfile(userDetails: UserDetails): Flow<ResultState<String>> = flow {
        emit(ResultState.Loading)
        try {
            val uid = firebaseAuth.currentUser?.uid ?: throw Exception("User not logged in")
            firebaseFirestore.collection("USER").document(uid).set(userDetails).await()
            emit(ResultState.Success("Profile Updated"))
        } catch (e: Exception) {
            emit(ResultState.Error(e.message ?: "Update Failed"))
        }
    }

    override fun googleSignIn(idToken: String, accountCred: AccountCred): Flow<ResultState<String>> = flow {
        emit(ResultState.Loading)
        try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val authResult = firebaseAuth.signInWithCredential(credential).await()

            val uid = authResult.user?.uid ?: throw Exception("UID is null")
            firebaseFirestore.collection("USER").document(uid).set(accountCred).await()

            emit(ResultState.Success("Google Login Successful"))
        } catch (e: Exception) {
            emit(ResultState.Error(e.message ?: "Google SignIn Failed"))
        }
    }

    override fun saveWeightRecord(weightHistory: WeightHistory): Flow<ResultState<String>> = flow {
        emit(ResultState.Loading)
        try {
            val uid = firebaseAuth.currentUser?.uid ?: throw Exception("User not logged in")
            firebaseFirestore.collection("USER")
                .document(uid)
                .collection("weightHistory")
                .add(weightHistory).await()

            emit(ResultState.Success("Weight Record Saved"))
        } catch (e: Exception) {
            emit(ResultState.Error(e.message ?: "Save Failed"))
        }
    }

    override fun getBMIHistory(): Flow<ResultState<List<WeightHistory>>> = flow {
        emit(ResultState.Loading)
        try {
            val uid = firebaseAuth.currentUser?.uid ?: throw Exception("User not logged in")
            val snapshot = firebaseFirestore.collection("USER")
                .document(uid)
                .collection("weightHistory")
                .orderBy("timeStamp")
                .get().await()

            val list = snapshot.toObjects(WeightHistory::class.java)

            emit(ResultState.Success(list))
        } catch (e: Exception) {
            emit(ResultState.Error(e.message ?: "Fetch Failed"))
        }
    }

    suspend fun getLastHistory(): WeightHistory? {
        val uid = firebaseAuth.currentUser?.uid ?: return null
        val snapshot = firebaseFirestore.collection("USER")
            .document(uid)
            .collection("weightHistory")
            .orderBy("timeStamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .limit(1)
            .get().await()

        return snapshot.toObjects(WeightHistory::class.java).firstOrNull()
    }

    override fun getUserDetails(): Flow<ResultState<UserDetails>> = flow {
        emit(ResultState.Loading)
        try {
            val uid = firebaseAuth.currentUser?.uid ?: throw Exception("User not logged in")
            val snapshot = firebaseFirestore.collection("USER")
                .document(uid)
                .get().await()

            val user = snapshot.toObject(UserDetails::class.java)
                ?: throw Exception("No user details found")

            emit(ResultState.Success(user))
        } catch (e: Exception) {
            emit(ResultState.Error(e.message ?: "Failed to fetch user details"))
        }
    }


    override fun updateUserWeight(newWeight: String): Flow<ResultState<String>> = flow {
        emit(ResultState.Loading)

        try {
            val uid = firebaseAuth.currentUser?.uid ?: throw Exception("User not logged in")

            firebaseFirestore.collection("USER")
                .document(uid)
                .update("weight", newWeight)
                .await()

            emit(ResultState.Success("Weight updated"))
        } catch (e: Exception) {
            emit(ResultState.Error(e.message ?: "Failed to update weight"))
        }
    }

}

//
//import androidx.compose.ui.graphics.RectangleShape
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.auth.GoogleAuthProvider
//import com.google.firebase.firestore.FirebaseFirestore
//import com.patrykandpatrick.vico.views.chart.column.columnLineComponent
//
//import com.sahil.bmi_calculator.common.ResultState
//import com.sahil.bmi_calculator.common.calculate
//import com.sahil.bmi_calculator.data.dto.AccountCred
//import com.sahil.bmi_calculator.data.dto.UserDetails
//import com.sahil.bmi_calculator.data.dto.WeightHistory
//import com.sahil.bmi_calculator.domain.Di.Repo.Repo
//import kotlinx.coroutines.channels.awaitClose
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.callbackFlow
//import kotlinx.coroutines.tasks.await
//import javax.inject.Inject
//
//class RepoImpl @Inject constructor(
//    private val firebaseAuth: FirebaseAuth,
//    private val firebaseFirestore: FirebaseFirestore
//) : Repo {
//    override fun signUp(accountCred: AccountCred): Flow<ResultState<String>>  = callbackFlow{
//          trySend(ResultState.Loading)
//        firebaseAuth.createUserWithEmailAndPassword(accountCred.email, accountCred.password).addOnCompleteListener { it->
//            if(it.isSuccessful) {
//                val uid = it.result.user?.uid ?: ""
//                firebaseFirestore.collection("USER").document(uid).set(accountCred)
//                    .addOnCompleteListener { it ->
//                        if (it.isSuccessful) {
//                            trySend(ResultState.Success("SingUp SuccessFully"))
//                        } else {
//                            if (it.exception != null) {
//                                trySend(ResultState.Error(it.exception?.localizedMessage.toString()))
//                            }
//                        }
//
//                    }
//                trySend(ResultState.Success("User Registered Successfully"))
//            }else{
//                if(it.exception!=null){
//                    trySend(ResultState.Error(it.exception?.localizedMessage.toString()))
//                }
//            }
//        }
//        awaitClose { close() }
//    }
//
//    override fun passwordReset(email: String): Flow<ResultState<String>> = callbackFlow {
//        trySend(ResultState.Loading)
//
//        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener { it->
//            if(it.isSuccessful) {
//                trySend(ResultState.Success("Email Sent Successfully"))
//            }else{
//                if(it.exception!=null){
//                    trySend(ResultState.Error(it.exception?.localizedMessage.toString()))
//                }
//            }
//        }
//        awaitClose {
//            close()
//        }
//    }
//    override fun detail(userDetails: UserDetails): Flow<ResultState<String>>  = callbackFlow{
//        trySend(ResultState.Loading)
//
//        val uid = firebaseAuth.currentUser?.uid
//        if (uid == null) {
//            trySend(ResultState.Error("User is not registered"))
//            close() // ✅ Stop execution early
//            return@callbackFlow
//        }
//
//
//        firebaseFirestore.collection("USER").document(uid.toString()).set(userDetails).addOnSuccessListener {
//            trySend(ResultState.Success("User details Saved"))
//            close()
//
//        }.addOnFailureListener {
//            trySend(ResultState.Error(it.localizedMessage ?: "Unknown error"))
//            close()
//
//        }
//    awaitClose { close() }
//
//
//    }
//    override fun login(accountCred: AccountCred): Flow<ResultState<String>> = callbackFlow {
//        trySend(ResultState.Loading)
//
//        firebaseAuth.signInWithEmailAndPassword(accountCred.email, accountCred.password).addOnCompleteListener {
//            if(it.isSuccessful) {
//              trySend(ResultState.Success("Login Successfully"))
//            }else{
//                if(it.exception!=null){
//                    trySend(ResultState.Error(it.exception?.localizedMessage.toString()))
//                }
//            }
//        }
//        awaitClose {
//            close()
//        }
//    }
//
//    override fun googleSignIn(
//        idToken: String,
//        accountCred: AccountCred
//    ): Flow<ResultState<String>>  = callbackFlow{
//        trySend(ResultState.Loading)
//
//        val credential = GoogleAuthProvider.getCredential(idToken, null)
//        firebaseAuth.signInWithCredential(credential).addOnCompleteListener { it->
//            if(it.isSuccessful) {
//                val uid = it.result.user?.uid?:""
//                firebaseFirestore.collection("USER").document(uid).set(accountCred).addOnCompleteListener {
//                    if(it.isSuccessful){
//                        trySend(ResultState.Success("SignUp Successfully"))
//                    }else{
//                        if(it.exception!=null){
//                            trySend(ResultState.Error(it.exception?.localizedMessage.toString()))
//                        }
//                    }
//
//                }
//                trySend(ResultState.Success("User Registered Successfully"))
//            }else{
//                if(it.exception!= null) {
//                    trySend(ResultState.Error(it.exception?.localizedMessage.toString()))
//                }
//            }
//        }
//        awaitClose { close() }
//    }
//
//
//    override fun saveWeightRecord(
//        weightHistory: WeightHistory
//    ): Flow<ResultState<String>> = callbackFlow{
//        trySend(ResultState.Loading)
//
//        val uid = firebaseAuth.currentUser?.uid
//        if (uid == null) {
//            trySend(ResultState.Error("User not logged in"))
//            close()
//            return@callbackFlow
//        }
//
//
//
//
//        firebaseFirestore.collection("USER")
//            .document(uid)
//            .collection("weightHistory")
//            .add(weightHistory)
//            .addOnSuccessListener {
//                trySend(ResultState.Success("Weight Saved"))
//            }
//            .addOnFailureListener { e ->
//                trySend(ResultState.Error(e.message ?: "Error"))
//            }
//
//        awaitClose { close() }
//    }
//
//    override fun getUserDetails(): Flow<ResultState<UserDetails>> = callbackFlow {
//        trySend(ResultState.Loading)
//
//        val uid = firebaseAuth.currentUser?.uid?:""
//
//        firebaseFirestore.collection("USER").document(uid).get().addOnSuccessListener { document->
//            if(document.exists()){
//                val user = document.toObject(UserDetails::class.java)
//                trySend(ResultState.Success(user) as ResultState<UserDetails>)
//                close()
//            }
//        }.addOnFailureListener { e ->
//            trySend(ResultState.Error(e.message ?: "Error"))
//            close()
//        }
//        awaitClose { close() }
//    }
//
//    override fun updateProfile(userDetails: UserDetails): Flow<ResultState<String>> = callbackFlow{
//        trySend(ResultState.Loading)
//
//        val uid = firebaseAuth.currentUser?.uid?:""
//        firebaseFirestore.collection("USER").document(uid).set(userDetails).addOnSuccessListener {
//            trySend(ResultState.Success("Updated Successfully"))
//
//        }.addOnFailureListener { e->
//            trySend(ResultState.Error(e.message?:"Error"))
//        }
//        awaitClose { close() }
//    }
//
//    suspend fun getLastHistory(): WeightHistory? {
//
//        val uid = firebaseAuth.currentUser?.uid ?: return null
//        val snapshot = firebaseFirestore.collection("USER")
//            .document(uid)
//            .collection("weightHistory")
//            .orderBy("timeStamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
//            .limit(1)
//            .get()
//            .await()
//
//        return snapshot.documents.firstOrNull()?.toObject(WeightHistory::class.java)
//
//    }
//
//    override fun getBMIHistory(): Flow<ResultState<List<WeightHistory>>> = callbackFlow {
//        trySend(ResultState.Loading)
//        val uid = firebaseAuth.currentUser?.uid ?: ""
//        firebaseFirestore.collection("USER")
//            .document(uid)
//            .collection("weightHistory")
//            .orderBy("timeStamp")
//            .get()
//            .addOnSuccessListener { snapshot ->
//                val list = snapshot.documents.mapNotNull { it.toObject(WeightHistory::class.java) }
//                trySend(ResultState.Success(list))
//            }
//            .addOnFailureListener {
//                trySend(ResultState.Error(it.message ?: "Error fetching history"))
//            }
//        awaitClose { close() }
//    }
//
//}