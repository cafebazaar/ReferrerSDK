package com.cafebazaar.servicebase.state

sealed class ClientError {
    abstract val message: String
    abstract val code: Int

    data class ServiceUnAvailable(
        override val message: String,
        override val code: Int
    ) : ClientError() {

        companion object {
            const val BAZAAR_IS_NOT_INSTALL_MESSAGE = "Bazaar Client Is Not Installed"
            const val BAZAAR_IS_NOT_INSTALL_CODE = 0

            const val BAZAAR_IS_NOT_COMPATIBLE_MESSAGE = "Bazaar Client Is Not Compatible"
            const val BAZAAR_IS_NOT_COMPATIBLE_CODE = 1

            const val SDK_COULD_NOT_CONNECT_MESSAGE = "SDK Could Not Connect"
            const val SDK_COULD_NOT_CONNECT_CODE = 2
        }
    }

    data class DeveloperError(
        override val message: String,
        override val code: Int
    ) : ClientError() {

        companion object {
            const val SDK_IS_STARTED_MESSAGE = "SDK Is Started"
            const val SDK_IS_STARTED_CODE = 3
        }
    }

    data class RunTime(
        override val message: String,
        override val code: Int
    ) : ClientError() {

        companion object {
            const val ERROR_DURING_GETTING_REFERRER_DETAILS_MESSAGE =
                "Error during getting referrer details"
            const val ERROR_DURING_GETTING_REFERRER_DETAILS_CODE = 4

            const val ERROR_DURING_CONSUMING_REFERRER_MESSAGE = "Error during consuming referrer"
            const val ERROR_DURING_CONSUMING_REFERRER_CODE = 5
        }
    }
}
