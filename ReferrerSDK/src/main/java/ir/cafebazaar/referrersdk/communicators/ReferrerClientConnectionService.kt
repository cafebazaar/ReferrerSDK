package ir.cafebazaar.referrersdk.communicators

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.ResolveInfo
import android.os.Bundle
import android.os.IBinder
import com.farsitel.bazaar.referrerprovider.IReferrerProviderService
import ir.cafebazaar.servicebase.state.ClientState
import ir.cafebazaar.servicebase.state.ClientState.Companion.CONNECTED
import ir.cafebazaar.servicebase.state.ClientState.Companion.DISCONNECTED
import ir.cafebazaar.servicebase.Client
import ir.cafebazaar.referrersdk.ReferrerClientImpl
import ir.cafebazaar.servicebase.state.ClientStateListener
import ir.cafebazaar.servicebase.communicators.ClientConnectionCommunicator

internal class ReferrerClientConnectionService(
    var context: Context,
    override var clientState: ClientState,
    override var clientStateListener: ClientStateListener
) : ClientConnectionCommunicator, ReferrerConnectionFunctions {

    private var referrerServiceConnection: ReferrerServiceConnection? = null
    private var service: IReferrerProviderService? = null

    override val referrer: Bundle?
        get() = service?.getReferrer(context.packageName)

    override fun consumeReferrer(installTime: Long) {
        service?.consumeReferrer(context.packageName, installTime)
    }

    override fun startConnection(): Boolean {
        val serviceIntent = Intent(ReferrerClientImpl.SERVICE_ACTION_NAME)
        val componentName = ComponentName(
            ReferrerClientImpl.SERVICE_PACKAGE_NAME,
            ReferrerClientImpl.SERVICE_NAME
        )
        serviceIntent.component = componentName
        val resolvedServices: List<*> =
            context.packageManager.queryIntentServices(serviceIntent, 0)
        if (resolvedServices.isNullOrEmpty().not()) {
            val resolveInfo = resolvedServices.first() as ResolveInfo
            resolveInfo.serviceInfo?.let {
                val serviceInfo = resolveInfo.serviceInfo
                val packageName = serviceInfo.packageName
                val name = resolveInfo.serviceInfo.name
                if ((ReferrerClientImpl.SERVICE_PACKAGE_NAME == packageName).and(name != null)) {
                    ReferrerServiceConnection(clientStateListener).also { referrerServiceConnection ->
                        this.referrerServiceConnection = referrerServiceConnection
                        if (context.bindService(
                                serviceIntent,
                                referrerServiceConnection,
                                Context.BIND_AUTO_CREATE
                            )
                        ) {
                            return true
                        }
                    }
                }
            }
        }
        return false
    }

    override fun stopConnection() {
        referrerServiceConnection?.let { referrerServiceConnection ->
            context.unbindService(referrerServiceConnection)
            this.referrerServiceConnection = null
        }
    }


    inner class ReferrerServiceConnection internal constructor(private val clientStateListener: ClientStateListener) :
        ServiceConnection {
        override fun onServiceConnected(componentName: ComponentName, iBinder: IBinder) {
            service = IReferrerProviderService.Stub.asInterface(iBinder)
            clientState.updateClientState(CONNECTED)
            clientStateListener.onSetupFinished(Client.OK)
        }

        override fun onServiceDisconnected(var1: ComponentName) {
            service = null
            clientState.updateClientState(DISCONNECTED)
            clientStateListener.onServiceDisconnected()
        }
    }
}