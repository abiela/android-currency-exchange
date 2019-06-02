package com.arekbiela.currencyexchange.model.remote

import com.arekbiela.currencyexchange.model.model.CurrencyExchangeRatesModel
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import java.math.BigDecimal
import java.util.*

class RevolutCurrencyExchangeRatesRemoteService(private val revolutCurrencyExchangeRatesApi: RevolutCurrencyExchangeRatesAPI) : CurrencyExchangeRatesRemoteService {

    override fun getExchangeRatesForBase(baseCurrency: Currency): Single<CurrencyExchangeRatesModel> =
            revolutCurrencyExchangeRatesApi
                .getCurrencyRatesForBase(baseCurrency)
                .map {
                    CurrencyExchangeRatesModel(Currency.getInstance(it.baseCurrency),
                    it.ratesMap.map { ratesMap -> Currency.getInstance(ratesMap.key) to BigDecimal(ratesMap.value) }.toMap()) }
                .subscribeOn(Schedulers.io())
}