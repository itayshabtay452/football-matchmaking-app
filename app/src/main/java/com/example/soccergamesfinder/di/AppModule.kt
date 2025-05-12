package com.example.soccergamesfinder.di

import android.app.Application
import com.example.soccergamesfinder.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import android.content.Context
import com.example.soccergamesfinder.services.GoogleSignInService
import com.example.soccergamesfinder.services.GoogleSignInServiceImpl
import com.example.soccergamesfinder.services.GptService
import com.example.soccergamesfinder.services.GptServiceImpl
import com.example.soccergamesfinder.services.LocationService
import com.example.soccergamesfinder.services.LocationServiceImpl
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseStorage(): FirebaseStorage = FirebaseStorage.getInstance()

    @Provides
    @Singleton
    fun provideApplicationContext(application: Application): Context {
        return application.applicationContext
    }

    @Provides
    @Singleton
    fun provideGoogleSignClient(context: Context): GoogleSignInClient {
        val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        return GoogleSignIn.getClient(context, options)
    }

    @Provides
    @Singleton
    fun provideGoogleSignInService(
        googleClient: GoogleSignInClient
    ): GoogleSignInService {
        return GoogleSignInServiceImpl(googleClient)
    }

    @Provides
    @Singleton
    fun provideFusedLocationProviderClient(application: Application): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(application)
    }

    @Provides
    @Singleton
    fun provideLocationService(
        fusedLocationProviderClient: FusedLocationProviderClient,
        @ApplicationContext context: Context
    ): LocationService {
        return LocationServiceImpl(fusedLocationProviderClient, context)
    }

    @Provides
    @Singleton
    fun provideGptService(): GptService {
        val apiKey = "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" // או משאב מקובץ סודי
        return GptServiceImpl(apiKey)
    }
}