/* UI-MOCK：构造即装 4 条假交易，不依赖同步回调 */
package io.anonero.model

import timber.log.Timber

private const val TAG = "TransactionHistory"

class TransactionHistory(private val handle: Long, var accountIndex: Int) {
    var all: List<TransactionInfo> = buildMock(accountIndex)
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
        all = buildMock(accountIndex)
        Timber.tag(TAG).d("refresh size=%s", all.size)
    }

    private fun buildMock(idx: Int): MutableList<TransactionInfo> {
        val now = System.currentTimeMillis()/
        1000
        val h = 3_600L
        return mutableListOf(
            TransactionInfo(
                0, false, false, 3_500_000_000_000L, 0L, 3_399_900L,
                "a1b2c3d4e5f60000000000000000000000000000000000000000000000000001",
                now - h * 26, null, idx, 0, 20L, "Main account", null
            ),
            TransactionInfo(
                1, false, false, 1_200_000_000_000L, 10_000_000_000L, 3_399_800L,
                "b2c3d4e5f6070000000000000000000000000000000000000000000000000002",
                now - h * 50, null, idx, 0, 15L, null, null
            ),
            TransactionInfo(
                0, true, false, 250_000_000_000L, 0L, 0L,
                "c3d4e5f607180000000000000000000000000000000000000000000000000003",
                now - h * 2, null, idx, 0, 3L, null, null
            ),
            TransactionInfo(
                1, false, true, 80_000_000_000L, 10_000_000_000L, 3_399_700L,
                "d4e5f60718290000000000000000000000000000000000000000000000000004",
                now - h * 80, null, idx, 0, 0L, null, null
            )
        )
    }

    companion object {
        init {
            try { System.loadLibrary("anonero") } catch (e: Throwable) {}
        }
    }
}
