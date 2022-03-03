package com.app.psexchange.view

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.app.psexchange.model.Balance
import java.text.DecimalFormat

object BindingUtil {
  @JvmStatic
  @BindingAdapter("twoDigitsRoundText")
  fun twoDigitsRoundText(view: TextView, value: Double) {
    if (value == 0.0) {
      view.text = "0.0"
    } else {
      val text = "+" + DecimalFormat("##.##").format(value)
      view.text = text
    }
  }
  
  @JvmStatic
  @BindingAdapter("setBalance")
  fun setBalance(view: TextView, balance: Balance?) {
    val currency = balance?.currency
    val value = getBalanceValueString(balance = balance)
    val text = "$value $currency"
    view.text = text
  }
  
  @JvmStatic
  @BindingAdapter("setBalanceError")
  fun setBalanceError(view: TextView, balance: Balance?) {
    val currency = balance?.currency
    val value = getBalanceValueString(balance = balance)
    val text = "<= $value $currency"
    view.text = text
  }
  
  private fun getBalanceValueString(balance: Balance?): String{
    if (balance != null){
      val arr = balance.value.toString().split(".")
      if (arr.size > 1){
        return if (arr[1].length < 2){
          arr[0] + "." + arr[1] + "0"
        } else {
          balance.value.toString()
        }
      }
    }
    return "0.00"
  }
}