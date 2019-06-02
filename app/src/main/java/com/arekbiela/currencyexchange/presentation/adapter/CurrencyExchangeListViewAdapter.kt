package com.arekbiela.currencyexchange.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.arekbiela.currencyexchange.R
import com.arekbiela.currencyexchange.presentation.model.ExchangeRateViewItemModel
import com.arekbiela.currencyexchange.presentation.util.CurrencyAmountDifference
import com.arekbiela.currencyexchange.presentation.util.CurrencyAmountEditTextWatcher
import com.squareup.picasso.Picasso
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.item_currency_value.view.*
import java.math.BigDecimal

class CurrencyExchangeListViewAdapter(private val context: Context,
                                      private val onCurrencyItemSelected: (ExchangeRateViewItemModel) -> Unit,
                                      onCurrencyAmountChanged: (BigDecimal) -> Unit,
                                      private val onCalculateDiffList: (List<ExchangeRateViewItemModel>, List<ExchangeRateViewItemModel>) -> Single<DiffUtil.DiffResult>,
                                      private var exchangeRateViewItemModel: MutableList<ExchangeRateViewItemModel>) :RecyclerView.Adapter<CurrencyExchangeListViewAdapter.CurrencyItemViewHolder>() {

    private val currencyAmountEditTextWatcher =
        CurrencyAmountEditTextWatcher(onCurrencyAmountChanged)
    private var exchangeRateListsDifferenceCalculationDisposable: Disposable? = null

    fun updateItems(newExchangeRateViewItemModel: List<ExchangeRateViewItemModel>) {
        exchangeRateListsDifferenceCalculationDisposable?.dispose()
        onCalculateDiffList(exchangeRateViewItemModel, newExchangeRateViewItemModel)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                exchangeRateViewItemModel.clear()
                exchangeRateViewItemModel.addAll(newExchangeRateViewItemModel)
                it.dispatchUpdatesTo(this@CurrencyExchangeListViewAdapter)}, {})
            .let { exchangeRateListsDifferenceCalculationDisposable = it }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyItemViewHolder {
        val viewHolder =
            CurrencyItemViewHolder(
                LayoutInflater.from(context).inflate(R.layout.item_currency_value, parent, false)
            )
        viewHolder.itemView.setOnClickListener {
            if(viewHolder.adapterPosition != 0) viewHolder.currencyAmountEditText.requestFocus() }
        viewHolder.currencyAmountEditText.apply { setOnFocusChangeListener { view, focused ->
            when(focused) {
                true -> {
                    onCurrencyItemSelected(exchangeRateViewItemModel[viewHolder.adapterPosition])
                    addTextChangedListener(currencyAmountEditTextWatcher) }
                false -> removeTextChangedListener(currencyAmountEditTextWatcher) }}}
        return viewHolder
    }

    override fun onBindViewHolder(holder: CurrencyItemViewHolder, position: Int) {
        val currencyItem = exchangeRateViewItemModel[position]
        holder.currencyCodeText?.text = currencyItem.currency.currencyCode
        holder.currencyNameText?.text = currencyItem.currency.displayName
        Picasso.get()
            .load(context.resources.getIdentifier(currencyItem.currency.currencyCode.toLowerCase().plus(context.getString(R.string.flag_suffix)), context.getString(R.string.drawable_text), context.applicationInfo.packageName))
            .into(holder.currencyFlagImage)
        with(holder.currencyAmountEditText) {
            if (!isFocused) setText(currencyItem.amount.toPlainString())
        }
    }

    override fun onBindViewHolder(holder: CurrencyItemViewHolder, position: Int, payloads: MutableList<Any>) {
        when(payloads.isEmpty()) {
            true -> onBindViewHolder(holder, position)
            false -> with(holder.currencyAmountEditText) { if(!isFocused) setText((payloads[0] as CurrencyAmountDifference).newCurrencyAmountValue.toPlainString()) }
        }
    }

    override fun getItemCount(): Int {
        return exchangeRateViewItemModel.size
    }

    override fun getItemId(position: Int): Long = exchangeRateViewItemModel[position].currency.hashCode().toLong()

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        exchangeRateListsDifferenceCalculationDisposable?.dispose()
    }

    class CurrencyItemViewHolder(itemLayout: View) : RecyclerView.ViewHolder(itemLayout) {
        val currencyCodeText = itemLayout.currency_code
        val currencyNameText = itemLayout.currency_name
        val currencyAmountEditText = itemLayout.currency_amount
        val currencyFlagImage = itemLayout.currency_flag
    }
}