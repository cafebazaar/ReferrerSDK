package com.cafebazaar.referrersdk.model

/**
 * Referrer data that Bazaar recorded for the host application.
 *
 * All timestamps are epoch milliseconds (UTC). A value of [TIMESTAMP_NOT_AVAILABLE] (`-1`)
 * means the timestamp isn't available, e.g. it was recorded by an older Bazaar version.
 */
interface ReferrerDetails {

    /**
     * The referrer string.
     */
    val referrer: String?

    /**
     * Device time of the referrer click.
     */
    val referrerClickTimestampMilliseconds: Long

    /**
     * Device time of the install begin, recorded when the download of the first install
     * actually starts. Updates don't change it, so it always describes the first install.
     * It is [TIMESTAMP_NOT_AVAILABLE] if the install hasn't started yet.
     */
    val installBeginTimestampMilliseconds: Long

    /**
     * Server time of the referrer click, independent of the device clock.
     * Prefer it over [referrerClickTimestampMilliseconds] for ordering and anti-fraud checks.
     * It has whole-second precision and is [TIMESTAMP_NOT_AVAILABLE] when unknown.
     */
    val referrerClickTimestampServerMilliseconds: Long

    /**
     * Server time of the install begin, independent of the device clock.
     * Prefer it over [installBeginTimestampMilliseconds] for ordering and anti-fraud checks.
     * It has whole-second precision and is [TIMESTAMP_NOT_AVAILABLE] when unknown.
     */
    val installBeginTimestampServerMilliseconds: Long

    /**
     * Version code of the application when the referrer was recorded.
     */
    val appVersion: String?

    companion object {
        const val TIMESTAMP_NOT_AVAILABLE = -1L
    }
}
