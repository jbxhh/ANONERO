/* UI-MOCK：MOCK_MODE==4 时为发送失败态 */
package io.anonero.model

class PendingTransaction internal constructor(override var handle: Long) : StagingTransaction {
    val status: Status
        get() = Status.values()[getStatusJ()]

    fun getStatusJ(): Int = if (Wallet.MOCK_MODE == 4) 1 else 0
    fun getErrorString(): String? =
        if (Wallet.MOCK_MODE == 4) "模拟：交易广播失败" else ""
    fun commit(filename: String?, overwrite: Boolean): Boolean = Wallet.MOCK_MODE != 4
    fun getAmount(): Long = 0L
    fun getDust(): Long = 0L
    fun getFee(): Long = 10_000_000_000L
    fun getFirstTxIdJ(): String? =
        "c3712da86a78c49ea20e32684b27b95e909348334896a68f812d810a485ed032"
    fun getTxCount(): Long = 1L

    enum class Status {
        Status_Ok, Status_Error, Status_Critical
    }

    enum class Priority(value: Int) {
        Priority_Default(0), Priority_Low(1), Priority_Medium(2), Priority_High(3), Priority_Last(4);
        companion object {
            fun fromInteger(n: Int): Priority? {
                when (n) {
                    0 -> return Priority_Default
                    1 -> return Priority_Low
                    2 -> return Priority_Medium
                    3 -> return Priority_High
                }
                return null
            }
        }
    }

    companion object {
        init {
            try { System.loadLibrary("anonero") } catch (e: Throwable) {}
        }
    }
}
