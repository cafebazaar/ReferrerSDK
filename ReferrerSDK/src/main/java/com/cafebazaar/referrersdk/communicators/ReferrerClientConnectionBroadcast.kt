package com.cafebazaar.referrersdk.communicators

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.cafebazaar.servicebase.communicator.ClientConnectionCommunicator
import com.cafebazaar.servicebase.communicator.ClientReceiverCommunicator
import com.cafebazaar.servicebase.Client.Companion.SERVICE_PACKAGE_NAME
import java.lang.Exception
import java.util.concurrent.TimeUnit

internal class ReferrerClientConnectionBroadcast(
    private val context: Context
) : ClientConnectionCommunicator, ClientReceiverCommunicator, ReferrerConnectionFunctions {

    private val abortableCountDownLatch = AbortableCountDownLatch(ABORTABLE_COUNT_DOWN_LATCH_COUNT)
    private var referrerResponseBundle: Bundle? = null

    override val referrerBundle: Bundle?
        get() {
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
        return true
    }

    override fun stopConnection() { }

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

    private companion object {
        const val ABORTABLE_COUNT_DOWN_LATCH_COUNT = 1
        const val ABORTABLE_COUNT_DOWN_LATCH_COUNT_TIMEOUT_SECONDS = 5L
        const val BAZAAR_BASE_ACTION = "com.farsitel.bazaar.referrer."
        const val ACTION_REFERRER_GET = "${BAZAAR_BASE_ACTION}get"
        const val ACTION_REFERRER_CONSUME = "${BAZAAR_BASE_ACTION}consume"
        const val KEY_PACKAGE_NAME = "packageName"
        const val KEY_INSTALL_TIME = "installTime"
        const val KEY_RESPONSE = "response"
    }
}