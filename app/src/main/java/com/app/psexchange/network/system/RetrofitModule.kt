package com.app.psexchange.network.system

import com.app.psexchange.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RetrofitModule {
  @Provides
  @Singleton
  internal fun providesRetrofit(): Retrofit {
    return Retrofit.Builder()
      .addConverterFactory(GsonConverterFactory.create())
      .baseUrl("http://api.exchangeratesapi.io/")
      .client(networkClient())
      .build()
  }
  
  private fun networkClient(): OkHttpClient {
    val builder = OkHttpClient.Builder()
      .readTimeout(30, TimeUnit.SECONDS)
      .writeTimeout(30, TimeUnit.SECONDS)
      .connectTimeout(30, TimeUnit.SECONDS)
      .addInterceptor(logInterceptor())
    
    return builder.build()
  }
  
  private fun logInterceptor(): Interceptor {
    val interceptor = HttpLoggingInterceptor()
    if (BuildConfig.DEBUG) {
      interceptor.level = HttpLoggingInterceptor.Level.BODY
    } else {
      interceptor.level = HttpLoggingInterceptor.Level.NONE
    }
    return interceptor
  }
}
