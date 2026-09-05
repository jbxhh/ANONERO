/* UI-MOCK：返回 2 条假 UTXO，不依赖 native 库 */
package io.anonero.model

class Coins(private val handle: Long){
    var all: List<CoinsInfo> = ArrayList()
        private set

    fun refresh() {
        all = refreshJ()
    }

    private fun refreshJ(): List<CoinsInfo> {
        val day = 86_400_000L
        val now = System.currentTimeMillis()
        return listOf(
            CoinsInfo(0L, false, "mock-key-001", 7_000_000_000_000L,
                "e5f6071829300000000000000000000000000000000000000000000000000001",
                "mock-pub-001", false, now - day * 10),
            CoinsInfo(0L, false, "mock-key-002", 5_345_600_000_000L,
                "f607182930410000000000000000000000000000000000000000000000000002",
                "mock-pub-002", false, now - day * 5)
        )
    }

    fun getCount(): Int = all.size

    companion object {
        init {
            try { System.loadLibrary("anonero") } catch (e: Throwable) {}
        }
    }
}
