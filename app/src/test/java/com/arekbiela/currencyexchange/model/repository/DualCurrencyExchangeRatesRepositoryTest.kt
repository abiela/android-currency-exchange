package com.arekbiela.currencyexchange.model.repository

import com.arekbiela.currencyexchange.config.CurrencyExchangeApplicationConfiguration
import com.arekbiela.currencyexchange.model.local.UserCurrencySelectionLocalService
import com.arekbiela.currencyexchange.model.model.CurrencyExchangeRatesModel
import com.arekbiela.currencyexchange.model.model.UserCurrencySelectionModel
import com.arekbiela.currencyexchange.model.remote.CurrencyExchangeRatesRemoteService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import org.junit.Test
import java.math.BigDecimal
import java.util.*

class DualCurrencyExchangeRatesRepositoryTest {

    private val localCurrencyExchangeRatesService = mockk<UserCurrencySelectionLocalService>()
    private val remoteCurrencyExchangeRatesService = mockk<CurrencyExchangeRatesRemoteService>()
    private val applicationConfiguration = CurrencyExchangeApplicationConfiguration()

    private val dualCurrencyExchangeRatesRepository = DualCurrencyExchangeRatesRepository(
        localCurrencyExchangeRatesService,
        remoteCurrencyExchangeRatesService,
        applicationConfiguration)

    @Test
    fun `given base currency when remote service responds correctly then should return periodic currency exchange rates update`() {
        //given
        val baseCurrency = Currency.getInstance("EUR")
        val currencyExchangeRatesModel =
            CurrencyExchangeRatesModel(baseCurrency, emptyMap())
        every { remoteCurrencyExchangeRatesService.getExchangeRatesForBase(any()) } returns Single.just(currencyExchangeRatesModel)

        //when
        val testObserver = dualCurrencyExchangeRatesRepository.observeCurrencyExchangeRates(baseCurrency).test()

        //then
        verify { remoteCurrencyExchangeRatesService.getExchangeRatesForBase(any()) }
        testObserver
            .awaitCount(2)
            .assertNoErrors()
            .assertValueAt(0, currencyExchangeRatesModel)
    }

    @Test
    fun `given correct answers from local service should return correct user currency observations`() {
        //given
        val baseCurrency = Currency.getInstance("EUR")
        val baseCurrencyAmount = BigDecimal("100.0")
        every { localCurrencyExchangeRatesService.observeUserCurrencySelections() } returns Flowable.just(UserCurrencySelectionModel(baseCurrency, baseCurrencyAmount))

        //when
        val testObserver = dualCurrencyExchangeRatesRepository.observeUserCurrencySelection().test()

        //then
        verify { localCurrencyExchangeRatesService.observeUserCurrencySelections() }
        testObserver
            .awaitCount(1)
            .assertNoErrors()
            .assertValueAt(0, UserCurrencySelectionModel(baseCurrency, baseCurrencyAmount))
    }

    @Test
    fun `given new base currency when make a user base currency selection update should complete successfully`() {
        //given
        val baseCurrency = Currency.getInstance("EUR")
        every { localCurrencyExchangeRatesService.updateUserBaseCurrencySelection(baseCurrency) } returns Completable.complete()

        //when
        val testObserver = dualCurrencyExchangeRatesRepository.updateUserBaseCurrencySelection(baseCurrency).test()

        //then
        verify { localCurrencyExchangeRatesService.updateUserBaseCurrencySelection(baseCurrency) }
        testObserver
            .assertComplete()
            .assertNoErrors()
    }

    @Test
    fun `given new currency amount when make a user currency amount selection update should complete successfully`() {
        //given
        val baseCurrencyAmount = BigDecimal("100.0")
        every { localCurrencyExchangeRatesService.updateUserCurrencyAmountSelection(baseCurrencyAmount) } returns Completable.complete()

        //when
        val testObserver = dualCurrencyExchangeRatesRepository.updateUserCurrencyAmountSelection(baseCurrencyAmount).test()

        //then
        verify { localCurrencyExchangeRatesService.updateUserCurrencyAmountSelection(baseCurrencyAmount) }
        testObserver
            .assertComplete()
            .assertNoErrors()
    }
}