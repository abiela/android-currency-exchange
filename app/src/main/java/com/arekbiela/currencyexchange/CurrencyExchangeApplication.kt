package com.arekbiela.currencyexchange

import android.app.Activity
import android.app.Application
import com.arekbiela.currencyexchange.config.CurrencyExchangeApplicationConfiguration
import com.arekbiela.currencyexchange.di.DaggerApplicationComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class CurrencyExchangeApplication : Application(), HasActivityInjector {

    @Inject
    lateinit var activityInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()
        DaggerApplicationComponent.builder()
            .withApplicationContext(this)
            .withApplicationConfiguration(CurrencyExchangeApplicationConfiguration())
            .build()
            .inject(this)
    }

    override fun activityInjector(): AndroidInjector<Activity> = activityInjector
}