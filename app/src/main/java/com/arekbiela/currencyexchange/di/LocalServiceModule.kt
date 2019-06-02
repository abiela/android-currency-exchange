package com.arekbiela.currencyexchange.di

import android.app.Application
import com.arekbiela.currencyexchange.model.local.RoomUserCurrencySelectionDatabase
import com.arekbiela.currencyexchange.model.local.RoomUserCurrencySelectionLocalService
import com.arekbiela.currencyexchange.model.local.UserCurrencySelectionLocalService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class LocalServiceModule {

    @Singleton
    @Provides
    fun providesCurrencyExchangeRateLocalService(roomUserCurrencySelectionDatabase: RoomUserCurrencySelectionDatabase): UserCurrencySelectionLocalService =
        RoomUserCurrencySelectionLocalService(roomUserCurrencySelectionDatabase)

    @Singleton
    @Provides
    fun providesRoomCurrencyExchangeRatesDatabase(application: Application): RoomUserCurrencySelectionDatabase =
        RoomUserCurrencySelectionDatabase.getInstance(application)
}