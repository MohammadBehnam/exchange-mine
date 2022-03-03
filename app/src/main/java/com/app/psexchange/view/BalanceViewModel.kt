package com.app.psexchange.view

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.psexchange.Config
import com.app.psexchange.network.model.BalanceModel
import com.app.psexchange.network.model.ExchangeRateResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.DecimalFormat
import javax.inject.Inject

@HiltViewModel
class BalanceViewModel @Inject constructor(application: Application, private val repository: RatesRepository) : AndroidViewModel(application) {
  val hasError: MutableLiveData<Boolean> = MutableLiveData()
  val balances: MutableLiveData<ArrayList<BalanceModel>> = MutableLiveData()
  val receiveValueRound: MutableLiveData<Double> = MutableLiveData()
  private var receiveValue: Double = 0.0
  private var selectedBalanceCurrency: String = ""
  private var selectedSellValue: Double = 0.0
  private var selectedSellRate: Double = 0.0
  
  init {
    balances.postValue(Config.defaultBalance())
    receiveValueRound.postValue(0.0)
  }
  
  fun rates(): LiveData<ExchangeRateResponse> {
    return repository.result
  }
  
  fun getRates() {
    repository.call()
  }
  
  fun setBalance(currency: String){
    selectedBalanceCurrency = currency
    calculateReceiveValue()
  }

  fun setSellValue(value: Double){
    selectedSellValue = value
    calculateReceiveValue()
  }
  
  fun setReceiveCurrency(currency: String?){
    selectedSellRate = getSellRate(currency = currency)
    calculateReceiveValue()
  }
  
  fun toSet(list: ArrayList<BalanceModel>?) : Set<String>{
    val result = HashSet<String>()
    if (list != null){
      for (i in 0 until list.size)
        result.add(list[i].currency)
    }
    return result
  }
  
  private fun getSellRate(currency: String?): Double {
    var rate = 0.0
    if (rates().value != null){
      val tmpRate = rates().value?.rates?.get(currency)
      if (tmpRate != null) {
        rate = tmpRate
      }
    }
    return rate
  }
 
  private fun getBalance(currency: String?): BalanceModel? {
    val tmpBalances = balances.value
    if (tmpBalances != null) {
      for (i in 0 until tmpBalances.size) {
        if (tmpBalances[i].currency == currency)
          return tmpBalances[i]
      }
    }
    return null
  }
  
  private fun calculateReceiveValue() {
    val balance = getBalance(currency = selectedBalanceCurrency)
    if (balance != null){
      receiveValue = (selectedSellValue / balance.rate) * selectedSellRate
      receiveValueRound.postValue(DecimalFormat("##.##").format(receiveValue).toDouble())
      if (selectedSellValue > balance.balance){
        hasError.postValue(true)
      } else {
        hasError.postValue(false)
      }
    } else {
      hasError.postValue(false)
    }
  }
}