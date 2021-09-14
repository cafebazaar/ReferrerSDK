package ir.cafebazaar.referrersdk

import android.content.Context
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

abstract class ReferrerClient: ClientState() {
    abstract fun startConnection(stateListener: ReferrerStateListener)
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
        const val FEATURE_NOT_SUPPORTED = 2
        const val DEVELOPER_ERROR = 3
        fun newBuilder(ctx: Context): Builder {
            return Builder(ctx)
        }
    }
}