package ir.cafebazaar.referrersdk

import android.content.Context

abstract class ReferrerClient: ClientState() {
    /**
     * Establishes the connection between SDK and Bazaar client.
     * @param stateListener A listener which user can use it to get the state of the connection.
     * @throws Exception You have to call this function off the main thread
     * otherwise it throws an exception.
     */
    abstract suspend fun startConnection(stateListener: ReferrerStateListener)
    abstract fun endConnection()
    abstract val isReady: Boolean
    abstract val referrer: ReferrerDetails?
    abstract fun consumeReferrer(installTime: Long)

    class Builder internal constructor(private val context: Context) {
        fun build(): ReferrerClient {
            return ReferrerClientImpl(context)
        }
    }

    companion object {
        fun newBuilder(context: Context): Builder {
            return Builder(context)
        }
    }
}