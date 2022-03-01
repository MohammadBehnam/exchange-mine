package com.app.psexchange

import com.app.psexchange.network.model.BalanceModel

class Config {
  companion object{
    const val ACCESS_KEY = "8a87bf0b1ef89765a9dbfe8c4f79c161"
    
    fun defaultBalance(): ArrayList<BalanceModel>{
      val list = ArrayList<BalanceModel>()
      list.add(BalanceModel(currency = "EUR", balance = 1000.0))
      return list
    }
  }
}