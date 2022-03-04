package com.app.psexchange.network.repository

import androidx.lifecycle.MutableLiveData
import com.app.psexchange.Config
import com.app.psexchange.network.Api
import com.app.psexchange.network.system.RequestStatus
import com.app.psexchange.network.response.ExchangeRateResponse
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class RatesRepository @Inject constructor(private val api: Api) {
  var result: MutableLiveData<ExchangeRateResponse> = MutableLiveData()
  var status: MutableLiveData<Int> = MutableLiveData()
  
  fun call() {
    CoroutineScope(Dispatchers.IO + CoroutineExceptionHandler { _, _ ->
      status.postValue(RequestStatus.CALL_FAILURE.get())
    }).launch {
      status.postValue(RequestStatus.LOADING.get())
      val response = api.latest(accessKey = Config.ACCESS_KEY)
      if (response != null) {
        status.postValue(RequestStatus.CALL_SUCCESS.get())
        result.postValue(response)
      } else {
        status.postValue(RequestStatus.CALL_FAILURE.get())
      }
    }
  }
  
  fun getRate(currency: String?): Double {
    var rate = 0.0
    if (result.value != null){
      val tmpRate = result.value?.rates?.get(currency)
      if (tmpRate != null) {
        rate = tmpRate
      }
    }
    return rate
  }
}
