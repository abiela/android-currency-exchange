package com.arekbiela.currencyexchange.model.remote

import com.arekbiela.currencyexchange.model.model.CurrencyExchangeRatesModel
import io.reactivex.Single
import java.util.*

interface CurrencyExchangeRatesRemoteService {

    fun getExchangeRatesForBase(baseCurrency: Currency): Single<CurrencyExchangeRatesModel>
}