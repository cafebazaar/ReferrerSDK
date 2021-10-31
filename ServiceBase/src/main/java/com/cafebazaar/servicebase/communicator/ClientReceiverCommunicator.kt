package com.cafebazaar.servicebase.communicator

import android.content.Intent

interface ClientReceiverCommunicator {

    fun onNewBroadcastReceived(intent: Intent?)
}