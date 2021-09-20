package ir.cafebazaar.referrersdk.communicators

object ReferrerBroadcast {
    private const val BAZAAR_BASE_ACTION = "com.farsitel.bazaar.referrer."
    internal const val ACTION_REFERRER_GET = "${BAZAAR_BASE_ACTION}get"
    internal const val ACTION_REFERRER_CONSUME = "${BAZAAR_BASE_ACTION}consume"
    internal const val KEY_PACKAGE_NAME = "packageName"
    internal const val KEY_INSTALL_TIME = "installTime"
    internal const val KEY_RESPONSE = "response"
}