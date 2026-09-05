/* UI-MOCK：空列表，不依赖 native 库 */
package io.anonero.model

class Coins(private val handle: Long){
    var all: List<CoinsInfo> = ArrayList()
        private set

    fun refresh() {
        all = refreshJ()
    }

    private fun refreshJ(): List<CoinsInfo> = listOf()
    fun getCount(): Int = all.size

    companion object {
        init {
            try { System.loadLibrary("anonero") } catch (e: Throwable) {}
        }
    }
}
