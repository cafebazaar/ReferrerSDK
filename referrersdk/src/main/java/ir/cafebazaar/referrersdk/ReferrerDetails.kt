package ir.cafebazaar.referrersdk

import android.os.Bundle

class ReferrerDetails(private val originalBundle: Bundle) {
    val referrer: String?
        get() = originalBundle.getString(KEY_INSTALL_REFERRER)
    val referrerClickTimestampMilliseconds: Long
        get() = originalBundle.getLong(KEY_REFERRER_CLICK_TIMESTAMP)
    val installBeginTimestampMilliseconds: Long
        get() = originalBundle.getLong(KEY_INSTALL_BEGIN_TIMESTAMP)
    val appVersion: String?
        get() = originalBundle.getString(KEY_APP_VERSION)

    companion object {
        private const val KEY_INSTALL_REFERRER = "install_referrer"
        private const val KEY_REFERRER_CLICK_TIMESTAMP = "referrer_click_timestamp_milliseconds"
        private const val KEY_INSTALL_BEGIN_TIMESTAMP = "install_begin_timestamp_milliseconds"
        private const val KEY_APP_VERSION = "app_version"
    }
}