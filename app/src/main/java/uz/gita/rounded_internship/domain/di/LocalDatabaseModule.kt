package uz.gita.rounded_internship.domain.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import uz.gita.rounded_internship.data.local.room.AppDatabase
import uz.gita.rounded_internship.data.local.room.StateDao
import uz.gita.rounded_internship.data.local.sharedPref.SharedPref
import uz.gita.rounded_internship.utils.LOCAL_DATABASE_NAME
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LocalDatabaseModule {

    @Provides
    @Singleton
    fun provideSharedPref(@ApplicationContext context: Context): SharedPref =
        SharedPref(context)

    @Provides
    @Singleton
    fun provideLocalDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, LOCAL_DATABASE_NAME).build()

    @Provides
    @Singleton
    fun provideStateDao(appDatabase: AppDatabase): StateDao = appDatabase.stateDao()
}