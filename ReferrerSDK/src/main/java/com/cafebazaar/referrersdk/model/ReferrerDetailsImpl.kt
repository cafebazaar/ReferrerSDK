package com.cafebazaar.referrersdk.model

import android.os.Bundle

internal class ReferrerDetailsImpl(private val originalBundle: Bundle): ReferrerDetails {

    override val referrer: String?
        get() = originalBundle.getString(KEY_INSTALL_REFERRER)

    override val referrerClickTimestampMilliseconds: Long
        get() = originalBundle.getLong(KEY_REFERRER_CLICK_TIMESTAMP)

    override val installBeginTimestampMilliseconds: Long
        get() = originalBundle.getLong(KEY_INSTALL_BEGIN_TIMESTAMP)

    override val appVersion: String?
        get() = originalBundle.getString(KEY_APP_VERSION)

    private companion object {
        const val KEY_INSTALL_REFERRER = "install_referrer"
        const val KEY_REFERRER_CLICK_TIMESTAMP = "referrer_click_timestamp_milliseconds"
        const val KEY_INSTALL_BEGIN_TIMESTAMP = "install_begin_timestamp_milliseconds"
        const val KEY_APP_VERSION = "app_version"
    }
}