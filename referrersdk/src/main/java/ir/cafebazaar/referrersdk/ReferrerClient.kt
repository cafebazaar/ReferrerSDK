package ir.cafebazaar.referrersdk

import android.content.Context

abstract class ReferrerClient: ClientState() {
    abstract suspend fun startConnection(stateListener: ReferrerStateListener)
    abstract fun endConnection()
    abstract val isReady: Boolean
    abstract val referrer: ReferrerDetails?
    abstract fun consumeReferrer(installTime: Long)

    class Builder internal constructor(private val mContext: Context) {
        fun build(): ReferrerClient {
            return ReferrerClientImpl(mContext)
        }
    }

    companion object {
        const val OK = 0
        const val SERVICE_UNAVAILABLE = 1
        const val DEVELOPER_ERROR = 2
        fun newBuilder(context: Context): Builder {
            return Builder(context)
        }
    }
}