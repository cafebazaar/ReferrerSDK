package ir.cafebazaar.referrersdk.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

internal class ReferrerReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Intent().apply {
            action = intent!!.action + ".iab"
            intent.extras?.let { bundle ->
                putExtras(bundle)
            }
        }.run {
            notifyObservers(this)
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
        private val observers = mutableListOf<ReferrerReceiverCommunicator>()

        fun addObserver(communicator: ReferrerReceiverCommunicator) {
            synchronized(observerLock) {
                observers.add(communicator)
            }
        }

        fun removeObserver(communicator: ReferrerReceiverCommunicator) {
            synchronized(observerLock) {
                observers.remove(communicator)
            }
        }
    }
}