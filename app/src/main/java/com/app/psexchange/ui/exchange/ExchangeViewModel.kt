package com.app.psexchange.ui.exchange

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.app.psexchange.Config
import com.app.psexchange.model.Balance
import com.app.psexchange.model.Exchange
import com.app.psexchange.network.repository.RatesRepository
import com.app.psexchange.util.ExchangeUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ExchangeViewModel @Inject constructor(application: Application, val ratesRepository: RatesRepository) : AndroidViewModel(application) {
  val exchangeValid: MutableLiveData<Boolean> = MutableLiveData()
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
    val balance = getBalance(currency = currency)
    if (balance != null) {
      selectedBalance.postValue(balance)
      exchange.value?.sell?.currency = currency
      exchange.value?.sell?.rate = balance.rate
      update()
    }
  }
  
  fun setSellValue(value: Double) {
    exchange.value?.sell?.value = value
    update()
  }
  
  fun setReceiveCurrency(currency: String) {
    exchange.value?.receive?.currency = currency
    exchange.value?.receive?.rate = ratesRepository.getRate(currency)
    update()
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
  
  private fun update() {
    updateReceiveValue()
    updateValidation()
  }
  
  private fun updateValidation() {
    exchangeValid.postValue(
      ExchangeUtil.isBalanceSufficient(
        sellValue = exchange.value?.sell?.value,
        balance = getBalance(currency = exchange.value?.sell?.currency)
      )
    )
  }
  
  private fun updateReceiveValue() {
    exchange.value?.receive?.value = ExchangeUtil.calculateReceiveValue(
      sellValue = exchange.value?.sell?.value,
      sellRate = exchange.value?.sell?.rate,
      receiveRate = exchange.value?.receive?.rate,
    )
    exchange.postValue(exchange.value)
  }
}