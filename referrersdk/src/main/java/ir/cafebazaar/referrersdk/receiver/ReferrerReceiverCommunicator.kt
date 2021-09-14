package ir.cafebazaar.referrersdk.receiver

import android.content.Intent

internal interface ReferrerReceiverCommunicator {
    fun onNewBroadcastReceived(intent: Intent?)
}