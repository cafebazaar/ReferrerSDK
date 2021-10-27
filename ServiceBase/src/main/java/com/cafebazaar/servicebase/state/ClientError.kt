package com.cafebazaar.servicebase.state

enum class ClientError(var message: String) {
    ERROR_BAZAAR_IS_NOT_INSTALL("Bazaar Client Is Not Installed"),
    ERROR_BAZAAR_IS_NOT_COMPATIBLE("Bazaar Client Is Not Compatible"),
    ERROR_SDK_COULD_NOT_CONNECT("SDK Could Not Connect"),
    ERROR_SDK_IS_STARTED("SDK Is Started"),
    ERROR_DURING_GETTING_REFERRER_DETAILS("Error during getting referrer details"),
    ERROR_DURING_CONSUMING_REFERRER("Error during consuming referrer")
}
