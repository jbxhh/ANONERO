/* UI-MOCK：空交易列表，不依赖 native 库 */
package io.anonero.model

import timber.log.Timber

private const val TAG = "TransactionHistory"

class TransactionHistory(private val handle: Long, var accountIndex: Int) {
    var all: List<TransactionInfo> = ArrayList()
        private set

    fun setAccountFor(wallet: Wallet) {
        if (accountIndex != wallet.getAccountIndex()) {
            accountIndex = wallet.getAccountIndex()
            refreshWithNotes(wallet)
        }
    }

    private fun loadNotes(wallet: Wallet) {
        for (info in all) {
            info.notes = wallet.getUserNote(info.hash)
        }
    }

    fun getCount(): Int = all.size

    fun refreshWithNotes(wallet: Wallet) {
        refresh()
        loadNotes(wallet)
    }

    private fun refresh() {
        val transactionInfos = refreshJ()
        Timber.tag(TAG).d("refresh size=%s", transactionInfos.size)
        val iterator = transactionInfos.iterator()
        while (iterator.hasNext()) {
            val info = iterator.next()
            if (info.accountIndex != accountIndex) {
                iterator.remove()
            }
        }
        all = transactionInfos
    }

    private fun refreshJ(): MutableList<TransactionInfo> = mutableListOf()

    companion object {
        init {
            try { System.loadLibrary("anonero") } catch (e: Throwable) {}
        }
    }
}
