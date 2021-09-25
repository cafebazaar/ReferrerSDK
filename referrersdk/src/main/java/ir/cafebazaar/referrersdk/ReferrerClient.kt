package ir.cafebazaar.referrersdk

import android.content.Context
import ir.cafebazaar.referrersdk.model.ReferrerDetails
import ir.cafebazaar.servicebase.state.ClientStateListener

interface ReferrerClient {
    fun startConnection(clientStateListener: ClientStateListener)
    fun endConnection()
    fun getReferrer(): ReferrerDetails?
    fun consumeReferrer(installTime: Long)
    companion object {
        fun getClient(context: Context): ReferrerClient {
            return ReferrerClientImpl(context)
        }
    }
}