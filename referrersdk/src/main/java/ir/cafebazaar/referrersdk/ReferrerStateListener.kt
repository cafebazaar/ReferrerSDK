package ir.cafebazaar.referrersdk

interface ReferrerStateListener {
    fun onReferrerSetupFinished(referrerResponse: ReferrerSDKStates)
    fun onReferrerServiceDisconnected()
}