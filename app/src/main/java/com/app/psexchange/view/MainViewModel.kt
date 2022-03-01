package com.app.psexchange.view

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.psexchange.Config
import com.app.psexchange.network.model.BalanceModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(application: Application) : AndroidViewModel(application) {
  val balances: MutableLiveData<ArrayList<BalanceModel>> = MutableLiveData()
  
  init {
    balances.postValue(Config.defaultBalance())
  }
}
