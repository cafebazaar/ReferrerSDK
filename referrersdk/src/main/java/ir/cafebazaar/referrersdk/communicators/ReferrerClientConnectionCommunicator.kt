package ir.cafebazaar.referrersdk.communicators

import android.content.Context
import android.os.Bundle
import ir.cafebazaar.referrersdk.ClientState
import ir.cafebazaar.referrersdk.ReferrerStateListener

internal interface ReferrerClientConnectionCommunicator {
    var context: Context
    var clientState: ClientState
    var stateListener: ReferrerStateListener
    val referrerBundle: Bundle?
    fun consumeReferrer(installTime: Long)
    fun startConnection(): Boolean
    fun stopConnection()
}