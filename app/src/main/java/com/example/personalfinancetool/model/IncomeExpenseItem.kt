package com.example.personalfinancetool.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "budgetList")
data class IncomeExpenseItem (
    @ColumnInfo
    val krValue : Int,
    @PrimaryKey
    val description: String
        ) : Parcelable
