package com.arekbiela.currencyexchange.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.DiffUtil
import com.arekbiela.currencyexchange.domain.ObserveUserCurrencyExchangeRatesListChangesUseCase
import com.arekbiela.currencyexchange.domain.UpdateBaseCurrencyUseCase
import com.arekbiela.currencyexchange.domain.UpdateCurrencyAmountUseCase
import com.arekbiela.currencyexchange.presentation.model.CurrencyExchangeListState
import com.arekbiela.currencyexchange.presentation.model.ExchangeRateViewItemModel
import com.arekbiela.currencyexchange.presentation.util.ExchangeRateItemDiffList
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.DisposableSubscriber
import java.math.BigDecimal
import javax.inject.Inject

class CurrencyExchangeRatesViewModel @Inject constructor(private val observeUserCurrencyExchangeRatesListChangesUseCase: ObserveUserCurrencyExchangeRatesListChangesUseCase,
                                                         private val updateBaseCurrencyUseCase: UpdateBaseCurrencyUseCase,
                                                         private val updateCurrencyAmountUseCase: UpdateCurrencyAmountUseCase) :ViewModel() {

    private var userBaseCurrencySelectionDisposable: Disposable? = null
    private var userCurrencyAmountDisposable: Disposable? = null
    private var userCurrencyExchangeRatesListChangesDisposable: Disposable? = null


    fun observeExchangeListState(flowableSubscriber: DisposableSubscriber<CurrencyExchangeListState>) {
        flowableSubscriber.onNext(CurrencyExchangeListState.Loading("Currency exchange list is loading..."))
        userCurrencyExchangeRatesListChangesDisposable?.dispose()
        observeUserCurrencyExchangeRatesListChangesUseCase
            .run()
            .map { CurrencyExchangeListState.Update(it) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(flowableSubscriber)
            .let { userCurrencyExchangeRatesListChangesDisposable = it }
    }

    fun onCurrencyItemSelected(exchangeRateViewItemModel: ExchangeRateViewItemModel) {
        userBaseCurrencySelectionDisposable?.dispose()
        updateBaseCurrencyUseCase.run(exchangeRateViewItemModel.currency)
            .andThen(updateCurrencyAmountUseCase.run(exchangeRateViewItemModel.amount))
            .subscribe()
            .let { userBaseCurrencySelectionDisposable = it }
    }

    fun onCurrencyAmountChanged(newCurrencyAmount: BigDecimal) {
        userCurrencyAmountDisposable?.dispose()
        updateCurrencyAmountUseCase.run(newCurrencyAmount)
            .subscribe()
            .let { userCurrencyAmountDisposable = it }
    }

    fun onCalculateExchangeRateListsDifferences(oldList: List<ExchangeRateViewItemModel>, newList: List<ExchangeRateViewItemModel>): Single<DiffUtil.DiffResult> {
        return Single.just(DiffUtil.calculateDiff(ExchangeRateItemDiffList(oldList, newList)))
            .subscribeOn(Schedulers.io())
    }

    override fun onCleared() {
        super.onCleared()
        userCurrencyAmountDisposable?.dispose()
        userBaseCurrencySelectionDisposable?.dispose()
        userCurrencyExchangeRatesListChangesDisposable?.dispose()
    }
}