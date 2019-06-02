package com.arekbiela.currencyexchange.config

import com.arekbiela.currencyexchange.BuildConfig
import java.math.RoundingMode
import java.util.*
import java.util.concurrent.TimeUnit

data class CurrencyExchangeApplicationConfiguration (val currencyExchangeRatesApiEndpoint: String = BuildConfig.API_ENDPOINT,
                                                     val connectionTimeoutValue: Long = 5,
                                                     val connectionTimeoutTimeUnit: TimeUnit = TimeUnit.SECONDS,
                                                     val readTimeoutValue: Long = 5,
                                                     val readTimeoutTimeUnit: TimeUnit = TimeUnit.SECONDS,
                                                     val currencyExchangePollingIntervalValue: Long = 1,
                                                     val currencyExchangePollingIntervalTimeUnit: TimeUnit = TimeUnit.SECONDS,
                                                     val startBaseCurrency: Currency = Currency.getInstance("EUR"),
                                                     val mathCalculationPrecision: Int = 5,
                                                     val mathCalculationRoundingMode: RoundingMode = RoundingMode.HALF_EVEN)