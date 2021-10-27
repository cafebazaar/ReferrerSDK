package com.cafebazaar.servicebase.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.cafebazaar.servicebase.communicator.ClientReceiverCommunicator

class ClientReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            notifyObservers(it)
        }
    }

    private fun notifyObservers(intent: Intent) {
        synchronized(observerLock) {
            for (observer in observers) {
                observer.onNewBroadcastReceived(intent)
            }
        }
    }

    companion object {

        private val observerLock = Any()
        private val observers = mutableListOf<ClientReceiverCommunicator>()

        fun addObserver(communicator: ClientReceiverCommunicator) {
            synchronized(observerLock) {
                observers.add(communicator)
            }
        }

        fun removeObserver(communicator: ClientReceiverCommunicator) {
            synchronized(observerLock) {
                observers.remove(communicator)
            }
        }
    }
}