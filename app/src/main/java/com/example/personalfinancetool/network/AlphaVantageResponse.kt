package com.example.personalfinancetool.network

import com.example.personalfinancetool.model.IndexPrice
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class AlphaVantageResponse {
    @Json(name ="Monthly Adjusted Time Series")
    var priceList : Map<String,IndexPrice> = mapOf()
}