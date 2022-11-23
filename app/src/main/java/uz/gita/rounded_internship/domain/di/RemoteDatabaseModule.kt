package uz.gita.rounded_internship.domain.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttp
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uz.gita.rounded_internship.data.remote.DialogApi
import uz.gita.rounded_internship.utils.BASE_URL
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RemoteDatabaseModule {
    @Provides
    @Singleton
    fun loggingInterceptor() = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    @Provides
    @Singleton
    fun client(@ApplicationContext context: Context, loggingInterceptor: HttpLoggingInterceptor) =
        OkHttpClient
            .Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(ChuckerInterceptor(context))
            .build()

    @Provides
    fun provideRetrofit(client: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    @Provides
    fun dialogApi(retrofit: Retrofit) = retrofit.create(DialogApi::class.java)
}