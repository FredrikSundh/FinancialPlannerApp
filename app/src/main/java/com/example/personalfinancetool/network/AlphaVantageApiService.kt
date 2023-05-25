package com.example.personalfinancetool.network

import com.example.personalfinancetool.secret.ApiKey
import com.example.personalfinancetool.utils.Constants
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()


fun getLoggerIntercepter(): HttpLoggingInterceptor {
    val logging = HttpLoggingInterceptor()
    logging.level = HttpLoggingInterceptor.Level.BODY
    return logging
}


private val AlphaVantageRetrofit = Retrofit.Builder()
    .client(
        OkHttpClient.Builder()
            .addInterceptor(getLoggerIntercepter())
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .build()
    )
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(Constants.vantageBaseUrl)
    .build()


interface AlphaVantageApiService {
    @GET("query")
    suspend fun getIndexData(
        @Query("function") function: String = "TIME_SERIES_MONTHLY_ADJUSTED",
        @Query("symbol") symbol : String,
        @Query("apikey") apiKey: String = ApiKey.API_KEY
    ) : AlphaVantageResponse

}

object AlphaVantageAPI {
    val AlphaVantageRetrofitService : AlphaVantageApiService by lazy { AlphaVantageRetrofit.create(AlphaVantageApiService::class.java)}
}