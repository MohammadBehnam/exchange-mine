package com.app.psexchange.view

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.psexchange.Config
import com.app.psexchange.network.model.BalanceModel
import com.app.psexchange.network.model.ExchangeRateResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LatestRatesViewModel @Inject constructor(application: Application, private val repository: LatestRatesRepository) : AndroidViewModel(application) {
  fun status(): LiveData<Int> {
    return repository.status
  }
  
  fun result(): LiveData<ExchangeRateResponse> {
    return repository.result
  }
  
  fun call() {
    repository.call()
  }
}
