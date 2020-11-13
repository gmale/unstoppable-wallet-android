package io.horizontalsystems.bankwallet.modules.swap_new

import io.horizontalsystems.uniswapkit.models.TradeData
import java.math.BigDecimal

class SwapTradeService {

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
