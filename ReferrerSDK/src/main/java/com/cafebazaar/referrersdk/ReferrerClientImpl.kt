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
import com.cafebazaar.servicebase.state.ClientError

internal class ReferrerClientImpl(private val applicationContext: Application) : Client(applicationContext),
    ReferrerClient {

    override val supportedClientVersion: Long
        get() = SUPPORTED_BAZAAR_CLIENT_VERSION

    override fun getServiceConnection(): ClientConnectionCommunicator {
        return ReferrerClientConnectionService(
            applicationContext
        )
    }

    override fun getBroadcastConnections(): ClientConnectionCommunicator {
        return ReferrerClientConnectionBroadcast(
            applicationContext
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
                errorOccurred(
                    ClientError.ERROR_DURING_GETTING_REFERRER_DETAILS
                )
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
                errorOccurred(
                    ClientError.ERROR_DURING_CONSUMING_REFERRER
                )
                throw exception
            }
        }
    }

    private fun ClientConnectionCommunicator.toReferrerConnectionFunctions(): ReferrerConnectionFunctions {
        return this as ReferrerConnectionFunctions
    }

    internal companion object {
        const val SUPPORTED_BAZAAR_CLIENT_VERSION = 1400600L
        const val KEY_PACKAGE_NAME = "package_name"
        const val SERVICE_NAME =
            "com.farsitel.bazaar.referrerprovider.ReferrerProviderServiceImpl"
        const val SERVICE_ACTION_NAME = "com.cafebazaar.referrer.BIND"
    }
}