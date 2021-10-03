package com.cafebazaar.servicebase.state

sealed class ClientError {
    data class ServiceUnAvailable(
        val message: String
    ) : ClientError()
    data class DeveloperError(
        val message: String
    ) : ClientError()
    data class RunTime(
        val message: String
    ) : ClientError()
}
