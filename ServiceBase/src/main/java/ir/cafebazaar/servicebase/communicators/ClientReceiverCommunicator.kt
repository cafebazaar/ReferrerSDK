package ir.cafebazaar.servicebase.communicators

import android.content.Intent

interface ClientReceiverCommunicator {
    fun onNewBroadcastReceived(intent: Intent?)
}