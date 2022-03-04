package com.app.psexchange.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.app.psexchange.R
import com.app.psexchange.databinding.AdapterBalanceBinding
import com.app.psexchange.model.Balance

class BalanceAdapter : ListAdapter<Balance, BalanceAdapter.ItemHolder>(DIFF_CALLBACK) {
  private var listener: OnItemClickListener? = null

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
    val binding: AdapterBalanceBinding = DataBindingUtil.inflate(
      LayoutInflater.from(parent.context),
      R.layout.adapter_balance,
      parent,
      false)
    return ItemHolder(binding)
  }

  override fun submitList(list: List<Balance>?) {
    super.submitList(if (list != null) ArrayList(list) else null)
  }

  override fun onBindViewHolder(holder: ItemHolder, position: Int) {
    holder.binding.item = getItem(position)
    holder.binding.executePendingBindings()
  }

  fun setOnItemClickListener(listener: OnItemClickListener) {
    this.listener = listener
  }

  interface OnItemClickListener {
    fun onCopy(item: Balance, position: Int)
  }

  inner class ItemHolder(val binding: AdapterBalanceBinding) : RecyclerView.ViewHolder(binding.root) {
    init {
      itemView.setOnClickListener {
        val position = adapterPosition
        if (listener != null && position != RecyclerView.NO_POSITION) {
          val item = getItem(position)
          listener!!.onCopy(item, position)
        }
      }
    }
  }

  companion object {
    private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Balance>() {
      override fun areItemsTheSame(oldItem: Balance, newItem: Balance): Boolean {
        return oldItem.currency == newItem.currency
      }

      override fun areContentsTheSame(oldItem: Balance, newItem: Balance): Boolean {
        return oldItem.value == newItem.value
      }
    }
  }
}
