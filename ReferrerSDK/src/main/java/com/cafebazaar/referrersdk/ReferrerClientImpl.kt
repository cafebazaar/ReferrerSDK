package com.cafebazaar.referrersdk

import android.app.Application
import android.os.RemoteException
import com.cafebazaar.servicebase.Client
import com.cafebazaar.servicebase.communicator.ClientConnectionCommunicator
import com.cafebazaar.referrersdk.communicators.ReferrerClientConnectionBroadcast
import com.cafebazaar.referrersdk.communicators.ReferrerClientConnectionService
import com.cafebazaar.referrersdk.communicators.ReferrerConnectionFunctions
import com.cafebazaar.referrersdk.model.ReferrerDetails
import com.cafebazaar.referrersdk.model.ReferrerDetailsImpl

internal class ReferrerClientImpl(private val applicationContext: Application) : Client(applicationContext),
    ReferrerClient {

    override val supportedClientVersion: Long
        get() = SUPPORTED_BAZAAR_CLIENT_VERSION

    override fun getConnectionsList(): List<ClientConnectionCommunicator> {
        return listOf<ClientConnectionCommunicator>(
            ReferrerClientConnectionService(
                applicationContext
            ),
            ReferrerClientConnectionBroadcast(
                applicationContext
            )
        )
    }

    override fun getReferrerDetails(): ReferrerDetails? {
        return runIfReady {
            try {
                clientConnection?.toReferrerConnectionFunctions()?.referrerBundle?.apply {
                    putString(KEY_PACKAGE_NAME, applicationContext.packageName)
                }?.also {
                    return@runIfReady ReferrerDetailsImpl(it)
                }
            } catch (exception: RemoteException) {
                errorOccurred("Error during getting referrer details")
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
                errorOccurred("Error during consuming referrer")
                throw exception
            }
        }
    }

    private fun ClientConnectionCommunicator.toReferrerConnectionFunctions(): ReferrerConnectionFunctions {
        return this as ReferrerConnectionFunctions
    }

    companion object {
        internal const val SUPPORTED_BAZAAR_CLIENT_VERSION = 1400600L
        internal const val KEY_PACKAGE_NAME = "package_name"
        internal const val SERVICE_NAME =
            "com.farsitel.bazaar.referrerprovider.ReferrerProviderService"
        internal const val SERVICE_ACTION_NAME = "com.cafebazaar.referrer.BIND"
    }
}