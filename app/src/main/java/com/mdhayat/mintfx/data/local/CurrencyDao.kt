package com.mdhayat.mintfx.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CurrencyDao {
    @Query("SELECT * FROM currency_rates WHERE baseCurrency = :baseCurrency")
    suspend fun getRatesForBase(baseCurrency: String): List<CurrencyEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRates(rates: List<CurrencyEntity>)

    @Query("DELETE FROM currency_rates WHERE baseCurrency = :baseCurrency")
    suspend fun deleteRatesForBase(baseCurrency: String)
}
