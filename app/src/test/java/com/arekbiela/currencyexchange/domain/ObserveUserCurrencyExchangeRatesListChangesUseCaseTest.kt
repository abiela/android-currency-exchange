package com.arekbiela.currencyexchange.domain

import com.arekbiela.currencyexchange.config.CurrencyExchangeApplicationConfiguration
import com.arekbiela.currencyexchange.model.model.CurrencyExchangeRatesModel
import com.arekbiela.currencyexchange.model.model.UserCurrencySelectionModel
import com.arekbiela.currencyexchange.model.repository.CurrencyExchangeRatesRepository
import com.arekbiela.currencyexchange.presentation.model.ExchangeRateViewItemModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Flowable
import org.junit.Test
import java.math.BigDecimal
import java.math.MathContext
import java.util.*

class ObserveUserCurrencyExchangeRatesListChangesUseCaseTest {

    private val repository = mockk<CurrencyExchangeRatesRepository>()
    private val configuration = CurrencyExchangeApplicationConfiguration()
    private val mathContext = MathContext(configuration.mathCalculationPrecision, configuration.mathCalculationRoundingMode)
    private val useCase = ObserveUserCurrencyExchangeRatesListChangesUseCase(repository, configuration, mathContext)

    @Test
    fun `when running with correct repository answers should return currency exchange list stream`() {
        //given
        val baseCurrency = Currency.getInstance("EUR")
        val baseCurrencyAmount = BigDecimal("100.0")
        every { repository.observeCurrencyExchangeRates(baseCurrency) }returns
                Flowable.just(
                    CurrencyExchangeRatesModel(
                        baseCurrency,
                        mapOf(Pair(Currency.getInstance("PLN"), BigDecimal("4.0")))
                    )
                )
        every { repository.observeUserCurrencySelection() } returns Flowable.just(
            UserCurrencySelectionModel(
                baseCurrency,
                baseCurrencyAmount
            )
        )

        //when
        val testSubscriber = useCase.run().test()

        //then
        verify { repository.observeCurrencyExchangeRates(baseCurrency) }
        verify { repository.observeUserCurrencySelection() }
        testSubscriber
            .awaitCount(1)
            .assertNoErrors()
            .assertValue(listOf(
                ExchangeRateViewItemModel(Currency.getInstance("EUR"), BigDecimal("100.00")),
                ExchangeRateViewItemModel(Currency.getInstance("PLN"), BigDecimal("400.00"))))
    }

    @Test
    fun `when running with base currency different than start base should return currency exchange list stream`() {
        //given
        val baseCurrency = Currency.getInstance("EUR")
        val userSelectionBaseCurrency = Currency.getInstance("USD")
        val userBaseCurrencyAmount = BigDecimal("100.0")
        every { repository.observeCurrencyExchangeRates(baseCurrency) } returns
                Flowable.just(
                    CurrencyExchangeRatesModel(
                        baseCurrency, mapOf(
                            Pair(Currency.getInstance("PLN"), BigDecimal("4.0")), Pair(
                                Currency.getInstance("USD"), BigDecimal("2.0")
                            )
                        )
                    )
                )
        every { repository.observeUserCurrencySelection() } returns Flowable.just(
            UserCurrencySelectionModel(
                userSelectionBaseCurrency,
                userBaseCurrencyAmount
            )
        )

        //when
        val testSubscriber = useCase.run().test()

        //then
        verify { repository.observeCurrencyExchangeRates(baseCurrency) }
        verify { repository.observeUserCurrencySelection() }
        testSubscriber
            .awaitCount(1)
            .assertNoErrors()
            .assertValue(listOf(
                ExchangeRateViewItemModel(Currency.getInstance("USD"), BigDecimal("100.0")),
                ExchangeRateViewItemModel(Currency.getInstance("EUR"), BigDecimal("50.0")),
                ExchangeRateViewItemModel(Currency.getInstance("PLN"), BigDecimal("200.0"))))
    }
}
