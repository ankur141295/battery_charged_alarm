package com.ankur_anand.battery_charged_alarm.di

import android.content.Context
import com.ankur_anand.battery_charged_alarm.utils.pref_datastore.PrefDataStore
import com.ankur_anand.battery_charged_alarm.utils.pref_datastore.PrefDataStoreImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PrefDataStoreModule {

    @Provides
    @Singleton
    fun providePrefDataStore(
        @ApplicationContext  context : Context
    ) : PrefDataStore{
        return PrefDataStoreImpl(context)
    }
}