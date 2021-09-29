package ir.cafebazaar.referrersdk.communicators

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.ResolveInfo
import android.os.Bundle
import android.os.IBinder
import com.farsitel.bazaar.referrerprovider.ReferrerProviderService
import ir.cafebazaar.referrersdk.*
import ir.cafebazaar.referrersdk.ClientState.Companion.CONNECTED
import ir.cafebazaar.referrersdk.ClientState.Companion.DISCONNECTED
import ir.cafebazaar.referrersdk.ReferrerClientImpl

internal class ReferrerClientConnectionService(
    override val context: Context,
    override val clientState: ClientState,
    override val stateListener: ReferrerStateListener
) : ReferrerClientConnectionCommunicator, ServiceConnection {

    private var service: ReferrerProviderService? = null

    override val referrerBundle: Bundle?
        get() = service?.getReferrer(context.packageName)

    override fun consumeReferrer(installTime: Long) {
        service?.consumeReferrer(context.packageName, installTime)
    }

    override suspend fun startConnection(): Boolean {
        val serviceIntent = getServiceIntent()
        getResovleInfo(serviceIntent)?.let { resolvedServiceInfo ->
            resolvedServiceInfo.serviceInfo?.let { serviceInfo ->
                if (isPackageNameValid(serviceInfo.packageName, serviceInfo.name)) {
                    return bindService(serviceIntent)
                }
            }
        }
        return false
    }

    private fun getResovleInfo(serviceIntent: Intent): ResolveInfo? {
        val resolvedServices: List<*> =
            context.packageManager.queryIntentServices(serviceIntent, 0)
        if (resolvedServices.isNullOrEmpty().not()) {
            return resolvedServices.first() as ResolveInfo
        }
        return null
    }

    private fun isPackageNameValid(packageName: String?, name: String?) =
        (ReferrerClientImpl.SERVICE_PACKAGE_NAME == packageName).and(name != null)

    private fun bindService(serviceIntent: Intent): Boolean {
        if (context.bindService(
                serviceIntent,
                this,
                Context.BIND_AUTO_CREATE
            )
        ) {
            return true
        }
        return false
    }

    private fun getServiceIntent(): Intent {
        val serviceIntent = Intent(ReferrerClientImpl.SERVICE_ACTION_NAME)
        val componentName = ComponentName(
            ReferrerClientImpl.SERVICE_PACKAGE_NAME,
            ReferrerClientImpl.SERVICE_NAME
        )
        serviceIntent.component = componentName
        return serviceIntent
    }

    override fun stopConnection() {
        context.unbindService(this)
    }

    override fun onServiceConnected(name: ComponentName?, iBinder: IBinder?) {
        service = ReferrerProviderService.Stub.asInterface(iBinder)
        clientState.updateState(CONNECTED)
        stateListener.onReferrerSetupFinished(ReferrerSDKStates.Ok)
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        service = null
        clientState.updateState(DISCONNECTED)
        stateListener.onReferrerServiceDisconnected()
    }
}