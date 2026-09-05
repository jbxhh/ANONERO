/*
 * UI-MOCK：原 native(external) 方法全部改为 Kotlin 假实现，仅用于 UI/UX 走查
 */
package io.anonero.model
import io.anonero.AnonConfig
import timber.log.Timber
import java.io.File
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
private const val TAG = "Wallet"
class Wallet {
    var isSynchronized = false
    var isInitialized = false
    private var accountIndex = 0
    private var handle: Long = 0
    private var listenerHandle: Long = 0
    private var pendingTransaction: PendingTransaction? = null
    private var unsignedTransaction: UnsignedTransaction? = null
    private var mockPath: String? = null
    var history: TransactionHistory? = null
        get() {
            if (field == null) {
                field = TransactionHistory(getHistoryJ(), accountIndex)
            }
            return field
        }
    public var coins: Coins? = null
        get() {
            if (field == null) {
                field = Coins(getCoinsJ())
            }
            return field
        }
    internal constructor(handle: Long) {
        this.handle = handle
    }
    fun getPendingTx(): PendingTransaction? {
        return pendingTransaction
    }
    fun getUnsginedTx(): UnsignedTransaction? {
        return unsignedTransaction
    }
    internal constructor(handle: Long, accountIndex: Int) {
        this.handle = handle
        this.accountIndex = accountIndex
    }
    fun getAccountIndex(): Int {
        return accountIndex
    }
    fun setAccountIndex(accountIndex: Int) {
        this.accountIndex = accountIndex
        history?.setAccountFor(this)
    }
    val name: String
        get() = getPath()?.let { File(it).name }.toString()
    fun getSeed(offset: String?): String? = MOCK_SEED
    fun getLegacySeed(offset: String?): String? = MOCK_SEED
    fun isPolyseedSupported(offset: String?): Boolean = true
    fun getSeedLanguage(): String? = "English"
    val status: Status
        get() = statusWithErrorString()
    val fullStatus: Status
        get() {
            val walletStatus = statusWithErrorString()
            walletStatus.connectionStatus = connectionStatus
            return walletStatus
        }
    private fun statusWithErrorString(): Status = Status(0, "")
    @Synchronized
    fun setPassword(password: String?): Boolean = true
    val address: String
        get() = getAddress(accountIndex)
    fun getAddress(accountIndex: Int): String {
        return getAddressJ(accountIndex, 0)
    }
    private fun getSubaddress(addressIndex: Int): String {
        return getAddressJ(accountIndex, addressIndex)
    }
    fun getSubaddress(accountIndex: Int, addressIndex: Int): String {
        return getAddressJ(accountIndex, addressIndex)
    }
    private fun getAddressJ(accountIndex: Int, addressIndex: Int): String = MOCK_ADDRESS
    private fun getSubaddressObject(accountIndex: Int, subAddressIndex: Int): Subaddress {
        return Subaddress(
            accountIndex,
            subAddressIndex,
            getSubaddress(subAddressIndex),
            getSubaddressLabel(0, subAddressIndex)
        )
    }
    fun getSubaddressObject(subAddressIndex: Int): Subaddress {
        val subaddress = getSubaddressObject(accountIndex, subAddressIndex)
        var amount: Long = 0
        history?.let { history ->
            for (info in history.all) {
                if (info.addressIndex == subAddressIndex && info.direction == TransactionInfo.Direction.Direction_In) {
                    amount += info.amount
                }
            }
        }
        subaddress.amount = amount
        return subaddress
    }
    fun getPath(): String? = mockPath ?: MOCK_FILENAME
    val networkType: NetworkType?
        get() = NetworkType.fromInteger(nettype())
    fun nettype(): Int = 0
    fun getIntegratedAddress(paymentId: String?): String? = MOCK_ADDRESS
    fun getSecretViewKey(): String =
        "d5e0853964b50af03b971722f244f58d669cbee3772a077021721a278f64f7fd"
    fun getSecretSpendKey(): String =
        "633dbdde131ca3766e4d58e72e310275dff6c15c0c8e9df469611a11f5125227"
    fun store(): Boolean {
        return store("")
    }
    @Synchronized
    fun store(path: String?): Boolean {
        try {
            if (!path.isNullOrBlank()) {
                mockPath = path
                val f = File(path)
                f.parentFile?.mkdirs()
                if (!f.exists()) f.createNewFile()
            } else {
                AnonConfig.context?.let {
                    val f = AnonConfig.getDefaultWalletFile(it)
                    f.parentFile?.mkdirs()
                    if (!f.exists()) f.createNewFile()
                    mockPath = f.absolutePath
                }
            }
        } catch (_: Throwable) {}
        return true
    }
    fun close(): Boolean {
        disposePendingTransaction()
        return WalletManager.instance?.close(this) == true
    }
    fun getFilename(): String = MOCK_FILENAME
    fun init(upperTransactionSizeLimit: Long): Boolean {
        var daemonAddress = WalletManager.instance?.getDaemonAddress()
        var daemonUsername = WalletManager.instance?.daemonUsername
        var daemonPassword = WalletManager.instance?.daemonPassword
        var proxyAddress = WalletManager.instance?.proxy
        var message = "init("
        if (daemonAddress != null) {
            message = "${message}\n$daemonAddress"
        } else {
            Timber.tag(TAG).i("")
            message = "${message}\ndaemon_address == null"
            daemonAddress = ""
        }
        message = "${message}\nupper_transaction_size_limit = 0 (probably)"
        if (daemonUsername != null) {
            Timber.tag(TAG).i(daemonUsername)
        } else {
            message = "${message}\ndaemon_username == null"
            daemonUsername = ""
        }
        if (daemonPassword != null) {
            Timber.tag(TAG).i(daemonPassword)
        } else {
            message = "${message}daemon_password == null"
            daemonPassword = ""
        }
        if (proxyAddress != null) {
            message = "${message}\nproxy : $proxyAddress"
        } else {
            message = "${message}\nproxy_address = null"
            proxyAddress = ""
        }
        Timber.tag(TAG).i("${message}\n);")
        isInitialized = initJ(
            daemonAddress, upperTransactionSizeLimit,
            daemonUsername, daemonPassword,
            proxyAddress
        )
        return isInitialized
    }
    private fun initJ(
        daemonAddress: String, upperTransactionSizeLimit: Long,
        daemonUsername: String, daemonPassword: String, proxyAddress: String
    ): Boolean = true
    fun getRestoreHeight(): Long = 0L
    fun setRestoreHeight(height: Long) {}
    private val connectionStatus: ConnectionStatus
        get() {
            val s = getConnectionStatusJ()
            return ConnectionStatus.values()[s]
        }
    private fun getConnectionStatusJ(): Int = 1
    fun setTrustedDaemon(trusted: Boolean): Boolean = true
    fun setProxy(address: String?): Boolean {
        return setProxyJ(address)
    }
    private fun setProxyJ(address: String?): Boolean = true
    val balance: Long
        get() = getBalance(accountIndex)
    private fun getBalance(accountIndex: Int): Long = 12_345_600_000_000L
    fun viewOnlyBalance(): Long = 12_345_600_000_000L
    fun getBalanceAll(): Long = 12_345_600_000_000L
    val unlockedBalance: Long
        get() = getUnlockedBalance(accountIndex)
    fun getUnlockedBalanceAll(): Long = 11_000_000_000_000L
    fun getUnlockedBalance(accountIndex: Int): Long = 11_000_000_000_000L
    fun isWatchOnly(): Boolean = false
    fun getBlockChainHeight(): Long = 3_400_000L
    fun getApproximateBlockChainHeight(): Long = 3_400_000L
    fun getDaemonBlockChainHeight(): Long = 3_400_000L
    fun getDaemonBlockChainTargetHeight(): Long = 3_400_000L
    fun setSynchronized() {
        isSynchronized = true
    }
    fun startRefresh() {}
    fun pauseRefresh() {}
    fun refresh(): Boolean = true
    fun refreshAsync() {}
    private fun rescanBlockchainAsyncJ() {}
    fun rescanBlockchainAsync() {
        isSynchronized = false
        rescanBlockchainAsyncJ()
    }
    private fun disposePendingTransaction() {
        if (pendingTransaction != null) {
            disposeTransaction(pendingTransaction)
            pendingTransaction = null
            unsignedTransaction = null
        }
    }
    fun createSweepTransaction(
        dstAddr: String,
        priority: PendingTransaction.Priority,
        keyImages: ArrayList<String>
    ): PendingTransaction? {
        disposePendingTransaction()
        val _priority = priority.ordinal
        val txHandle = createSweepTransaction(dstAddr, "", 0, _priority, accountIndex, keyImages)
        pendingTransaction = PendingTransaction(txHandle)
        unsignedTransaction = null
        return pendingTransaction
    }
    fun createTransactionJ(
        dstAddr: String, paymentId: String,
        amount: Long, mixinCount: Int,
        priority: Int, accountIndex: Int, keyImages: ArrayList<String>
    ): Long = 1L
    fun signAndExportJ(inputFile: String?, outputFile: String?): String? = ""
    @Throws(Exception::class)
    fun createTransaction(
        dst_addr: String?,
    	amount: Long,
    	sweepAll: Boolean = false,
    	mixin_count: Int = 0,
    	priority: PendingTransaction.Priority = PendingTransaction.Priority.Priority_Default,
    	selectedUtxos: List<CoinsInfo> = arrayListOf()
    ): PendingTransaction {
    	disposePendingTransaction()
        val priority: Int = priority.ordinal
    	val preferredInputs = selectedUtxos.map { it.key }.toCollection(ArrayList())
    	if (selectedUtxos.isNotEmpty() && !sweepAll) {
            checkSelectedAmounts(preferredInputs, amount, false)
    	}
    	val txHandle =
            if (sweepAll) createSweepTransaction(
            	dst_addr!!, "", mixin_count, priority,
            	accountIndex, preferredInputs
            ) else createTransactionJ(
            	dst_addr!!, "", amount, mixin_count, priority,
             	accountIndex, preferredInputs
            )
    	pendingTransaction = PendingTransaction(txHandle)
    	unsignedTransaction = null
    	return pendingTransaction!!
    }
    fun send(pendingTransaction: PendingTransaction): Boolean {
        return pendingTransaction.commit("", overwrite = true)
    }
    @Throws(java.lang.Exception::class)
    fun getUtxos(): List<CoinsInfo> {
        return coins?.all ?: listOf()
    }
    @Throws(java.lang.Exception::class)
    private fun checkSelectedAmounts(selectedUtxos: List<String>, amount: Long, sendAll: Boolean) {
        if (!sendAll) {
            var amountSelected: Long = 0
            for (coinsInfo in getUtxos()) {
                if (selectedUtxos.contains(coinsInfo.key)) {
                    amountSelected += coinsInfo.amount
                }
            }
            if (amountSelected <= amount) {
                Timber.tag("Wallet").e("insufficient wallet balance- Available/Selected: $amountSelected, amount: $amount")
                throw java.lang.Exception("insufficient wallet balance")
            }
        }
    }
    private fun createSweepTransaction(
        dstAddr: String, paymentId: String,
        mixinCount: Int,
        priority: Int, accountIndex: Int, keyImages: ArrayList<String>
    ): Long = 1L
    fun createSweepUnmixableTransaction(): PendingTransaction? {
        disposePendingTransaction()
        val txHandle = createSweepUnmixableTransactionJ()
        pendingTransaction = PendingTransaction(txHandle)
        return pendingTransaction
    }
    private fun createSweepUnmixableTransactionJ(): Long = 1L
    private fun disposeTransaction(pendingTransaction: PendingTransaction?) {}
    private fun getHistoryJ(): Long = 1L
    private fun getCoinsJ(): Long = 1L
    fun exportOutputs(filename: String?, all: Boolean): Boolean = true
    fun importOutputs(filename: String?): String? = ""
    fun exportKeyImages(filename: String?, all: Boolean): Boolean = true
    fun hasUnknownKeyImages(): Boolean = false
    fun importKeyImages(filename: String?): Boolean = true
    fun loadUnsignedTransaction(inputFile: String?): UnsignedTransaction {
        val unsignedTx: Long = loadUnsignedTx(inputFile)
        unsignedTransaction = UnsignedTransaction(unsignedTx)
        pendingTransaction = null
        return unsignedTransaction!!
    }
    fun submitTransaction(filename: String?): String? = null
    private fun loadUnsignedTx(inputFile: String?): Long = 1L
    fun refreshHistory() {
        history?.refreshWithNotes(this)
    }
    fun stopBackgroundSync(password: String?): Boolean = true
    fun startBackgroundSync(): Boolean = true
    fun refreshCoins() {
        if (isSynchronized) {
            Timber.tag("Wallet").d("Coin Refreshed: %s", coins?.getCount())
            coins?.refresh()
        }
    }
    private fun setListenerJ(listener: WalletListener?): Long = 1L
    fun setListener(listener: WalletListener?) {
        listenerHandle = setListenerJ(listener)
    }
    fun getDefaultMixin(): Int = 15
    fun setDefaultMixin(mixin: Int) {}
    fun setUserNote(txid: String?, note: String?): Boolean = true
    fun getUserNote(txid: String?): String? = null
    fun getTxKey(txid: String?): String? = null
    @JvmOverloads
    fun addAccount(label: String? = NEW_ACCOUNT_NAME) {}
    var accountLabel: String?
        get() = getAccountLabel(accountIndex)
        set(label) {
            setAccountLabel(accountIndex, label)
        }
    private fun getAccountLabel(accountIndex: Int): String {
        var label = getSubaddressLabel(accountIndex, 0)
        if (label == NEW_ACCOUNT_NAME) {
            val address = getAddress(accountIndex)
            val len = address.length
            label = address.substring(0, 6) +
                    "\u2026" + address.substring(len - 6, len)
        }
        return label
    }
    fun getSubaddressLabel(addressIndex: Int): String {
        return getSubaddressLabel(accountIndex, addressIndex)
    }
    private fun getSubaddressLabel(accountIndex: Int, addressIndex: Int): String = NEW_ACCOUNT_NAME
    private fun setAccountLabel(accountIndex: Int, label: String?) {
        setSubaddressLabel(accountIndex, 0, label)
    }
    fun setSubaddressLabel(addressIndex: Int, label: String?) {
        setSubaddressLabel(accountIndex, addressIndex, label)
        refreshHistory()
    }
    private fun setSubaddressLabel(accountIndex: Int, addressIndex: Int, label: String?) {}
    fun getNumAccounts(): Int = 1
    val numSubAddresses: Int
        get() = getNumSubaddresses(accountIndex)
    private fun getNumSubaddresses(accountIndex: Int): Int = 1
    private fun getNewSubaddress(accountIndex: Int): String {
        val timeStamp = SimpleDateFormat("yyyy-MM-dd-HH:mm:ss", Locale.US).format(Date())
        addSubaddress(accountIndex, timeStamp)
        val subaddress = getLastSubaddress(accountIndex)
        Timber.tag("Wallet").i("${getNumSubaddresses(accountIndex) - 1} : ${subaddress}")
        return subaddress
    }
    fun addSubaddress(accountIndex: Int, label: String?) {}
    private fun getLastSubaddress(accountIndex: Int): String {
        return getSubaddress(accountIndex, getNumSubaddresses(accountIndex) - 1)
    }
    val deviceType: Device
        get() {
            val device = getDeviceTypeJ()
            return Device.values()[device + 1]
        }
    private fun getDeviceTypeJ(): Int = 0
    fun validateAddress(addressField: String): Boolean {
        return WalletManager.instance?.networkType?.value?.let {
            isAddressValid(
                addressField,
                it
            )
        } == true
    }
    enum class Device(val accountLookahead: Int, val subaddressLookahead: Int) {
        Device_Undefined(0, 0), Device_Software(50, 200), Device_Ledger(5, 20)
    }
    enum class StatusEnum {
        Status_Ok, Status_Error, Status_Critical
    }
    enum class ConnectionStatus {
        ConnectionStatus_Disconnected, ConnectionStatus_Connected, ConnectionStatus_WrongVersion
    }
    class Status internal constructor(status: Int, val errorString: String) {
        val status: StatusEnum
        var connectionStatus: ConnectionStatus? = null
        init {
            this.status = StatusEnum.entries.toTypedArray()[status]
        }
        val isOk: Boolean
            get() = (status == StatusEnum.Status_Ok
                    && (connectionStatus == null || connectionStatus == ConnectionStatus.ConnectionStatus_Connected))
        override fun toString(): String {
            return "Wallet.Status: $status/$errorString/$connectionStatus"
        }
    }
    companion object {
        const val SWEEP_ALL = Long.MAX_VALUE
        private const val NEW_ACCOUNT_NAME = "Untitled account"
        private const val MOCK_FILENAME = "mock_wallet"
        private const val MOCK_ADDRESS =
            "4h82pJGF9p7kpzb6eU326EFZf2cDnimbTFVeJtx1qtBmUNJAEqN76R7PwPfHt3oWb8R6cKvhgyxQdDn53jFrK6wFx7RJWhv"
        private const val MOCK_SEED =
            "abbey ability abiding abort absorb abstract absurd abuse access accident account accuse achieve acid acoustic acquire across act action actor actress adapt adept adjust admire"
        init {
            try { System.loadLibrary("anonero") } catch (e: Throwable) {}
        }
        @JvmStatic
        fun getDisplayAmount(amount: Long): String =
            BigDecimal(amount).movePointLeft(12).stripTrailingZeros().toPlainString()
        @JvmStatic
        fun getAmountFromString(amount: String?): Long =
            if (amount.isNullOrBlank()) 0L else BigDecimal(amount).movePointRight(12).toLong()
        @JvmStatic
        fun getAmountFromDouble(amount: Double): Long =
            BigDecimal.valueOf(amount).movePointRight(12).toLong()
        @JvmStatic
        fun generatePaymentId(): String =
            "0000000000000000000000000000000000000000000000000000000000000000"
        @JvmStatic
        fun isPaymentIdValid(payment_id: String?): Boolean =
            payment_id != null && payment_id.matches(Regex("^[0-9a-fA-F]{16,64}$"))
        @JvmStatic
        fun isAddressValid(address: String?, networkType: Int): Boolean =
            !address.isNullOrBlank()
        @JvmStatic
        fun getPaymentIdFromAddress(address: String?, networkType: Int): String? = null
        @JvmStatic
        fun getMaximumAllowedAmount(): Long = Long.MAX_VALUE
    }
}
