package com.app.psexchange.network.model

data class BalanceModel(
  val currency: String,
  val balance: Double,
  val rate: Double
)