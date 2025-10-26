package com.sahil.bmi_calculator.domain.Di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.sahil.bmi_calculator.data.dto.RepoImpl.RepoImpl
import com.sahil.bmi_calculator.domain.Di.Repo.Repo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn (SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun provideRepository(firebaseAuth: FirebaseAuth, firebaseFirestore: FirebaseFirestore): Repo {
        return RepoImpl(firebaseAuth, firebaseFirestore)
    }
}
