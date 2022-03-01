package com.app.psexchange.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
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
  
  private fun bind(){
    val balanceAdapter = BalanceAdapter()
    binding.recyclerBalances.adapter = balanceAdapter
  
    mainViewModel.balances.observe(this){
      if (it != null)
        balanceAdapter.submitList(it)
    }
    
    latestRatesViewModel.result().observe(this){
      if (it != null)
        Toast.makeText(this, it.rates.size.toString(), Toast.LENGTH_SHORT).show()
    }
  }
  
  private fun callApi(){
    latestRatesViewModel.call()
  }
}