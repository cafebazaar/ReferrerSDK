package ir.cafebazaar.referrersdk.communicators

import android.content.Context
import android.content.Intent
import android.os.Bundle
import ir.cafebazaar.referrersdk.ClientState
import ir.cafebazaar.referrersdk.ClientState.Companion.CONNECTED
import ir.cafebazaar.referrersdk.ClientState.Companion.DISCONNECTED
import ir.cafebazaar.referrersdk.ReferrerClient
import ir.cafebazaar.referrersdk.ReferrerClientImpl.Companion.SERVICE_PACKAGE_NAME
import ir.cafebazaar.referrersdk.ReferrerStateListener
import ir.cafebazaar.referrersdk.receiver.ReferrerReceiver
import ir.cafebazaar.referrersdk.receiver.ReferrerReceiverCommunicator

class ReferrerClientConnectionBroadcast(
    override var context: Context,
    override var clientState: ClientState,
    override var stateListener: ReferrerStateListener
) : ReferrerClientConnectionCommunicator, ReferrerReceiverCommunicator {
    private var referrerResponse: Bundle? = null
    override val referrer: Bundle?
        get() {
            return referrerResponse
        }

    override fun consumeReferrer(installTime: Long) {
        getNewIntentForBroadcast().apply {
            action = ReferrerBroadcast.ACTION_REFERRER_CONSUME
            putExtra(ReferrerBroadcast.KEY_INSTALL_TIME, installTime)
        }.run(::sendBroadcast)
    }

    override fun startConnection(): Boolean {
        ReferrerReceiver.addObserver(this)
        getNewIntentForBroadcast().apply {
            action = ReferrerBroadcast.ACTION_REFERRER_GET
        }.run(::sendBroadcast)
        return true
    }

    override fun stopConnection() {
        ReferrerReceiver.removeObserver(this)
        clientState.updateState(DISCONNECTED)
    }

    private fun getNewIntentForBroadcast(): Intent {
        val bundle = Bundle().apply {
            putString(ReferrerBroadcast.KEY_PACKAGE_NAME, context.packageName)
        }
        return Intent().apply {
            `package` = SERVICE_PACKAGE_NAME
            putExtras(bundle)
        }
    }

    private fun sendBroadcast(intent: Intent) {
        context.sendBroadcast(intent)
    }

    override fun onNewBroadcastReceived(intent: Intent?) {
        when (intent?.action) {
            ReferrerBroadcast.ACTION_REFERRER_GET -> {
                referrerResponse =
                    intent.getBundleExtra(ReferrerBroadcast.KEY_RESPONSE)
                clientState.updateState(CONNECTED)
                stateListener.onReferrerSetupFinished(ReferrerClient.OK)
            }
        }
    }
}