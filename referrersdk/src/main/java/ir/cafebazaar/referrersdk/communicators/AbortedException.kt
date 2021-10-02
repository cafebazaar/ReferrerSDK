package ir.cafebazaar.referrersdk.communicators

internal class AbortedException(detailMessage: String? = "") : InterruptedException(detailMessage)