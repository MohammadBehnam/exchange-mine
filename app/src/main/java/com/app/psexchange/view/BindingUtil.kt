package com.app.psexchange.view

import android.widget.TextView
import androidx.databinding.BindingAdapter
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
}