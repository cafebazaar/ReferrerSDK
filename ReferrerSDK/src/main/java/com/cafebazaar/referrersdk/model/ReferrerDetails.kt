package com.cafebazaar.referrersdk.model

interface ReferrerDetails {
    val referrer: String?
    val referrerClickTimestampMilliseconds: Long
    val installBeginTimestampMilliseconds: Long
    val appVersion: String?
}