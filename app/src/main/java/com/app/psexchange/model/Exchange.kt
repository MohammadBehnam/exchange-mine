package com.app.psexchange.model

data class Exchange(
  var sell: Balance,
  var receive: Balance,
  var commission: Double
)