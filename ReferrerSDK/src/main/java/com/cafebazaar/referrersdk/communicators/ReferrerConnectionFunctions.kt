package com.cafebazaar.referrersdk.communicators

import android.os.Bundle

internal interface ReferrerConnectionFunctions {
    val referrerBundle: Bundle?
    fun consumeReferrer(installTime: Long)
}