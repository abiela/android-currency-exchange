package com.arekbiela.currencyexchange.di

import com.arekbiela.currencyexchange.config.CurrencyExchangeApplicationConfiguration
import com.arekbiela.currencyexchange.domain.ObserveUserCurrencyExchangeRatesListChangesUseCase
import com.arekbiela.currencyexchange.domain.UpdateBaseCurrencyUseCase
import com.arekbiela.currencyexchange.domain.UpdateCurrencyAmountUseCase
import com.arekbiela.currencyexchange.model.repository.CurrencyExchangeRatesRepository
import dagger.Module
import dagger.Provides
import java.math.MathContext
import javax.inject.Singleton

@Module
class CurrencyExchangeModule {

    @Singleton
    @Provides
    fun providesObserveUserCurrencyExchangeRatesListUseCase(
        repository: CurrencyExchangeRatesRepository,
        configuration: CurrencyExchangeApplicationConfiguration,
        mathContext: MathContext): ObserveUserCurrencyExchangeRatesListChangesUseCase
            = ObserveUserCurrencyExchangeRatesListChangesUseCase(repository, configuration, mathContext)

    @Singleton
    @Provides
    fun providesChangeCurrencyAmountUseCase(repository: CurrencyExchangeRatesRepository): UpdateCurrencyAmountUseCase
            = UpdateCurrencyAmountUseCase(repository)

    @Singleton
    @Provides
    fun providesChangeBaseCurrencyUseCase(repository: CurrencyExchangeRatesRepository): UpdateBaseCurrencyUseCase
            = UpdateBaseCurrencyUseCase(repository)

    @Singleton
    @Provides
    fun providesMathContext(configuration: CurrencyExchangeApplicationConfiguration): MathContext
            = MathContext(configuration.mathCalculationPrecision, configuration.mathCalculationRoundingMode)
}