package com.app.psexchange

import com.app.psexchange.model.Balance

object Config {
  const val ACCESS_KEY = "8a87bf0b1ef89765a9dbfe8c4f79c161"
  const val COMMISSION_FEE = 0.7

  fun defaultBalance(): HashMap<String, Balance> {
    val list = HashMap<String, Balance>()
    list["EUR"] = Balance(currency = "EUR", value = 1000.0, rate = 1.0)
    return list
  }
}