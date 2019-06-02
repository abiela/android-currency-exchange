package com.arekbiela.currencyexchange.di

import com.arekbiela.currencyexchange.config.CurrencyExchangeApplicationConfiguration
import com.arekbiela.currencyexchange.model.local.UserCurrencySelectionLocalService
import com.arekbiela.currencyexchange.model.remote.CurrencyExchangeRatesRemoteService
import com.arekbiela.currencyexchange.model.repository.CurrencyExchangeRatesRepository
import com.arekbiela.currencyexchange.model.repository.DualCurrencyExchangeRatesRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Singleton
    @Provides
    fun providesCurrencyExchangeRatesRepository(localServiceUser: UserCurrencySelectionLocalService,
                                                remoteService: CurrencyExchangeRatesRemoteService,
                                                applicationConfiguration: CurrencyExchangeApplicationConfiguration
    ): CurrencyExchangeRatesRepository =
        DualCurrencyExchangeRatesRepository(localServiceUser,remoteService, applicationConfiguration)
}