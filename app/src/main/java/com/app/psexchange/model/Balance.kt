package com.app.psexchange.model

data class Balance(
  var currency: String = "",
  var value: Double = 0.0,
  var rate: Double = 0.0
)