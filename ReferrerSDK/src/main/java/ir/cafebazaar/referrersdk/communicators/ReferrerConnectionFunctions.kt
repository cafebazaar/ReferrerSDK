package ir.cafebazaar.referrersdk.communicators

import android.os.Bundle

internal interface ReferrerConnectionFunctions {
    val referrer: Bundle?
    fun consumeReferrer(installTime: Long)
}