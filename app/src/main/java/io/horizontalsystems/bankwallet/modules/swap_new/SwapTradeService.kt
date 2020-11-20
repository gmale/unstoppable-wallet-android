package io.horizontalsystems.bankwallet.modules.swap_new

import io.horizontalsystems.bankwallet.entities.Coin
import io.horizontalsystems.bankwallet.modules.swap_new.SwapModule.AmountType
import io.horizontalsystems.uniswapkit.models.TradeData
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import java.math.BigDecimal
import java.util.*


class SwapTradeService(
        coinFrom: Coin?
) {

    //region internal subjects
    private val amountTypSubject = PublishSubject.create<AmountType>()
    private val coinFromSubject = PublishSubject.create<Optional<Coin>>()
    private val coinToSubject = PublishSubject.create<Optional<Coin>>()
    //endregion

    //region outputs
    var amountType: AmountType = AmountType.ExactFrom
        private set(value) {
            field = value
            amountTypSubject.onNext(value)
        }
    val amountTypeObservable: Observable<AmountType> = amountTypSubject

    var coinFrom: Coin? = coinFrom
        private set(value) {
            field = value
            coinFromSubject.onNext(Optional.ofNullable(value))
        }
    val coinFromObservable: Observable<Optional<Coin>> = coinFromSubject

    var coinTo: Coin? = null
        private set(value) {
            field = value
            coinToSubject.onNext(Optional.ofNullable(value))
        }
    val coinToObservable: Observable<Optional<Coin>> = coinToSubject

    //endregion

    init {

    }

    fun updateCoinFrom(coin: Coin?) {
        if (coinFrom != coin) {
            coinFrom = coin
        }
        if (coinTo == coinFrom) {
            coinTo = null
        }
    }

    fun updateCoinTo(coin: Coin?) {
        if (coinTo != coin) {
            coinTo = coin
        }
        if (coinFrom == coinTo) {
            coinFrom = null
        }
    }

    //region models
    enum class PriceImpactLevel {
        None, Normal, Warning, Forbidden
    }

    data class Trade(
            val tradeData: TradeData
    ) {
        val priceImpactLevel: PriceImpactLevel = tradeData.priceImpact?.let {
            when {
                it >= BigDecimal.ZERO && it < warningPriceImpact -> PriceImpactLevel.Normal
                it >= warningPriceImpact && it < forbiddenPriceImpact -> PriceImpactLevel.Warning
                else -> PriceImpactLevel.Forbidden

            }
        } ?: PriceImpactLevel.None
    }
    //endregion

    companion object {
        private val warningPriceImpact = BigDecimal(1)
        private val forbiddenPriceImpact = BigDecimal(5)
    }

}
