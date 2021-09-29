package ir.cafebazaar.referrersdk

sealed class ReferrerSDKStates {
    object Ok : ReferrerSDKStates()
    object ServiceUnAvailable : ReferrerSDKStates()
    data class DeveloperError(
        val message: String
    ): ReferrerSDKStates()
}
