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
  val balances: MutableLiveData<HashMap<String, Balance>> = MutableLiveData()
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
  
  private fun getBalance(currency: String?): Balance? {
    return balances.value?.get(currency)
  }
  
  private fun update() {
    updateReceiveValue()
    updateValidation()
  }
  
  private fun updateValidation() {
    exchangeValid.postValue(
      ExchangeUtil.isBalanceSufficient(
        sellValue = exchange.value?.sell?.value,
        balance = getBalance(
          currency = exchange.value?.sell?.currency
        )
      ) && exchange.value?.sell?.currency != exchange.value?.receive?.currency
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
  
  fun confirmExchange(): Exchange? {
    val exchange = exchange.value
    if (exchange != null) {
      val receive = Balance()
      receive.currency = exchange.receive.currency
      receive.rate = exchange.receive.rate
      if (getBalance(exchange.receive.currency) != null){
        receive.value = exchange.receive.value + getBalance(exchange.receive.currency)!!.value
      } else {
        receive.value = exchange.receive.value
      }
      balances.value?.put(receive.currency, receive)

      val sell = Balance()
      sell.currency = exchange.sell.currency
      sell.rate = exchange.sell.rate
      if (getBalance(exchange.sell.currency) != null){
        sell.value = getBalance(exchange.sell.currency)!!.value - exchange.sell.value
      } else {
        sell.value = 0.0
      }

      this.exchange.postValue(exchange)

      balances.value?.put(sell.currency, sell)
      balances.postValue(getFilteredBalances())
    }
    return exchange
  }

  private fun getFilteredBalances(): HashMap<String, Balance>? {
    val balances = balances.value
    if (balances != null){
      val keys = ArrayList(balances.keys)
      for (i in 0 until keys.size){
        if (balances[keys[i]]?.value == 0.0){
          balances.remove(keys[i])
        }
      }
    }
    return balances
  }
}