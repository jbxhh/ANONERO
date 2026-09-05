/*
 * UI-MOCK：原 native(external) 方法全部改为 Kotlin 假实现，仅用于 UI/UX 走查
 */
package io.anonero.model
import io.anonero.AnonConfig
import io.anonero.model.node.Node
import timber.log.Timber
import java.io.File
import java.util.Locale
private const val TAG = "WalletManager"
class WalletManager {
    var networkType = NetworkType.NetworkType_Mainnet
    var wallet: Wallet? = null
        private set
    private var daemonAddress: String? = null
    var daemonUsername = ""
        private set
    var daemonPassword = ""
        private set
    var proxy = ""
        private set
    fun init() {
        this.networkType = AnonConfig.getNetworkType()
    }
    private fun manageWallet(wallet: Wallet) {
        Timber.tag(TAG).i("Managing %s", wallet.name)
        this.wallet = wallet
    }
    private fun unmanageWallet(wallet: Wallet?) {
        requireNotNull(wallet) { "Cannot unmanage null!" }
        checkNotNull(this.wallet) { "No wallet under management!" }
        check(this.wallet === wallet) { wallet.name + " not under management!" }
        Timber.tag(TAG).i("Unmanaging ${wallet.name}")
        this.wallet = null
    }
    fun createWallet(
        aFile: File,
        password: String,
        passphrase: String,
        language: String,
        height: Long
    ): Wallet {
        val walletHandle =
            createWalletJ(aFile.absolutePath, password, passphrase, language, networkType.value)
        val wallet = Wallet(walletHandle)
        manageWallet(wallet)
        if (wallet.status.isOk) {
            wallet.setPassword(password)
        }
        return wallet
    }
    private fun createWalletJ(
        path: String,
        password: String,
        passphrase: String,
        language: String,
        networkType: Int
    ): Long = 1L
    fun openWallet(path: String, password: String): Wallet {
        val walletHandle = openWalletJ(path, password, networkType.value, AnonConfig.viewOnly)
        val wallet = Wallet(walletHandle)
        manageWallet(wallet)
        return wallet
    }
    private fun openWalletJ(
        path: String,
        password: String,
        networkType: Int,
        isViewOnly: Boolean
    ): Long = 1L
    fun recoveryWallet(
        aFile: File,
        password: String,
        mnemonic: String,
        offset: String,
        restoreHeight: Long
    ): Wallet {
        val walletHandle = recoveryWalletJ(
            aFile.absolutePath, password,
            mnemonic, offset,
            networkType.value, restoreHeight
        )
        val wallet = Wallet(walletHandle)
        manageWallet(wallet)
        return wallet
    }
    private fun recoveryWalletJ(
        path: String, password: String,
        mnemonic: String, offset: String,
        networkType: Int, restoreHeight: Long
    ): Long = 1L
    fun recoveryWalletPolyseed(
        aFile: File, password: String,
        mnemonic: String, offset: String
    ): Wallet {
        val walletHandle = recoveryWalletPolyseedJ(
            aFile.absolutePath, password,
            mnemonic, offset,
            networkType.value
        )
        val wallet = Wallet(walletHandle)
        manageWallet(wallet)
        return wallet
    }
    private fun recoveryWalletPolyseedJ(
        path: String, password: String,
        mnemonic: String, offset: String,
        networkType: Int
    ): Long = 1L
    fun createWalletWithKeys(
        aFile: File, password: String, language: String, restoreHeight: Long,
        addressString: String, viewKeyString: String, spendKeyString: String
    ): Wallet {
        val walletHandle = createWalletFromKeysJ(
            aFile.absolutePath, password,
            language, networkType.value, restoreHeight,
            addressString, viewKeyString, spendKeyString
        )
        val wallet = Wallet(walletHandle)
        manageWallet(wallet)
        return wallet
    }
    private fun createWalletFromKeysJ(
        path: String, password: String,
        language: String,
        networkType: Int,
        restoreHeight: Long,
        addressString: String,
        viewKeyString: String,
        spendKeyString: String
    ): Long = 1L
    fun createWalletFromDevice(
        aFile: File, password: String, restoreHeight: Long,
        deviceName: String
    ): Wallet {
        val walletHandle = createWalletFromDeviceJ(
            aFile.absolutePath, password,
            networkType.value, deviceName, restoreHeight,
            "5:20"
        )
        val wallet = Wallet(walletHandle)
        manageWallet(wallet)
        return wallet
    }
    private fun createWalletFromDeviceJ(
        path: String, password: String,
        networkType: Int,
        deviceName: String,
        restoreHeight: Long,
        subaddressLookahead: String
    ): Long = 1L
    fun closeJ(wallet: Wallet?): Boolean = true
    fun close(wallet: Wallet): Boolean {
        unmanageWallet(wallet)
        val closed = closeJ(wallet)
        if (!closed) {
            manageWallet(wallet)
        }
        return closed
    }
    fun walletExists(aFile: File): Boolean {
        return walletExists(aFile.absolutePath)
    }
    private fun walletExists(path: String?): Boolean = path?.let { File(it).exists() } ?: false
    fun verifyWalletPassword(
        keysFileName: String?,
        password: String?,
        watchOnly: Boolean
    ): Boolean = true
    fun verifyWalletPasswordOnly(keysFileName: String, password: String): Boolean {
        return queryWalletDeviceJ(keysFileName, password) >= 0
    }
    private fun queryWalletDeviceJ(keysFileName: String, password: String): Int = 0
    fun setDaemon(node: Node?) {
        if (node != null) {
            daemonAddress = node.address
            require(networkType === node.networkType) { "network type does not match" }
            daemonUsername = node.username
            daemonPassword = node.password
            daemonAddress?.let { addr -> setDaemonAddressJ(addr) }
            Timber.tag(TAG).i("setDaemon:  %s", daemonAddress)
        } else {
            daemonAddress = null
            daemonUsername = ""
            daemonPassword = ""
        }
    }
    fun getDaemonAddress(): String? {
        return daemonAddress
    }
    private fun setDaemonAddressJ(address: String) {}
    fun getDaemonVersion(): Int = 18
    fun getBlockchainHeight(): Long = 3_400_000L
    fun getBlockchainTargetHeight(): Long = 3_400_000L
    fun getNetworkDifficulty(): Long = 200_000_000_000L
    fun getMiningHashRate(): Double = 0.0
    fun getBlockTarget(): Long = 120L
    fun isMining(): Boolean = false
    fun startMining(
        address: String?,
        backgroundMining: Boolean,
        ignoreBattery: Boolean
    ): Boolean = true
    fun stopMining(): Boolean = true
    fun resolveOpenAlias(address: String?, dnssec_valid: Boolean): String? = null
    fun setProxy(address: String): Boolean {
        proxy = address
        return setProxyJ(address)
    }
    private fun setProxyJ(address: String?): Boolean = true
    inner class WalletInfo(wallet: File) : Comparable<WalletInfo> {
        private val path: File? = wallet.parentFile
        private val name: String = wallet.name
        override fun compareTo(other: WalletInfo): Int {
            return name.lowercase(Locale.getDefault())
                .compareTo(other.name.lowercase(Locale.getDefault()))
        }
    }
    companion object {
        var LOGLEVEL_SILENT = -1
        var LOGLEVEL_WARN = 0
        var LOGLEVEL_INFO = 1
        var LOGLEVEL_DEBUG = 2
        var LOGLEVEL_TRACE = 3
        var LOGLEVEL_MAX = 4
        @get:Synchronized
        var instance: WalletManager? = null
            get() {
                if (field == null) {
                    field = WalletManager()
                }
                return field
            }
            private set
        init {
            try { System.loadLibrary("anonero") } catch (e: Throwable) {}
        }
        fun addressPrefix(networkType: NetworkType): String {
            return when (networkType) {
                NetworkType.NetworkType_Testnet -> "9A-"
                NetworkType.NetworkType_Mainnet -> "4-"
                NetworkType.NetworkType_Stagenet -> "5-"
            }
        }
        fun resetInstance() {
            instance = null
        }
        @JvmStatic
        fun initLogger(argv0: String?, defaultLogBaseName: String?) {}
        @JvmStatic
        fun setLogLevel(level: Int) {}
        @JvmStatic
        fun logDebug(category: String?, message: String?) {}
        @JvmStatic
        fun logInfo(category: String?, message: String?) {}
        @JvmStatic
        fun logWarning(category: String?, message: String?) {}
        @JvmStatic
        fun logError(category: String?, message: String?) {}
        @JvmStatic
        fun moneroVersion(): String? = "0.18.3.4-ui-mock"
    }
}
