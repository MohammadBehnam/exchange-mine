package com.app.psexchange.view

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.app.psexchange.Config
import com.app.psexchange.model.Balance
import com.app.psexchange.model.Exchange
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BalanceViewModel @Inject constructor(application: Application, val ratesRepository: RatesRepository) : AndroidViewModel(application) {
  val hasError: MutableLiveData<Boolean> = MutableLiveData()
  val balances: MutableLiveData<ArrayList<Balance>> = MutableLiveData()
  val selectedBalance: MutableLiveData<Balance> = MutableLiveData()
  val exchange: MutableLiveData<Exchange> = MutableLiveData()
  
  init {
    balances.postValue(Config.defaultBalance())
    exchange.postValue(
      Exchange(
        sell = Balance(),
        receive = Balance()
      )
    )
  }
  
  fun fetchRates() {
    ratesRepository.call()
  }
  
  fun setSellCurrency(currency: String) {
    selectedBalance.postValue(getBalance(currency))
    exchange.value?.sell?.currency = currency
    exchange.value?.sell?.rate = ratesRepository.getRate(currency)
    calculate()
  }
  
  fun setSellValue(value: Double) {
    exchange.value?.sell?.value = value
    calculate()
  }
  
  fun setReceiveCurrency(currency: String) {
    exchange.value?.receive?.currency = currency
    exchange.value?.receive?.rate = ratesRepository.getRate(currency)
    calculate()
  }
  
  fun toSet(list: ArrayList<Balance>?): Set<String> {
    val result = HashSet<String>()
    if (list != null) {
      for (i in 0 until list.size)
        result.add(list[i].currency)
    }
    return result
  }
  
  private fun getBalance(currency: String?): Balance? {
    val tmpBalances = balances.value
    if (tmpBalances != null) {
      for (i in 0 until tmpBalances.size) {
        if (tmpBalances[i].currency == currency)
          return tmpBalances[i]
      }
    }
    return null
  }
  
  private fun calculate() {
    hasError.postValue(false)
    
    val sellValue = exchange.value?.sell?.value
    val sellRate = exchange.value?.sell?.rate
    val receiveRate = exchange.value?.receive?.rate
    
    if (sellValue != null && sellRate != null && receiveRate != null) {
      val receiveValue = (sellValue / sellRate) * receiveRate
      exchange.value?.receive?.value = receiveValue
      
      val balance = getBalance(currency = exchange.value?.sell?.currency)
      if (balance != null) {
        if (sellValue > balance.value)
          hasError.postValue(true)
      }
    }
    
    exchange.postValue(exchange.value)
  }
}