package com.mdhayat.mintfx.data.repository

import com.mdhayat.mintfx.data.local.CurrencyPreferences
import com.mdhayat.mintfx.data.remote.CurrencyApiService

class CurrencyRepository(
    private val api: CurrencyApiService,
    private val cache: CurrencyPreferences
) {
    suspend fun getRates(): Result<RateSnapshot> {
        return try {
            val response = api.getLatestRates()
            val rates = buildMap {
                put(response.base_code, 1.0)
                putAll(response.rates)
            }
            cache.saveRates(rates)
            Result.success(RateSnapshot(rates, isCached = false))
        } catch (networkError: Exception) {
            val cached = cache.readRates()
            if (cached.isEmpty()) {
                Result.failure(networkError)
            } else {
                Result.success(
                    RateSnapshot(
                        rates = cached,
                        isCached = true
                    )
                )
            }
        }
    }
}

data class RateSnapshot(
    val rates: Map<String, Double>,
    val isCached: Boolean
)
