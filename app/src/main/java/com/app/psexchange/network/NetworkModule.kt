package com.app.psexchange.network

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {
  @Provides
  @Singleton
  fun provideRatesApi(retrofit: Retrofit): RatesApi {
    return retrofit.create(RatesApi::class.java)
  }
}
