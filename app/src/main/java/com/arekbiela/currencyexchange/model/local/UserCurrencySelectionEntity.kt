package com.arekbiela.currencyexchange.model.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_currency_selection")
data class UserCurrencySelectionEntity(
    @PrimaryKey
    val selectionId: Int,
    @ColumnInfo(name = "selected_currency_code")
    val selectedCurrencyCode: String,
    @ColumnInfo(name = "selected_currency_amount")
    val selectedCurrencyAmount: String)