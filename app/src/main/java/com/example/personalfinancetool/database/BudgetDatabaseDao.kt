package com.example.personalfinancetool.database

import android.graphics.Movie
import androidx.room.*
import com.example.personalfinancetool.model.IncomeExpenseItem

@Dao
interface BudgetDatabaseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(incomeExpense: IncomeExpenseItem)


    @Delete
    suspend fun delete(incomeExpense: IncomeExpenseItem)

    @Query("SELECT * from budgetList")
    suspend fun getItems(): List<IncomeExpenseItem>
}