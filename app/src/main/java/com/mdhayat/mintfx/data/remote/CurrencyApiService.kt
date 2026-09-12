package com.mdhayat.mintfx.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyApiService {
    @GET("v1/latest")
    suspend fun getLatestRates(
        @Query("base") baseCurrency: String
    ): FrankfurterRatesResponse
}
