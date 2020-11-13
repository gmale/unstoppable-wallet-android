package io.horizontalsystems.bankwallet.modules.swap_new

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.horizontalsystems.bankwallet.core.App
import io.horizontalsystems.bankwallet.core.ethereum.CoinService
import io.horizontalsystems.bankwallet.core.ethereum.EthereumFeeViewModel
import io.horizontalsystems.bankwallet.core.ethereum.EthereumTransactionService
import io.horizontalsystems.bankwallet.core.factories.FeeRateProviderFactory
import io.horizontalsystems.bankwallet.core.providers.EthereumFeeRateProvider
import io.horizontalsystems.bankwallet.entities.Coin
import io.horizontalsystems.bankwallet.modules.swap_new.viewmodels.SwapViewModel
import io.horizontalsystems.uniswapkit.UniswapKit

object SwapModule {

    enum class AmountType {
        ExactSending, ExactReceiving
    }

    class Factory(private val coinSending: Coin?) : ViewModelProvider.Factory {
        private val ethereumKit by lazy { App.ethereumKitManager.ethereumKit!! }
        private val transactionService by lazy {
            val feeRateProvider = FeeRateProviderFactory.provider(App.appConfigProvider.ethereumCoin) as EthereumFeeRateProvider
            EthereumTransactionService(ethereumKit, feeRateProvider)
        }
        private val ethCoinService by lazy { CoinService(App.appConfigProvider.ethereumCoin, App.currencyManager, App.xRateManager) }

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {

            return when (modelClass) {
                SwapViewModel::class.java -> {
                    val uniswapKit = UniswapKit.getInstance(ethereumKit)

//                    val allowanceProvider = AllowanceProvider(App.adapterManager)
//                    val feeRateProvider = EthereumFeeRateProvider(App.feeRateProvider)
//                    val stringProvider = StringProvider(App.instance)
//
//                    val swapRepository = UniswapRepository(uniswapKit)
//                    val swapService = UniswapService(coinSending, swapRepository, allowanceProvider, App.walletManager, App.adapterManager, transactionService, ethereumKit, App.appConfigProvider.ethereumCoin)
//                    val formatter = SwapItemFormatter(stringProvider, App.numberFormatter)
//                    val confirmationPresenter = ConfirmationPresenter(swapService, stringProvider, formatter, ethCoinService)

                    return SwapViewModel() as T
                }
                EthereumFeeViewModel::class.java -> {
                    EthereumFeeViewModel(transactionService, ethCoinService) as T
                }
                else -> throw IllegalArgumentException()
            }
        }
    }

}
