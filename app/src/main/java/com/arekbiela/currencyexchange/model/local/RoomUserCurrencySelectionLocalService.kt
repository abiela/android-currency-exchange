package com.arekbiela.currencyexchange.model.local

import com.arekbiela.currencyexchange.model.model.UserCurrencySelectionModel
import io.reactivex.Completable
import io.reactivex.Flowable
import java.math.BigDecimal
import java.util.*

class RoomUserCurrencySelectionLocalService(private val roomUserCurrencySelectionDatabase: RoomUserCurrencySelectionDatabase) : UserCurrencySelectionLocalService {

    override fun observeUserCurrencySelections(): Flowable<UserCurrencySelectionModel> =
        roomUserCurrencySelectionDatabase
            .userCurrencySelectionDao()
            .observeUserCurrencyAmountChanges()
            .map {
                UserCurrencySelectionModel(
                    Currency.getInstance(it.selectedCurrencyCode),
                    BigDecimal(it.selectedCurrencyAmount)
                )
            }

    override fun updateUserBaseCurrencySelection(newBaseCurrency: Currency): Completable =
        roomUserCurrencySelectionDatabase
            .userCurrencySelectionDao()
            .updateUserBaseCurrency(newBaseCurrency.currencyCode)

    override fun updateUserCurrencyAmountSelection(newCurrencyAmount: BigDecimal): Completable =
        roomUserCurrencySelectionDatabase
            .userCurrencySelectionDao()
            .updateUserCurrencyAmount(newCurrencyAmount.toPlainString())
}