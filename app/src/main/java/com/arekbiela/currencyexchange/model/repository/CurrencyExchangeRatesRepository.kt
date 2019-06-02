package com.arekbiela.currencyexchange.model.repository

import com.arekbiela.currencyexchange.model.model.CurrencyExchangeRatesModel
import com.arekbiela.currencyexchange.model.model.UserCurrencySelectionModel
import io.reactivex.Completable
import io.reactivex.Flowable
import java.math.BigDecimal
import java.util.*

interface CurrencyExchangeRatesRepository {

    fun observeCurrencyExchangeRates(baseCurrency: Currency): Flowable<CurrencyExchangeRatesModel>

    fun observeUserCurrencySelection(): Flowable<UserCurrencySelectionModel>

    fun updateUserBaseCurrencySelection(newBaseCurrency: Currency): Completable

    fun updateUserCurrencyAmountSelection(newCurrencyAmount: BigDecimal): Completable
}