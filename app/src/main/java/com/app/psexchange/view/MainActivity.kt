package com.app.psexchange.view

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.app.psexchange.R
import com.app.psexchange.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
  private lateinit var binding: ActivityMainBinding
  private lateinit var mainViewModel: MainViewModel
  private lateinit var latestRatesViewModel: LatestRatesViewModel
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
    mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
    latestRatesViewModel = ViewModelProvider(this)[LatestRatesViewModel::class.java]
    callApi()
    bind()
  }
  
  private fun bind() {
    val balanceAdapter = BalanceAdapter()
    binding.recyclerBalances.adapter = balanceAdapter
    
    mainViewModel.balances.observe(this) {
      if (it != null) {
        balanceAdapter.submitList(list = it)
        fillSpinner(
          spinner = binding.sellExchange.spinner,
          items = mainViewModel.convertToSet(list = it)
        )
      }
    }
    
    latestRatesViewModel.result().observe(this) {
      if (it != null) {
        fillSpinner(
          spinner = binding.receiveExchange.spinner,
          items = it.rates.keys
        )
      }
    }
  }
  
  private fun fillSpinner(spinner: Spinner, items: Set<String>) {
    val adapter: ArrayAdapter<String> =
      ArrayAdapter<String>(this, R.layout.spinner_item, items.toTypedArray())
    adapter.setDropDownViewResource(R.layout.spinner_item)
    spinner.adapter = adapter
  }
  
  private fun callApi() {
    latestRatesViewModel.call()
  }
}