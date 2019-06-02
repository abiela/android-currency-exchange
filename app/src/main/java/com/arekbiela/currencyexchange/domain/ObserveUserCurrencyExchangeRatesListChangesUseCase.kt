package com.arekbiela.currencyexchange.domain

import com.arekbiela.currencyexchange.config.CurrencyExchangeApplicationConfiguration
import com.arekbiela.currencyexchange.model.model.CurrencyExchangeRatesModel
import com.arekbiela.currencyexchange.model.model.UserCurrencySelectionModel
import com.arekbiela.currencyexchange.model.repository.CurrencyExchangeRatesRepository
import com.arekbiela.currencyexchange.presentation.model.ExchangeRateViewItemModel
import io.reactivex.Flowable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import java.math.BigDecimal
import java.math.MathContext

/**
 * Observes currency user currency exchange list based on combining two streams:
 * -> Getting currency exchange rates from repository
 * -> Getting user currency selections
 *
 */
class ObserveUserCurrencyExchangeRatesListChangesUseCase(private val repository: CurrencyExchangeRatesRepository,
                                                         private val configuration: CurrencyExchangeApplicationConfiguration,
                                                         private val mathContext: MathContext) {

    fun run(): Flowable<List<ExchangeRateViewItemModel>> =
        Flowable.combineLatest(
                repository.observeCurrencyExchangeRates(configuration.startBaseCurrency),
                repository.observeUserCurrencySelection(),
                BiFunction<CurrencyExchangeRatesModel, UserCurrencySelectionModel, List<ExchangeRateViewItemModel>>
                { currencyExchangeRatesModel, userCurrencySelectionModel ->
                    currencyExchangeRatesModel
                        .mapToExchangeRateViewItemList(userCurrencySelectionModel)
                        .andPlaceBaseCurrencyAtTheTop(userCurrencySelectionModel)})
            .subscribeOn(Schedulers.computation())

    
    private fun CurrencyExchangeRatesModel.mapToExchangeRateViewItemList(userCurrencySelectionModel: UserCurrencySelectionModel): List<ExchangeRateViewItemModel> =
        ratesMap
            .asSequence()
            .map { ExchangeRateViewItemModel(it.key, calculateCurrencyAmount(this, userCurrencySelectionModel, it.value)) }
            .plus(ExchangeRateViewItemModel(configuration.startBaseCurrency, calculateCurrencyAmount(this, userCurrencySelectionModel, BigDecimal("1.0"))))
            .sortedBy { it.currency.currencyCode }
            .toList()

    private fun List<ExchangeRateViewItemModel>.andPlaceBaseCurrencyAtTheTop(userCurrencySelectionModel: UserCurrencySelectionModel): List<ExchangeRateViewItemModel> =
        sortedByDescending { it.currency == userCurrencySelectionModel.baseCurrency }

    private fun calculateCurrencyAmount(currencyExchangeRatesModel: CurrencyExchangeRatesModel,
                                        userCurrencySelectionModel: UserCurrencySelectionModel,
                                        destinationCurrencyRate: BigDecimal): BigDecimal =

        when(userCurrencySelectionModel.baseCurrency == configuration.startBaseCurrency) {
            true -> userCurrencySelectionModel.currencyAmount.multiply(destinationCurrencyRate)
            false -> userCurrencySelectionModel.currencyAmount
                .divide(currencyExchangeRatesModel.ratesMap[userCurrencySelectionModel.baseCurrency], mathContext)
                .multiply(destinationCurrencyRate, mathContext)
        }
}