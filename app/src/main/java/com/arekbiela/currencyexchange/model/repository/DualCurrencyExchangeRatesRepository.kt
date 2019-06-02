package com.arekbiela.currencyexchange.model.repository

import com.arekbiela.currencyexchange.config.CurrencyExchangeApplicationConfiguration
import com.arekbiela.currencyexchange.model.local.UserCurrencySelectionLocalService
import com.arekbiela.currencyexchange.model.model.CurrencyExchangeRatesModel
import com.arekbiela.currencyexchange.model.model.UserCurrencySelectionModel
import com.arekbiela.currencyexchange.model.remote.CurrencyExchangeRatesRemoteService
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import java.math.BigDecimal
import java.util.*

class DualCurrencyExchangeRatesRepository(private val localUserCurrencySelectionService: UserCurrencySelectionLocalService,
                                          private val remoteCurrencyExchangeRatesService: CurrencyExchangeRatesRemoteService,
                                          private val currencyExchangeApplicationConfiguration: CurrencyExchangeApplicationConfiguration
) : CurrencyExchangeRatesRepository {

    override fun observeCurrencyExchangeRates(baseCurrency: Currency): Flowable<CurrencyExchangeRatesModel> =
            getRemoteCurrencyExchangeRates(baseCurrency)
                .repeatWhen {
                    it.delay(currencyExchangeApplicationConfiguration.currencyExchangePollingIntervalValue,
                        currencyExchangeApplicationConfiguration.currencyExchangePollingIntervalTimeUnit) }
                .retry(1)
                .subscribeOn(Schedulers.computation())

    override fun observeUserCurrencySelection(): Flowable<UserCurrencySelectionModel> =
        localUserCurrencySelectionService
            .observeUserCurrencySelections()
            .subscribeOn(Schedulers.computation())

    override fun updateUserBaseCurrencySelection(newBaseCurrency: Currency): Completable =
        localUserCurrencySelectionService
            .updateUserBaseCurrencySelection(newBaseCurrency)
            .subscribeOn(Schedulers.computation())

    override fun updateUserCurrencyAmountSelection(newCurrencyAmount: BigDecimal): Completable =
        localUserCurrencySelectionService
            .updateUserCurrencyAmountSelection(newCurrencyAmount)
            .subscribeOn(Schedulers.computation())

    private fun getRemoteCurrencyExchangeRates(baseCurrency: Currency): Single<CurrencyExchangeRatesModel> =
            remoteCurrencyExchangeRatesService
                .getExchangeRatesForBase(baseCurrency)
}