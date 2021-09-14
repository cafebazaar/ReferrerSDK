package ir.cafebazaar.referrersdk

interface ReferrerStateListener {
    fun onReferrerSetupFinished(referrerResponse: Int)
    fun onReferrerServiceDisconnected()
}