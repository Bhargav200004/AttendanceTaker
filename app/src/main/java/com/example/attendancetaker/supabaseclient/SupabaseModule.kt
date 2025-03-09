package com.example.attendancetaker.supabaseclient

import com.example.attendancetaker.BuildConfig
import com.example.attendancetaker.MySharedPreferenceDataStore
import com.example.attendancetaker.domain.authentication.repository.IAuthentication
import com.example.attendancetaker.domain.teacher.repository.IClassRoom
import com.example.attendancetaker.repository.AuthenticationImpl
import com.example.attendancetaker.repository.ClassRoomImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object SupabaseModule {

    @Provides
    @Singleton
    fun provideSupabaseClient() : SupabaseClient {
        return createSupabaseClient(
            supabaseUrl = BuildConfig.SUPABASE_URL,
            supabaseKey = BuildConfig.SUPABASE_ANON_KEY
        ){
            install(Postgrest)
            install(Auth)
        }
    }

    @Provides
    @Singleton
    fun provideSupabaseDatabase(client: SupabaseClient) : Postgrest {
        return client.postgrest
    }

    @Provides
    @Singleton
    fun provideSupabaseAuth(client: SupabaseClient) : Auth {
        return client.auth
    }

    @Provides
    @Singleton
    fun provideAuthenticationRepositoryImpl(
        auth: Auth
    ): IAuthentication {
        return AuthenticationImpl(
            auth = auth
        )
    }

    @Provides
    @Singleton
    fun provideClassRoomRepositoryImpl(
        database : Postgrest,
    ) : IClassRoom{
        return ClassRoomImpl(
            database =  database
        )
    }

}