package com.arekbiela.currencyexchange.presentation.model

sealed class CurrencyExchangeListState {
    data class Loading(val message: String): CurrencyExchangeListState()
    data class Error(val throwable: Throwable): CurrencyExchangeListState()
    data class Update(val currencyExchangeList: List<ExchangeRateViewItemModel>): CurrencyExchangeListState()
}