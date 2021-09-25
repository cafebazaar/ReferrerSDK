package ir.cafebazaar.referrersdk

import android.content.Context
import android.os.RemoteException
import ir.cafebazaar.referrersdk.communicators.ReferrerClientConnectionBroadcast
import ir.cafebazaar.referrersdk.communicators.ReferrerClientConnectionService
import ir.cafebazaar.referrersdk.communicators.ReferrerConnectionFunctions
import ir.cafebazaar.referrersdk.model.ReferrerDetails
import ir.cafebazaar.referrersdk.model.ReferrerDetailsImpl
import ir.cafebazaar.servicebase.Client
import ir.cafebazaar.servicebase.state.ClientStateListener
import ir.cafebazaar.servicebase.communicators.ClientConnectionCommunicator

internal class ReferrerClientImpl internal constructor(private val mApplicationContext: Context) : Client(),
    ReferrerClient {
    override val supportedClientVersion: Long
        get() = SUPPORTED_BAZAAR_CLIENT_VERSION

    override fun getConnectionsList(clientStateListener: ClientStateListener): List<ClientConnectionCommunicator> {
        return listOf<ClientConnectionCommunicator>(
            ReferrerClientConnectionService(
                mApplicationContext,
                this,
                clientStateListener
            ),
            ReferrerClientConnectionBroadcast(
                mApplicationContext,
                this,
                clientStateListener
            )
        )
    }

    override fun getReferrer(): ReferrerDetails? {
        return runIfReady {
            try {
                clientConnection?.toReferrerConnectionFunctions()?.referrer?.apply {
                    putString(KEY_PACKAGE_NAME, mApplicationContext.packageName)
                }?.also {
                    return@runIfReady ReferrerDetailsImpl(it)
                }
            } catch (exception: RemoteException) {
                updateClientState(DISCONNECTED)
                throw exception
            }
        }?.let { result ->
            return if (result is ReferrerDetails) {
                result
            } else {
                null
            }
        }
    }

    override fun consumeReferrer(installTime: Long) {
        runIfReady {
            try {
                clientConnection?.toReferrerConnectionFunctions()?.consumeReferrer(installTime)
            } catch (exception: RemoteException) {
                updateClientState(DISCONNECTED)
                throw exception
            }
        }
    }

    private fun ClientConnectionCommunicator.toReferrerConnectionFunctions(): ReferrerConnectionFunctions {
        return this as ReferrerConnectionFunctions
    }

    companion object {
        internal const val SUPPORTED_BAZAAR_CLIENT_VERSION = 2654456L
        internal const val KEY_PACKAGE_NAME = "package_name"
        internal const val SERVICE_PACKAGE_NAME = "com.farsitel.bazaar.dev"
        internal const val SERVICE_NAME =
            "com.farsitel.bazaar.referrerprovider.ReferrerProviderService"
        internal const val SERVICE_ACTION_NAME = "ir.cafebazaar.referrer.BIND"
    }
}