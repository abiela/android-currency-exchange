package com.arekbiela.currencyexchange.model.remote

import com.arekbiela.currencyexchange.model.model.CurrencyExchangeRatesModel
import com.arekbiela.currencyexchange.model.model.CurrencyExchangeRatesResponse
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Single
import org.junit.Test
import java.io.IOException
import java.util.*

class RevolutCurrencyExchangeRatesRemoteServiceTest {

    private val revolutCurrencyExchangeRatesAPI = mockk<RevolutCurrencyExchangeRatesAPI>()
    private val revolutCurrencyExchangeRatesRemoteService = RevolutCurrencyExchangeRatesRemoteService(revolutCurrencyExchangeRatesAPI)

    @Test
    fun `given base currency and correct API answer when getting exchange rates returns currency exchange rates model`() {
        //given
        val baseCurrency = "EUR"
        val currencyExchangeRatesResponse =
            CurrencyExchangeRatesResponse(baseCurrency, emptyMap())
        every { revolutCurrencyExchangeRatesAPI.getCurrencyRatesForBase(any()) } returns Single.just(currencyExchangeRatesResponse)

        //when
        val testObserver = revolutCurrencyExchangeRatesRemoteService.getExchangeRatesForBase(Currency.getInstance(baseCurrency)).test()

        //then
        verify { revolutCurrencyExchangeRatesAPI.getCurrencyRatesForBase(Currency.getInstance(baseCurrency)) }
        testObserver
            .assertComplete()
            .assertValue(CurrencyExchangeRatesModel(Currency.getInstance(baseCurrency), emptyMap()))
            .assertNoErrors()
    }

    @Test
    fun `given base currency and incorrect API answer when getting exchange rates returns error`() {
        val baseCurrency = Currency.getInstance("EUR")
        every { revolutCurrencyExchangeRatesAPI.getCurrencyRatesForBase(any()) } returns Single.error(IOException())

        //when
        val testObserver = revolutCurrencyExchangeRatesRemoteService.getExchangeRatesForBase(baseCurrency).test()

        //then
        verify { revolutCurrencyExchangeRatesAPI.getCurrencyRatesForBase(baseCurrency) }
        testObserver
            .assertNoValues()
            .assertError(IOException::class.java)
    }
}