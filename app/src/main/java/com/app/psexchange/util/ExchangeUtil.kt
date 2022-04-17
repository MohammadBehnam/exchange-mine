package com.app.psexchange.util

import com.app.psexchange.model.Balance

object ExchangeUtil {
    fun calculateReceiveValue(sellValue: Double?, sellRate: Double?, receiveRate: Double?): Double {
      if (sellValue != null && sellRate != null && receiveRate != null)
        return (sellValue / sellRate) * receiveRate
      return 0.0
    }
  
    fun isBalanceSufficient(sellValue: Double?, balance: Balance?): Boolean {
      if (sellValue != null) {
        val value = balance?.value
        if (value != null) {
          if (sellValue > value)
            return false
        }
      }
      return true
    }
}