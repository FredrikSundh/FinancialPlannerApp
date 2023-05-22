package com.example.personalfinancetool.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class IncomeExpenseItem (
    val krValue : Int,
    val description: String
        ) : Parcelable
