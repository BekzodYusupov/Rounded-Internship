package uz.gita.rounded_internship.domain.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uz.gita.rounded_internship.data.repository.Repository
import uz.gita.rounded_internship.data.repository.impl.RepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    @Singleton
    fun bindsRepository(impl: RepositoryImpl): Repository

}