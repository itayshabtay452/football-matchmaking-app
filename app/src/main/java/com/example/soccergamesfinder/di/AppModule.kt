package com.example.soccergamesfinder.di

import android.app.Application
import android.content.Context
import com.example.soccergamesfinder.R
import com.example.soccergamesfinder.services.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
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
import javax.inject.Singleton

/**
 * Dependency Injection module that provides application-wide singletons
 * using Hilt's SingletonComponent scope.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    /** Provides a singleton instance of FirebaseAuth */
    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    /** Provides a singleton instance of Firestore */
    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    /** Provides a singleton instance of Firebase Storage */
    @Provides
    @Singleton
    fun provideFirebaseStorage(): FirebaseStorage = FirebaseStorage.getInstance()

    /** Provides the application context */
    @Provides
    @Singleton
    fun provideApplicationContext(application: Application): Context {
        return application.applicationContext
    }

    /**
     * Provides a configured GoogleSignInClient
     *
     * @param context Application context
     */
    @Provides
    @Singleton
    fun provideGoogleSignClient(context: Context): GoogleSignInClient {
        val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        return GoogleSignIn.getClient(context, options)
    }

    /**
     * Provides an instance of GoogleSignInService
     *
     * @param googleClient GoogleSignInClient used internally
     */
    @Provides
    @Singleton
    fun provideGoogleSignInService(
        googleClient: GoogleSignInClient
    ): GoogleSignInService {
        return GoogleSignInServiceImpl(googleClient)
    }

    /**
     * Provides an instance of FusedLocationProviderClient
     *
     * @param application Application used to initialize location services
     */
    @Provides
    @Singleton
    fun provideFusedLocationProviderClient(application: Application): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(application)
    }

    /**
     * Provides an instance of LocationService
     *
     * @param fusedLocationProviderClient Native location client
     * @param context Application context
     */
    @Provides
    @Singleton
    fun provideLocationService(
        fusedLocationProviderClient: FusedLocationProviderClient,
        @ApplicationContext context: Context
    ): LocationService {
        return LocationServiceImpl(fusedLocationProviderClient, context)
    }

    /**
     * Provides an instance of GptService with a static API key.
     *
     * ⚠️ Important: Avoid hardcoding your OpenAI key in code.
     * Move it to a secure location (e.g., BuildConfig, local.properties, remote config).
     */
    @Provides
    @Singleton
    fun provideGptService(): GptService {
        val apiKey = "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"
        return GptServiceImpl(apiKey)
    }
}
