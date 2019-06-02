package com.arekbiela.currencyexchange.model.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Flowable

@Dao
interface UserCurrencySelectionDao {

    @Query("SELECT * FROM user_currency_selection")
    fun observeUserCurrencyAmountChanges(): Flowable<UserCurrencySelectionEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(userCurrencySelectionEntity: UserCurrencySelectionEntity): Completable

    @Query("UPDATE user_currency_selection SET selected_currency_code = :baseCurrencyCode")
    fun updateUserBaseCurrency(baseCurrencyCode: String): Completable

    @Query("UPDATE user_currency_selection SET selected_currency_amount = :currencyAmount")
    fun updateUserCurrencyAmount(currencyAmount: String): Completable
}