package com.app.psexchange.network.model

data class ExchangeRateResponse(
  val success: Boolean,
  val rates: Map<String, Double>
)