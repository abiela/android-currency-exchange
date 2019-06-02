package com.arekbiela.currencyexchange.presentation.util

import androidx.recyclerview.widget.DiffUtil
import com.arekbiela.currencyexchange.presentation.model.ExchangeRateViewItemModel
import java.math.BigDecimal

class ExchangeRateItemDiffList(private val oldList: List<ExchangeRateViewItemModel>,
                               private val newList: List<ExchangeRateViewItemModel>) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition].currency.currencyCode == newList[newItemPosition].currency.currencyCode

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition].amount == newList[newItemPosition].amount

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? =
        CurrencyAmountDifference(newList[newItemPosition].amount)
}

data class CurrencyAmountDifference(val newCurrencyAmountValue: BigDecimal)