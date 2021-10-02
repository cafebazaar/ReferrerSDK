package ir.cafebazaar.referrersdk

import android.content.Context
import android.os.Looper
import android.os.RemoteException
import ir.cafebazaar.referrersdk.communicators.ReferrerClientConnectionBroadcast
import ir.cafebazaar.referrersdk.communicators.ReferrerClientConnectionCommunicator
import ir.cafebazaar.referrersdk.communicators.ReferrerClientConnectionService

internal class ReferrerClientImpl(private val mApplicationContext: Context) : ReferrerClient() {
    @Volatile
    private var clientState = DISCONNECTED

    @Volatile
    private var referrerClientConnection: ReferrerClientConnectionCommunicator? = null

    override val isReady: Boolean
        get() = (clientState == CONNECTED).and(referrerClientConnection != null)

    override suspend fun startConnection(stateListener: ReferrerStateListener) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            throw IllegalThreadStateException(OFF_MAIN_THREAD_EXCEPTION)
        }
        if (isReady.not()) {
            when {
                clientState == CONNECTING -> {
                    stateListener.onReferrerSetupFinished(
                        ReferrerSDKStates.DeveloperError("It is connecting now")
                    )
                }
                clientState != CLOSED -> {
                    when {
                        tryToConnect(stateListener) -> return
                        else -> {
                            clientState = DISCONNECTED
                            stateListener.onReferrerSetupFinished(
                                ReferrerSDKStates.ServiceUnAvailable
                            )
                        }
                    }
                }
                else -> {
                    stateListener.onReferrerSetupFinished(
                        ReferrerSDKStates.DeveloperError("Connection is closed")
                    )
                }
            }
        } else {
            stateListener.onReferrerSetupFinished(
                ReferrerSDKStates.Ok
            )
        }
    }

    private suspend fun tryToConnect(stateListener: ReferrerStateListener): Boolean {
        clientState = CONNECTING
        ReferrerClientConnectionService(mApplicationContext, this, stateListener).also { connection ->
            if (connection.startConnection()) {
                referrerClientConnection = connection
                return true
            }
        }
        ReferrerClientConnectionBroadcast(mApplicationContext, this, stateListener).also { connection ->
            if (connection.startConnection()) {
                referrerClientConnection = connection
                return true
            }
        }
        return false
    }

    override fun endConnection() {
        clientState = CLOSED
        referrerClientConnection?.stopConnection()
    }

    override val referrer: ReferrerDetails?
        get() = if (isReady.not()) {
            throw IllegalStateException(SERVICE_IS_NOT_STARTED_EXCEPTION)
        } else {
            try {
                referrerClientConnection?.referrerBundle?.apply {
                    putString(KEY_PACKAGE_NAME, mApplicationContext.packageName)
                }?.run {
                    return@run ReferrerDetails(this)
                }
            } catch (exception: RemoteException) {
                clientState = DISCONNECTED
                throw exception
            }
        }

    override fun consumeReferrer(installTime: Long) {
        if (isReady.not()) {
            throw IllegalStateException(SERVICE_IS_NOT_STARTED_EXCEPTION)
        } else {
            try {
                referrerClientConnection?.consumeReferrer(installTime)
            } catch (exception: RemoteException) {
                clientState = DISCONNECTED
                throw exception
            }
        }
    }

    override fun updateState(state: Int) {
        clientState = state
    }

    companion object {
        internal const val KEY_PACKAGE_NAME = "package_name"
        internal const val SERVICE_PACKAGE_NAME = "com.farsitel.bazaar"
        internal const val SERVICE_NAME =
            "com.farsitel.bazaar.referrerprovider.ReferrerProviderService"
        internal const val SERVICE_ACTION_NAME = "com.cafebazaar.referrer.BIND"
        private const val OFF_MAIN_THREAD_EXCEPTION = "This function has to call off the main thread."
        private const val SERVICE_IS_NOT_STARTED_EXCEPTION = "Service not connected. Please start a connection before using the service."
    }
}