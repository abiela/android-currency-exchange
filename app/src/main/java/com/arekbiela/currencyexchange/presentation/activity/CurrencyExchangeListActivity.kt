package com.arekbiela.currencyexchange.presentation.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.arekbiela.currencyexchange.R
import com.arekbiela.currencyexchange.presentation.adapter.CurrencyExchangeListViewAdapter
import com.arekbiela.currencyexchange.presentation.model.CurrencyExchangeListState
import com.arekbiela.currencyexchange.presentation.model.ExchangeRateViewItemModel
import com.arekbiela.currencyexchange.presentation.viewmodel.CurrencyExchangeRatesViewModel
import dagger.android.AndroidInjection
import io.reactivex.disposables.Disposable
import io.reactivex.subscribers.DisposableSubscriber
import kotlinx.android.synthetic.main.activity_currency_exchange.*
import javax.inject.Inject

class CurrencyExchangeListActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: CurrencyExchangeRatesViewModel

    lateinit var exchangeRateListViewAdapter: CurrencyExchangeListViewAdapter
    private var exchangeListDisposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_currency_exchange)
        viewModel = ViewModelProviders.of(this, viewModelFactory)[CurrencyExchangeRatesViewModel::class.java]

        exchangeRateListViewAdapter = CurrencyExchangeListViewAdapter(
            this,
            viewModel::onCurrencyItemSelected,
            viewModel::onCurrencyAmountChanged,
            viewModel::onCalculateExchangeRateListsDifferences,
            mutableListOf()).apply { setHasStableIds(true) }

        exchange_rates_item_list
            .apply {
                layoutManager = LinearLayoutManager(this@CurrencyExchangeListActivity)
                adapter = exchangeRateListViewAdapter
            }
    }

    override fun onResume() {
        super.onResume()
        viewModel
            .observeExchangeListState(
                object: DisposableSubscriber<CurrencyExchangeListState>() {
                    override fun onComplete() {}

                    override fun onNext(state: CurrencyExchangeListState) {
                        when (state) {
                            is CurrencyExchangeListState.Loading -> showLoading()
                            is CurrencyExchangeListState.Error -> showError(getString(R.string.default_error_text))
                            is CurrencyExchangeListState.Update -> updateItems(state.currencyExchangeList)
                        }
                    }

                    override fun onError(t: Throwable) {
                        Log.e("CEActivity", "Error: ${t.message}")
                        showError(getString(R.string.default_error_text))}
                }
            )
    }

    override fun onPause() {
        exchangeListDisposable?.dispose()
        super.onPause()
    }

    private fun showLoading() {
        progress_bar.visibility = View.VISIBLE

        exchange_rates_item_list.visibility = View.GONE
        progress_text.visibility = View.GONE
        error_image.visibility = View.GONE
        error_text.visibility = View.GONE
    }

    private fun showError(message: String) {
        error_image.visibility = View.VISIBLE
        error_text.visibility = View.VISIBLE
        error_text.text = message

        progress_bar.visibility = View.GONE
        exchange_rates_item_list.visibility = View.GONE
        progress_text.visibility = View.GONE
    }

    private fun updateItems(list: List<ExchangeRateViewItemModel>) {
        exchange_rates_item_list.visibility = View.VISIBLE
        exchangeRateListViewAdapter.updateItems(list)

        error_image.visibility = View.GONE
        error_text.visibility = View.GONE
        progress_bar.visibility = View.GONE
        progress_text.visibility = View.GONE
    }

}
