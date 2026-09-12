package com.mdhayat.mintfx.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyApiService {
    @GET("latest")
    suspend fun getLatestRates(
        @Query("from") baseCurrency: String = "USD"
    ): FrankfurterRatesResponse
}
