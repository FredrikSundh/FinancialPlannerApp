package com.example.personalfinancetool.ui.Budgeting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.personalfinancetool.model.IncomeExpenseItem

class BudgetingViewModel : ViewModel() {
    val BudgetingItemList: MutableLiveData<MutableList<IncomeExpenseItem>> = MutableLiveData(mutableListOf())

    fun AddNewItem(amount: Int, description: String) { // Takes data from dialog and updates LiveData
        val currentList = BudgetingItemList.value!!.toMutableList()
        val newItem = IncomeExpenseItem(amount, description)
        currentList.add(newItem)
        BudgetingItemList.value = currentList
    }


    fun RemoveItem(item: IncomeExpenseItem) {
        val currentList = BudgetingItemList.value?.toMutableList()
        currentList?.remove(item)
        BudgetingItemList.value = currentList ?: mutableListOf()
    }


    fun onRemoveClicked(item: IncomeExpenseItem) {
        RemoveItem(item)
    }



}