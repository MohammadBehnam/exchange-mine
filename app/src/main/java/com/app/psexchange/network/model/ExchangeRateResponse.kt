package com.app.psexchange.network.model

data class ExchangeRateResponse(
  val success: Boolean,
  val timestamp: Long,
  val base: String,
  val date: String,
  val rates: Map<String, Double>
)