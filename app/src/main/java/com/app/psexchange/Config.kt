package com.app.psexchange

import com.app.psexchange.model.Balance

class Config {
  companion object{
    const val ACCESS_KEY = "8a87bf0b1ef89765a9dbfe8c4f79c161"
    
    fun defaultBalance(): ArrayList<Balance>{
      val list = ArrayList<Balance>()
      list.add(Balance(currency = "EUR", value = 1000.0, rate = 1.0))
      return list
    }
  }
}