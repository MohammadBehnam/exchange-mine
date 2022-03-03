package com.app.psexchange.model

data class Exchange(
  var balance: Double = 0.0,
  var sell: Balance,
  var receive: Balance
)