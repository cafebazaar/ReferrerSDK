package ir.cafebazaar.referrersdk.communicators

import android.content.Context
import android.content.Intent
import android.os.Bundle
import ir.cafebazaar.referrersdk.AbortableCountDownLatch
import ir.cafebazaar.referrersdk.ClientState
import ir.cafebazaar.referrersdk.ClientState.Companion.CONNECTED
import ir.cafebazaar.referrersdk.ClientState.Companion.DISCONNECTED
import ir.cafebazaar.referrersdk.ReferrerClient
import ir.cafebazaar.referrersdk.ReferrerClientImpl.Companion.SERVICE_PACKAGE_NAME
import ir.cafebazaar.referrersdk.ReferrerStateListener
import ir.cafebazaar.referrersdk.receiver.ReferrerReceiver
import ir.cafebazaar.referrersdk.receiver.ReferrerReceiverCommunicator
import kotlinx.coroutines.*
import java.lang.Exception
import java.util.concurrent.TimeUnit

class ReferrerClientConnectionBroadcast(
    override var context: Context,
    override var clientState: ClientState,
    override var stateListener: ReferrerStateListener
) : ReferrerClientConnectionCommunicator, ReferrerReceiverCommunicator {

    private var coroutineScope: CoroutineScope? = null
    private val abortableCountDownLatch = AbortableCountDownLatch(ABORTABLE_COUNT_DOWN_LATCH_COUNT)
    private var referrerResponseBundle: Bundle? = null
    override val referrerBundle: Bundle?
        get() {
            return if (referrerResponseBundle?.isEmpty == true) {
                null
            } else {
                referrerResponseBundle
            }
        }

    override fun consumeReferrer(installTime: Long) {
        getNewIntentForBroadcast().apply {
            action = ACTION_REFERRER_CONSUME
            putExtra(KEY_INSTALL_TIME, installTime)
        }.run(::sendBroadcast)
    }

    override fun startConnection(): Boolean {
        coroutineScope?.cancel()
        coroutineScope = CoroutineScope(Dispatchers.Main)
        ReferrerReceiver.addObserver(this)
        getNewIntentForBroadcast().apply {
            action = ACTION_REFERRER_GET
        }.run(::sendBroadcast)
        try {
            abortableCountDownLatch.await(
                ABORTABLE_COUNT_DOWN_LATCH_COUNT_TIMEOUT_SECONDS,
                TimeUnit.SECONDS
            )
        } catch (exception: Exception) {
        }
        return (referrerResponseBundle != null).apply {
            if (this) {
                coroutineScope?.launch {
                    delay(DELAY_BEFORE_UPDATE_STATE_MILLI_SECONDS)
                    clientState.updateState(CONNECTED)
                    stateListener.onReferrerSetupFinished(ReferrerClient.OK)
                }
            }
        }
    }

    override fun stopConnection() {
        coroutineScope?.cancel()
        ReferrerReceiver.removeObserver(this)
        clientState.updateState(DISCONNECTED)
    }

    private fun getNewIntentForBroadcast(): Intent {
        val bundle = Bundle().apply {
            putString(KEY_PACKAGE_NAME, context.packageName)
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
            ACTION_REFERRER_GET -> handleReferrerGetAction(intent)
        }
    }

    private fun handleReferrerGetAction(intent: Intent) {
        referrerResponseBundle =
            intent.getBundleExtra(KEY_RESPONSE) ?: Bundle()
        abortableCountDownLatch.countDown()
    }

    companion object {
        private const val ABORTABLE_COUNT_DOWN_LATCH_COUNT = 1
        private const val ABORTABLE_COUNT_DOWN_LATCH_COUNT_TIMEOUT_SECONDS = 5L
        private const val DELAY_BEFORE_UPDATE_STATE_MILLI_SECONDS = 1000L
        private const val BAZAAR_BASE_ACTION = "com.farsitel.bazaar.referrer."
        private const val ACTION_REFERRER_GET = "${BAZAAR_BASE_ACTION}get"
        private const val ACTION_REFERRER_CONSUME = "${BAZAAR_BASE_ACTION}consume"
        private const val KEY_PACKAGE_NAME = "packageName"
        private const val KEY_INSTALL_TIME = "installTime"
        private const val KEY_RESPONSE = "response"
    }
}