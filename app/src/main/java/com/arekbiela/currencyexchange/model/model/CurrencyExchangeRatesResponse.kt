package com.arekbiela.currencyexchange.model.model

import com.google.gson.annotations.SerializedName

data class CurrencyExchangeRatesResponse(@SerializedName("base")  val baseCurrency: String,
                                         @SerializedName("rates") val ratesMap: Map<String, String>)