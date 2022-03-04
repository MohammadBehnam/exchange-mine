package com.app.psexchange.network

import com.app.psexchange.network.response.ExchangeRateResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {
  @GET("v1/latest")
  suspend fun latest(
    @Query("access_key") accessKey: String,
  ): ExchangeRateResponse?
}
