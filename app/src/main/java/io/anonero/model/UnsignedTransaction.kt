/* UI-MOCK：假未签名交易，不依赖 native 库 */
package io.anonero.model

class UnsignedTransaction internal constructor(override var handle: Long) : StagingTransaction {
    enum class Status {
        Status_Ok,
        Status_Error,
        Status_Critical
    }

    enum class Priority(val value: Int) {
        Priority_Default(0),
        Priority_Low(1),
        Priority_Medium(2),
        Priority_High(3),
        Priority_Last(4);

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

    val status: Status
        get() = Status.entries[statusJ]

    val statusJ: Int
        get() = 0

    val errorString: String?
        get() = null

    val address: String?
        get() = "4h82pJGF9p7kpzb6eU326EFZf2cDnimbTFVeJtx1qtBmUNJAEqN76R7PwPfHt3oWb8R6cKvhgyxQdDn53jFrK6wFx7RJWhv"

    val amount: Long
        get() = 0L

    val fee: Long
        get() = 0L

    val firstTxId: String
        get() = firstTxIdJ

    val firstTxIdJ: String
        get() = "c3712da86a78c49ea20e32684b27b95e909348334896a68f812d810a485ed032"

    companion object {
        init {
            try { System.loadLibrary("anonero") } catch (e: Throwable) {}
        }
    }
}
