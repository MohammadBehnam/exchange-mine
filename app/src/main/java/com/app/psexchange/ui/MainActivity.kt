package com.app.psexchange.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.app.psexchange.R
import com.app.psexchange.databinding.ActivityMainBinding
import com.app.psexchange.ui.exchange.ExchangeFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
  private lateinit var binding: ActivityMainBinding
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
    addExchangeFragment()
  }
  
  private fun addExchangeFragment() {
    val fragTr = supportFragmentManager.beginTransaction()
    fragTr.replace(R.id.container, ExchangeFragment(), "ExchangeFragment")
    fragTr.commit()
  }
}