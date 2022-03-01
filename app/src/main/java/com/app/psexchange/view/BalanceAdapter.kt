package com.app.psexchange.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.app.psexchange.R
import com.app.psexchange.databinding.AdapterBalanceBinding
import com.app.psexchange.network.model.BalanceModel

class BalanceAdapter : ListAdapter<BalanceModel, BalanceAdapter.ItemHolder>(DIFF_CALLBACK) {
  private var listener: OnItemClickListener? = null

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
    val binding: AdapterBalanceBinding = DataBindingUtil.inflate(
      LayoutInflater.from(parent.context),
      R.layout.adapter_balance,
      parent,
      false)
    return ItemHolder(binding)
  }

  override fun submitList(list: List<BalanceModel>?) {
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
    fun onCopy(item: BalanceModel, position: Int)
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
    private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<BalanceModel>() {
      override fun areItemsTheSame(oldItem: BalanceModel, newItem: BalanceModel): Boolean {
        return oldItem.currency == newItem.currency
      }

      override fun areContentsTheSame(oldItem: BalanceModel, newItem: BalanceModel): Boolean {
        return oldItem.balance == newItem.balance
      }
    }
  }
}
