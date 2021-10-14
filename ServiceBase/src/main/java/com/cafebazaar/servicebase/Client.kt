package com.cafebazaar.servicebase

import android.content.Context
import android.content.pm.PackageInfo
import android.os.Build
import android.os.Looper
import com.cafebazaar.servicebase.communicator.ClientConnectionCommunicator
import com.cafebazaar.servicebase.communicator.ClientReceiverCommunicator
import com.cafebazaar.servicebase.receiver.ClientReceiver
import com.cafebazaar.servicebase.state.ClientError
import com.cafebazaar.servicebase.state.ClientStateListener

abstract class Client(private val context: Context) {

    private var clientStateListener: ClientStateListener? = null

    protected abstract val supportedClientVersion: Long
    protected abstract fun getConnectionsList(): List<ClientConnectionCommunicator>?

    @Volatile private var clientState = DISCONNECTED
    @Volatile protected var clientConnection: ClientConnectionCommunicator? = null

    private val isReady: Boolean
        get() = (clientState == CONNECTED)
            .and(clientConnection != null)

    fun startConnection(clientStateListener: ClientStateListener) {
        this.clientStateListener = clientStateListener
        if (handleStateForBazaarIsNotInstalled(clientStateListener)) return
        if (handleStateForIncompatbleBazaarVersion(clientStateListener)) return
        throwExceptionIfRunningOnMainThread()
        handleStartingConnection(clientStateListener)
    }

    private fun handleStartingConnection(clientStateListener: ClientStateListener) {
        if (isReady.not()) {
            if (isConnecting(clientStateListener)) return
            tryToConnect(clientStateListener)
        } else {
            clientStateListener.onReady()
        }
    }

    private fun tryToConnect(clientStateListener: ClientStateListener) {
        clientState = CONNECTING
        getConnectionsList()?.forEach { connection ->
            if (connection is ClientReceiverCommunicator) {
                ClientReceiver.addObserver(connection)
            }
            if (connection.startConnection()) {
                clientConnection = connection
                clientState = CONNECTED
                clientStateListener.onReady()
                return
            }
        }
        clientState = DISCONNECTED
        clientStateListener.onError(
            ClientError.ServiceUnAvailable("SDK Could Not Connect")
        )
    }

    private fun isConnecting(clientStateListener: ClientStateListener): Boolean {
        if (clientState == CONNECTING) {
            clientStateListener.onError(
                ClientError.DeveloperError("SDK Is Started")
            )
            return true
        }
        return false
    }

    private fun throwExceptionIfRunningOnMainThread() {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            throw IllegalThreadStateException(OFF_MAIN_THREAD_EXCEPTION)
        }
    }

    private fun handleStateForIncompatbleBazaarVersion(
        clientStateListener: ClientStateListener
    ): Boolean {
        if (getBazaarVersionCode() < supportedClientVersion) {
            clientState = DISCONNECTED
            clientStateListener.onError(
                ClientError.ServiceUnAvailable("Bazaar Client Is Not Compatible")
            )
            return true
        }
        return false
    }

    private fun getBazaarVersionCode() = getPackageInfo(context)?.let {
        sdkAwareVersionCode(it)
    } ?: 0L

    private fun handleStateForBazaarIsNotInstalled(clientStateListener: ClientStateListener): Boolean {
        if (verifyBazaarIsInstalled(context).not()) {
            clientState = DISCONNECTED
            clientStateListener.onError(
                ClientError.ServiceUnAvailable("Bazaar Client Is Not Installed")
            )
            return true
        }
        return false
    }

    fun endConnection() {
        clientStateListener = null
        clientConnection?.let { connection ->
            if (connection is ClientReceiverCommunicator) {
                ClientReceiver.removeObserver(connection)
            }
            connection.stopConnection()
        }
        clientState = DISCONNECTED
    }

    protected fun errorOccurred(errorMessage: String) {
        clientStateListener?.onError(ClientError.RunTime(errorMessage))
    }

    protected fun runIfReady(block: () -> Any?): Any? {
        if (isReady.not()) {
            throw IllegalStateException(SERVICE_IS_NOT_STARTED_EXCEPTION)
        } else {
            return block.invoke()
        }
    }

    private fun verifyBazaarIsInstalled(context: Context): Boolean {
        return getPackageInfo(context) != null
    }

    private fun getPackageInfo(context: Context): PackageInfo? {
        return try {
            val packageManager = context.packageManager
            packageManager.getPackageInfo(SERVICE_PACKAGE_NAME, 0)
        } catch (ignored: Exception) {
            null
        }
    }

    private fun sdkAwareVersionCode(packageInfo: PackageInfo): Long {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            packageInfo.longVersionCode
        } else {
            packageInfo.versionCode.toLong()
        }
    }

    companion object {
        const val SERVICE_PACKAGE_NAME = "com.farsitel.bazaar"

        private const val OFF_MAIN_THREAD_EXCEPTION = "This function has to call off the main thread."
        private const val SERVICE_IS_NOT_STARTED_EXCEPTION =
            "Service not connected. Please start a connection before using the service."
        private const val DISCONNECTED = 0
        private const val CONNECTING = 1
        private const val CONNECTED = 2
    }
}