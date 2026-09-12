package com.mdhayat.mintfx.data.remote

import retrofit2.http.GET
import retrofit2.http.Path

interface CurrencyApiService {
    @GET("{baseCurrency}")
    suspend fun getLatestRates(
        @Path("baseCurrency") baseCurrency: String = "USD"
    ): ExchangeRateResponse
}
