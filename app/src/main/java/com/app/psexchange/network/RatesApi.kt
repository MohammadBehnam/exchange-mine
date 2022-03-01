package com.app.psexchange.network

import com.app.psexchange.network.model.ExchangeRateResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface RatesApi {
  @GET("v1/latest")
  suspend fun latest(
    @Query("access_key") accessKey: String,
  ): ExchangeRateResponse?
}
