package com.cafebazaar.referrersdk.model

import android.os.Bundle
import com.cafebazaar.referrersdk.model.ReferrerDetails.Companion.TIMESTAMP_NOT_AVAILABLE

internal class ReferrerDetailsImpl(private val originalBundle: Bundle): ReferrerDetails {

    override val referrer: String?
        get() = originalBundle.getString(KEY_INSTALL_REFERRER)

    override val referrerClickTimestampMilliseconds: Long
        get() = originalBundle.getLong(KEY_REFERRER_CLICK_TIMESTAMP, TIMESTAMP_NOT_AVAILABLE)

    override val installBeginTimestampMilliseconds: Long
        get() = originalBundle.getLong(KEY_INSTALL_BEGIN_TIMESTAMP, TIMESTAMP_NOT_AVAILABLE)

    override val referrerClickTimestampServerMilliseconds: Long
        get() = originalBundle.getLong(KEY_REFERRER_CLICK_TIMESTAMP_SERVER, TIMESTAMP_NOT_AVAILABLE)

    override val installBeginTimestampServerMilliseconds: Long
        get() = originalBundle.getLong(KEY_INSTALL_BEGIN_TIMESTAMP_SERVER, TIMESTAMP_NOT_AVAILABLE)

    override val appVersion: String?
        get() = originalBundle.getString(KEY_APP_VERSION)

    private companion object {
        const val KEY_INSTALL_REFERRER = "install_referrer"
        const val KEY_REFERRER_CLICK_TIMESTAMP = "referrer_click_timestamp_milliseconds"
        const val KEY_INSTALL_BEGIN_TIMESTAMP = "install_begin_timestamp_milliseconds"
        const val KEY_REFERRER_CLICK_TIMESTAMP_SERVER = "referrer_click_timestamp_server_milliseconds"
        const val KEY_INSTALL_BEGIN_TIMESTAMP_SERVER = "install_begin_timestamp_server_milliseconds"
        const val KEY_APP_VERSION = "app_version"
    }
}
