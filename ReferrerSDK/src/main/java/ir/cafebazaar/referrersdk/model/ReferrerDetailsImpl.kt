package ir.cafebazaar.referrersdk.model

import android.os.Bundle

internal class ReferrerDetailsImpl(private val mOriginalBundle: Bundle) : ReferrerDetails {
    override val referrer: String?
        get() = mOriginalBundle.getString(KEY_INSTALL_REFERRER)
    override val referrerClickTimestampMilliseconds: Long
        get() = mOriginalBundle.getLong(KEY_REFERRER_CLICK_TIMESTAMP)
    override val installBeginTimestampMilliseconds: Long
        get() = mOriginalBundle.getLong(KEY_INSTALL_BEGIN_TIMESTAMP)
    override val appVersion: String?
        get() = mOriginalBundle.getString(KEY_APP_VERSION)

    companion object {
        private const val KEY_INSTALL_REFERRER = "install_referrer"
        private const val KEY_REFERRER_CLICK_TIMESTAMP = "referrer_click_timestamp_milliseconds"
        private const val KEY_INSTALL_BEGIN_TIMESTAMP = "install_begin_timestamp_milliseconds"
        private const val KEY_APP_VERSION = "app_version"
    }
}