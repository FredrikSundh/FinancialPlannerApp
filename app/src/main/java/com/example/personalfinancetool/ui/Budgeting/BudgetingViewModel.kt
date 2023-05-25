package com.example.personalfinancetool.ui.Budgeting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.personalfinancetool.database.BudgetDatabaseDao
import com.example.personalfinancetool.model.IncomeExpenseItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BudgetingViewModel : ViewModel() {
    val BudgetingItemList: MutableLiveData<MutableList<IncomeExpenseItem>> = MutableLiveData(mutableListOf())
    var disposableIncome : MutableLiveData<Int> = MutableLiveData()
    var calcResult : String = ""
    lateinit var dao : BudgetDatabaseDao
    fun AddNewItem(amount: Int, description: String) { // Takes data from dialog and updates LiveData
        val currentList = BudgetingItemList.value!!.toMutableList()
        val newItem = IncomeExpenseItem(amount, description)
        currentList.add(newItem)
        viewModelScope.launch {
            dao.insert(newItem)
        }
        BudgetingItemList.value = currentList
    }

    fun updateFromDatabase() {
            viewModelScope.launch { withContext(Dispatchers.IO){
                BudgetingItemList.postValue(dao.getItems().toMutableList())
            }
        }
    }


    fun RemoveItem(item: IncomeExpenseItem) {
        val currentList = BudgetingItemList.value?.toMutableList()
        currentList?.remove(item)
        viewModelScope.launch {
            dao.delete(item)
        }
        BudgetingItemList.value = currentList ?: mutableListOf()
    }


    fun onRemoveClicked(item: IncomeExpenseItem) {
        RemoveItem(item)
    }



}