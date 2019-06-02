package com.arekbiela.currencyexchange.di

import android.app.Application
import com.arekbiela.currencyexchange.CurrencyExchangeApplication
import com.arekbiela.currencyexchange.config.CurrencyExchangeApplicationConfiguration
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidInjectionModule::class,
    RemoteServiceModule::class,
    LocalServiceModule::class,
    RepositoryModule::class,
    ActivityModule::class,
    CurrencyExchangeViewModelModule::class,
    CurrencyExchangeModule::class])
interface ApplicationComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance fun withApplicationContext(application: Application): Builder

        @BindsInstance fun withApplicationConfiguration(applicationConfiguration: CurrencyExchangeApplicationConfiguration): Builder

        fun build(): ApplicationComponent
    }

    fun inject(currencyExchangeApplication: CurrencyExchangeApplication)
}