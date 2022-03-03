package com.app.psexchange.view

import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
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
  private lateinit var viewModel: BalanceViewModel
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    viewModel = ViewModelProvider(this)[BalanceViewModel::class.java]
    viewModel.getRates()
    binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
    binding.lifecycleOwner = this
    binding.viewModel = viewModel
    bindView()
  }
  
  private fun bindView() {
    setupSellValue()
    setupSellSpinnerItemChangedListener()
    setupReceiveSpinnerItemChangedListener()
    setupBalanceAdapter()
    setupRatesObservable()
  }
  
  private fun setupRatesObservable() {
    viewModel.rates().observe(this) {
      if (it != null) {
        fillSpinner(
          spinner = binding.receiveExchange.spinner,
          items = it.rates.keys
        )
      }
    }
  }
  
  private fun setupBalanceAdapter() {
    val balanceAdapter = BalanceAdapter()
    binding.recyclerBalances.adapter = balanceAdapter
    viewModel.balances.observe(this) {
      if (it != null) {
        balanceAdapter.submitList(list = it)
        fillSpinner(
          spinner = binding.sellExchange.spinner,
          items = viewModel.toSet(list = it)
        )
      }
    }
  }
  
  private fun setupReceiveSpinnerItemChangedListener() {
    binding.receiveExchange.spinner.onItemSelectedListener =
      object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
          viewModel.setReceiveCurrency(currency = getSelectedReceiveCurrency())
        }
      
        override fun onNothingSelected(parentView: AdapterView<*>?) {}
      }
  }
  
  private fun setupSellSpinnerItemChangedListener() {
    binding.sellExchange.spinner.onItemSelectedListener =
      object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
          viewModel.setBalance(currency = getSelectedBalanceCurrency())
        }
      
        override fun onNothingSelected(parentView: AdapterView<*>?) {}
      }
  }
  
  private fun setupSellValue() {
    binding.sellExchange.etValue.filters = arrayOf<InputFilter>(DecimalDigitsInputFilter())
    binding.sellExchange.etValue.addTextChangedListener(object : TextWatcher {
      override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
    
      override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
    
      override fun afterTextChanged(p0: Editable?) {
        viewModel.setSellValue(value = getInsertedSellValue())
      }
    })
  }
  
  private fun getSelectedBalanceCurrency(): String {
    val spinnerSelection = binding.sellExchange.spinner.selectedItem
    if (spinnerSelection != null)
      return spinnerSelection.toString()
    return ""
  }
  
  private fun getSelectedReceiveCurrency(): String? {
    val spinnerSelection = binding.receiveExchange.spinner.selectedItem
    if (spinnerSelection != null)
      return spinnerSelection.toString()
    return null
  }
  
  private fun getInsertedSellValue(): Double {
    if (binding.sellExchange.etValue.text.toString().isNotEmpty())
      return binding.sellExchange.etValue.text.toString().toDouble()
    return 0.0
  }
  
  private fun fillSpinner(spinner: Spinner, items: Set<String>) {
    val adapter: ArrayAdapter<String> = ArrayAdapter<String>(this, R.layout.spinner_item, items.toTypedArray())
    adapter.setDropDownViewResource(R.layout.spinner_item)
    spinner.adapter = adapter
  }
}