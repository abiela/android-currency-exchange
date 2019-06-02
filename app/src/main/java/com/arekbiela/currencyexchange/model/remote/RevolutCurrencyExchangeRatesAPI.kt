package com.arekbiela.currencyexchange.model.remote

import com.arekbiela.currencyexchange.model.model.CurrencyExchangeRatesResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.*

interface RevolutCurrencyExchangeRatesAPI {

    @GET("latest")
    fun getCurrencyRatesForBase(@Query("base") baseCurrency: Currency): Single<CurrencyExchangeRatesResponse>
}