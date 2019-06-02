package com.arekbiela.currencyexchange.model.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.arekbiela.currencyexchange.model.model.UserCurrencySelectionModel
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.math.BigDecimal
import java.util.*

@RunWith(AndroidJUnit4::class)
class RoomUserCurrencySelectionLocalServiceTest {

    private lateinit var databaseUser: RoomUserCurrencySelectionDatabase
    private lateinit var localService: RoomUserCurrencySelectionLocalService

    private val startBaseCurrency = Currency.getInstance("EUR")
    private val startBaseCurrencyAmount = "100.0"

    @get:Rule var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun init() {
        //Initialize Room databaseUser
        databaseUser = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().targetContext,
            RoomUserCurrencySelectionDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        //Prepopulate first record
        databaseUser.userCurrencySelectionDao().insert(UserCurrencySelectionEntity(1, startBaseCurrency.currencyCode, startBaseCurrencyAmount)).blockingAwait()

        //Create a local service
        localService = RoomUserCurrencySelectionLocalService(databaseUser)
    }

    @After
    fun closeDb() {
        databaseUser.close()
    }

    @Test
    fun whenRunningObservationWithNoWriteActionsShouldReturnUserCurrencySelectionWithPrepopulatedData() {
        //when
        val testObserver = localService
            .observeUserCurrencySelections()
            .test()

        //then
        testObserver
            .awaitCount(1)
            .assertValueCount(1)
            .assertValue(UserCurrencySelectionModel(startBaseCurrency, BigDecimal(startBaseCurrencyAmount)))
    }

    @Test
    fun whenUpdatingCurrencyAmountShouldReturnUserCurrencySelectionWithChangedAmount() {
        //given
        val newCurrencyAmount = BigDecimal("200.0")
        localService
            .updateUserCurrencyAmountSelection(newCurrencyAmount)
            .blockingAwait()

        //when
        val testObserver = localService
            .observeUserCurrencySelections()
            .test()

        //then
        testObserver
            .awaitCount(1)
            .assertValueCount(1)
            .assertValueAt(0, UserCurrencySelectionModel(startBaseCurrency, newCurrencyAmount))
    }

    @Test
    fun whenUpdatingCurrencyBaseShouldReturnUserCurrencySelectionWithChangedBase() {
        //given
        val newCurrencyBase = Currency.getInstance("USD")
        localService
            .updateUserBaseCurrencySelection(newCurrencyBase)
            .blockingAwait()

        //when
        val testObserver = localService
            .observeUserCurrencySelections()
            .test()

        //then
        testObserver
            .awaitCount(1)
            .assertValueCount(1)
            .assertValueAt(0, UserCurrencySelectionModel(newCurrencyBase, BigDecimal(startBaseCurrencyAmount)))
    }
}