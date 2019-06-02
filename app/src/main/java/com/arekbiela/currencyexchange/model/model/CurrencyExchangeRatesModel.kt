package com.arekbiela.currencyexchange.model.model

import java.math.BigDecimal
import java.util.*

data class CurrencyExchangeRatesModel(val baseCurrency: Currency,
                                      val ratesMap: Map<Currency, BigDecimal>)