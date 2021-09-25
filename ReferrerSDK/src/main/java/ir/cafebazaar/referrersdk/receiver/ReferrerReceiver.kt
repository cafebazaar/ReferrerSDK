package ir.cafebazaar.referrersdk.receiver

import android.content.Context
import android.content.Intent
import ir.cafebazaar.servicebase.receiver.ClientReceiver

internal class ReferrerReceiver : ClientReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Intent().apply {
            action = intent!!.action
            intent.extras?.let { bundle ->
                putExtras(bundle)
            }
        }.run {
            notifyObservers(this)
        }
    }
}