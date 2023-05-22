package com.example.personalfinancetool.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.personalfinancetool.databinding.ListItemLayoutBinding
import com.example.personalfinancetool.model.IncomeExpenseItem

class budgetViewAdapter(val listItems : MutableList<IncomeExpenseItem>, val clickListener: RemoveClickListener) : RecyclerView.Adapter<budgetViewAdapter.ViewHolder>() {
    val adapterData = listItems


public class ViewHolder(private var binding: ListItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {


    fun bidnings(incexp : IncomeExpenseItem, clickListener: RemoveClickListener) {
        if (incexp.krValue < 0) { // Sets the type of income expense item
            binding.itemType.text = "Expense :"
        }
        else {
            binding.itemType.text = "Income   :"
        }
        binding.clickListener = clickListener
        binding.incomeExpense = incexp
        binding.ItemDescription.text = incexp.description
        binding.krAmount.text = incexp.krValue.toString() + " kr" // Sets the amount of kr to display
    }


}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): budgetViewAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListItemLayoutBinding.inflate(layoutInflater, parent, false)
        return budgetViewAdapter.ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return adapterData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bidnings(adapterData.get(position), clickListener) // Gets the corresponding income expense Item from the list
    }
}

class RemoveClickListener(private val clickListener: (IncomeExpenseItem) -> Unit) {
    fun onItemClick(item: IncomeExpenseItem) {
        clickListener(item)
    }
}