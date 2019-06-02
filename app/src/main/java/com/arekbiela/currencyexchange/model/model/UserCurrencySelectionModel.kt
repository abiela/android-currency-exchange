package com.arekbiela.currencyexchange.model.model

import java.math.BigDecimal
import java.util.*

data class UserCurrencySelectionModel(val baseCurrency: Currency,
                                      val currencyAmount: BigDecimal)