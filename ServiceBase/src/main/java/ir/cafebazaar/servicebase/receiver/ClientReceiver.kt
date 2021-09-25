package ir.cafebazaar.servicebase.receiver

import android.content.BroadcastReceiver
import android.content.Intent
import ir.cafebazaar.servicebase.communicators.ClientReceiverCommunicator

abstract class ClientReceiver: BroadcastReceiver() {

    protected fun notifyObservers(intent: Intent) {
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