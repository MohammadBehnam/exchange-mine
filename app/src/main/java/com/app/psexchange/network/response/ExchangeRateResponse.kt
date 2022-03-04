package com.app.psexchange.network.response

data class ExchangeRateResponse(
  val success: Boolean,
  val rates: Map<String, Double>
)