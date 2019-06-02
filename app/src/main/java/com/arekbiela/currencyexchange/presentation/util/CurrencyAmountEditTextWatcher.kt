package com.arekbiela.currencyexchange.presentation.util

import android.text.Editable
import android.text.TextWatcher
import java.math.BigDecimal

class CurrencyAmountEditTextWatcher(private val onCurrencyAmountChanged: (BigDecimal) -> Unit): TextWatcher {

    override fun afterTextChanged(p0: Editable?) {}

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    override fun onTextChanged(currentAmountValueText: CharSequence?, p1: Int, p2: Int, p3: Int) {
        if (!currentAmountValueText.isNullOrEmpty()) {
            onCurrencyAmountChanged(BigDecimal(currentAmountValueText.toString()))
        }
    }
}