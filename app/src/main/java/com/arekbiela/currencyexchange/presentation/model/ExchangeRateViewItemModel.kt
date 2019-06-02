package com.arekbiela.currencyexchange.presentation.model

import java.math.BigDecimal
import java.util.*

data class ExchangeRateViewItemModel(val currency: Currency,
                                     val amount: BigDecimal)