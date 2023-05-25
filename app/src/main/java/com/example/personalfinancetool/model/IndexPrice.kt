package com.example.personalfinancetool.model

import com.squareup.moshi.Json


data class IndexPrice (
        @Json(name = "5. adjusted close")
        val price : String
        )