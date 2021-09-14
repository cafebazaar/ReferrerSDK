package ir.cafebazaar.referrersdk

import android.content.Context
import android.content.pm.PackageManager
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

    override fun startConnection(stateListener: ReferrerStateListener) {
        if (isReady.not()) {
            when {
                clientState == CONNECTING -> {
                    stateListener.onReferrerSetupFinished(DEVELOPER_ERROR)
                }
                clientState != CLOSED -> {
                    when {
                        isCafeBazaarCompatible().not() -> {
                            clientState = DISCONNECTED
                            stateListener.onReferrerSetupFinished(SERVICE_UNAVAILABLE)
                        }
                        tryToConnect(stateListener) -> return
                        else -> {
                            clientState = DISCONNECTED
                            stateListener.onReferrerSetupFinished(FEATURE_NOT_SUPPORTED)
                        }
                    }
                }
                else -> {
                    stateListener.onReferrerSetupFinished(DEVELOPER_ERROR)
                }
            }
        } else {
            stateListener.onReferrerSetupFinished(OK)
        }
    }

    private fun tryToConnect(stateListener: ReferrerStateListener): Boolean {
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
            throw IllegalStateException("Service not connected. Please start a connection before using the service.")
        } else {
            try {
                referrerClientConnection?.referrer?.apply {
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
            throw IllegalStateException("Service not connected. Please start a connection before using the service.")
        } else {
            try {
                referrerClientConnection?.consumeReferrer(installTime)
            } catch (exception: RemoteException) {
                clientState = DISCONNECTED
                throw exception
            }
        }
    }

    private fun isCafeBazaarCompatible(): Boolean {
        return try {
            mApplicationContext.getPackageInfo(SERVICE_PACKAGE_NAME)
                ?.sdkAwareVersionCode()?.let { versionCode ->
                    versionCode >= CAFE_BAZAAR_MIN_APP_VER
                } ?: false
        } catch (exception: PackageManager.NameNotFoundException) {
            false
        }
    }

    companion object {
        private const val CAFE_BAZAAR_MIN_APP_VER = 80837300
        internal const val KEY_PACKAGE_NAME = "package_name"
        internal const val SERVICE_PACKAGE_NAME = "com.farsitel.bazaar.referrerprovider"
        internal const val SERVICE_NAME =
            "com.farsitel.bazaar.referrerprovider.ReferrerProviderService"
        internal const val SERVICE_ACTION_NAME = "ir.cafebazaar.referrer.BIND"
    }

    override fun updateState(state: Int) {
        clientState = state
    }
}