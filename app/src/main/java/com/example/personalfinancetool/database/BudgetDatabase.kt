package com.example.personalfinancetool.database

import android.content.Context
import android.graphics.Movie
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.personalfinancetool.model.IncomeExpenseItem

@Database(entities = [IncomeExpenseItem::class], version = 1, exportSchema = false)
abstract class BudgetDatabase : RoomDatabase() {
    abstract val budgetDatabaseDao: BudgetDatabaseDao

    companion object {



        @Volatile
        private var INSTANCE: BudgetDatabase? = null

        fun getInstance(context: Context): BudgetDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        BudgetDatabase::class.java,
                        "saved_movie_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}